package com.kindnesskattle.bddAtcProject.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-cors")
    public ResponseEntity<String> testCors() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        headers.add("Access-Control-Allow-Headers", "Content-Type");
        return ResponseEntity.ok().headers(headers).body("CORS Test Successful");
    }
}

