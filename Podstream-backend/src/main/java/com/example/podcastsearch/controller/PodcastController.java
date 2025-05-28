package com.example.podcastsearch.controller;

import com.example.podcastsearch.model.Podcast;
import com.example.podcastsearch.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String term) {
        try {
            if (term == null || term.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Search term must not be empty");
            }

            List<Podcast> results = podcastService.searchPodcasts(term);

            if (results.isEmpty()) {
                return ResponseEntity.ok("No podcasts found for term: " + term);
            }

            return ResponseEntity.ok(results);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            // Log the error
            System.err.println("Error while processing search request: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
