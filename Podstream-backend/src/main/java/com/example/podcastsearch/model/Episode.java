package com.example.podcastsearch.model;

public class Episode {
    private String title;
    private String audioUrl;
    private String pubDate;
    private String description;

    public Episode() {}

    public Episode(String title, String audioUrl, String pubDate, String description) {
        this.title = title;
        this.audioUrl = audioUrl;
        this.pubDate = pubDate;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
