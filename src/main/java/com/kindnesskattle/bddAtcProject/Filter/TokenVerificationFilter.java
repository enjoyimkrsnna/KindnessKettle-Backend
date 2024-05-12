package com.kindnesskattle.bddAtcProject.Filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;
import java.io.IOException;

public class TokenVerificationFilter extends GenericFilterBean {

    private static final String SECRET_KEY = "5R@hP2A+gQkzXK9vS4M*E7jWdGdF5aJd";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpResponse.setHeader("Access-Control-Allow-Origin", "*");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            httpResponse.setStatus(HttpStatus.OK.value());
            return;
        }

        // Allow requests to the /login/auth endpoint to bypass token verification
        if ((httpRequest.getRequestURI().equals("/login/auth") && httpRequest.getMethod().equals("POST")) || httpRequest.getRequestURI().equals("/test-cors") ) {
            chain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        String token = extractToken(httpRequest.getHeader("Authorization"));

        if (token != null) {
            try {
                // Parse and verify the token
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

                // Extract the subject (email) from the token
                String email = claims.getSubject();

                // Proceed with the filter chain
                chain.doFilter(request, response);

            } catch (Exception e) {
                // Token verification failed
                httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
                httpResponse.getWriter().write("Invalid token");
                return;
            }
        } else {
            // Token is missing
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("Token is missing");
            return;
        }
    }

    private String extractToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}