package com.example.podcastsearch.model;

import java.util.List;

public class Podcast {

    private String trackName;
    private String artistName;
    private String artworkUrl100;
    private String collectionName;
    private String feedUrl;
    private List<Episode> episodes;  // Added episodes list

    public Podcast() {
    }

    public Podcast(String trackName, String artistName, String artworkUrl100, String collectionName, String feedUrl, List<Episode> episodes) {
        this.trackName = trackName;
        this.artistName = artistName;
        this.artworkUrl100 = artworkUrl100;
        this.collectionName = collectionName;
        this.feedUrl = feedUrl;
        this.episodes = episodes;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
}
