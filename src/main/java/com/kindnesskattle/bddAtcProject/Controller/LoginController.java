package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.AuthResponse;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private final UserService userService;
    private static final String SECRET_KEY = "5R@hP2A+gQkzXK9vS4M*E7jWdGdF5aJd";
    private static final long EXPIRATION_TIME = 864_000_000;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth")
    public AuthResponse generateToken(@RequestBody String email) {
        String emailexract = email.split(":")[1].replaceAll("[^a-zA-Z0-9@.]", "").replaceAll("}", "");
        System.out.println("calling the generate token method with email "+ emailexract);
        // Calculate token expiration time
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        // Create a JWT token with the email as the subject

        System.out.println("checking email is in our system or not");
        UserAccount user_id = userService.getUserByEmail(emailexract);
        System.out.println("user_id is "+user_id);

        if(user_id==null)
        {
            return null;
        }
        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserAccount(user_id);
        return response;
    }
}