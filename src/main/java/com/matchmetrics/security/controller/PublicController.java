package com.matchmetrics.security.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("matchmetrics/api/v0/public")
public class PublicController {

    @Value("${api.teams.url}")
    private String teamsApiUrl;

    @Value("${api.token}")
    private String apiToken;

    private final RestTemplate restTemplate;

    public PublicController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/teams")
    public ResponseEntity<?> getTeams() {
        System.out.println("Received request to get all teams.");
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    teamsApiUrl, HttpMethod.GET, entity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving teams.");
        }
    }
}