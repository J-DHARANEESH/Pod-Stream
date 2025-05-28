
package com.example.podcastsearch.service;

import com.example.podcastsearch.model.Podcast;
import com.example.podcastsearch.model.Episode;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class PodcastService {

    private static final String ITUNES_API_URL = "https://itunes.apple.com/search";

    public List<Podcast> searchPodcasts(String term) {
        RestTemplate restTemplate = new RestTemplate();

        // Set up converter for JSON and text/javascript
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.valueOf("text/javascript")
        ));
        restTemplate.setMessageConverters(List.of(converter));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ITUNES_API_URL)
                .queryParam("term", term)
                .queryParam("media", "podcast")
                .queryParam("limit", 20);

        try {
            Map<String, Object> response = restTemplate.getForObject(builder.toUriString(), Map.class);

            if (response == null || !response.containsKey("results")) {
                throw new RuntimeException("Invalid API response");
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

            List<Podcast> podcasts = new ArrayList<>();
            for (Map<String, Object> item : results) {
                Podcast podcast = new Podcast();
                podcast.setTrackName((String) item.getOrDefault("trackName", ""));
                podcast.setArtistName((String) item.getOrDefault("artistName", ""));
                podcast.setArtworkUrl100((String) item.getOrDefault("artworkUrl100", ""));
                podcast.setCollectionName((String) item.getOrDefault("collectionName", ""));
                podcast.setFeedUrl((String) item.getOrDefault("feedUrl", ""));

                // Fetch episodes and set them to podcast
                if (podcast.getFeedUrl() != null && !podcast.getFeedUrl().isEmpty()) {
                    List<Episode> episodes = fetchEpisodes(podcast.getFeedUrl());
                    podcast.setEpisodes(episodes);
                } else {
                    podcast.setEpisodes(Collections.emptyList());
                }

                podcasts.add(podcast);
            }

            return podcasts;
        } catch (Exception e) {
            System.err.println("Error while fetching podcasts: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("iTunes API call failed", e);
        }
    }

    public List<Episode> fetchEpisodes(String feedUrl) {
        List<Episode> episodes = new ArrayList<>();

        try {
            // Open connection
            URL url = new URL(feedUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // Some feeds require User-Agent
            int responseCode = conn.getResponseCode();

            if (responseCode != 200) {
                System.err.println("Failed to fetch feed: HTTP " + responseCode);
                return episodes;
            }

            // Read content as String first
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseContent = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                responseContent.append(inputLine).append("\n");
            }
            in.close();

            String xmlContent = responseContent.toString();

            // Debug print the content, optionally:
            System.out.println("Feed content preview: " + xmlContent.substring(0, Math.min(500, xmlContent.length())));

            // Now parse XML from String
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse from InputSource wrapping a StringReader
            org.xml.sax.InputSource is = new org.xml.sax.InputSource(new java.io.StringReader(xmlContent));
            Document doc = builder.parse(is);

            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);

                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) item;

                    String title = getTagValue("title", element);
                    String pubDate = getTagValue("pubDate", element);
                    String description = getTagValue("description", element);

                    String audioUrl = "";
                    NodeList enclosures = element.getElementsByTagName("enclosure");
                    if (enclosures.getLength() > 0) {
                        Element enclosure = (Element) enclosures.item(0);
                        audioUrl = enclosure.getAttribute("url");
                    }

                    episodes.add(new Episode(title, audioUrl, pubDate, description));
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to parse RSS feed: " + e.getMessage());
            e.printStackTrace();
        }

        return episodes;
    }


    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null && node.getFirstChild() != null) {
                return node.getFirstChild().getNodeValue();
            }
        }
        return "";
    }
}
