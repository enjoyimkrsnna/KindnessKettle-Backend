package com.kindnesskattle.bddAtcProject.Controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class VerifyTokenController {
    private static final String SECRET_KEY = "5R@hP2A+gQkzXK9vS4M*E7jWdGdF5aJd";
    @GetMapping
    public ResponseEntity<String> verifyToken(@RequestHeader("Authorization") String token) {
        try {
            // Remove "Bearer " prefix from the token
            String jwtToken = token.substring(7);
            // Parse and verify the token
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody();
            // Extract the subject from the token
            String email = claims.getSubject();
            // Token is valid, return the email
            return ResponseEntity.ok("Token is valid for email: " + email);
        } catch (Exception e) {
            // Token verification failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
