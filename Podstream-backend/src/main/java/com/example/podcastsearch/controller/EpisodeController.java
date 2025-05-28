package com.example.podcastsearch.controller;
import com.example.podcastsearch.model.Episode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EpisodeController {

    @GetMapping(value = "/episodes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Episode> getEpisodes(@RequestParam String feedUrl) {
        List<Episode> episodes = new ArrayList<>();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new URL(feedUrl).openStream());

            NodeList items = doc.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String title = getTagValue(item, "title");
                String pubDate = getTagValue(item, "pubDate");
                String description = getTagValue(item, "description");

                // Extract audio URL from <enclosure> tag (common in podcast feeds)
                String audioUrl = null;
                NodeList enclosureList = item.getElementsByTagName("enclosure");
                if (enclosureList.getLength() > 0) {
                    Element enclosure = (Element) enclosureList.item(0);
                    audioUrl = enclosure.getAttribute("url");
                }

                if (audioUrl != null) {
                    episodes.add(new Episode(title, audioUrl, pubDate, description));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch or parse RSS feed");
        }
        return episodes;
    }

    private String getTagValue(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return null;
    }
}
