package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.DTO.UserAnalyticsDTO;
import com.kindnesskattle.bddAtcProject.Services.UserAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kindnesskettle/useranalytics")
public class UserAnalyticsController {

    private final UserAnalyticsService userAnalyticsService;

    @Autowired
    public UserAnalyticsController(UserAnalyticsService userAnalyticsService) {
        this.userAnalyticsService = userAnalyticsService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserAnalyticsDTO> getUserAnalytics(@PathVariable Long userId) {
        System.out.println("user id "+userId);
        UserAnalyticsDTO userAnalyticsDTO = userAnalyticsService.getUserAnalytics(userId);
        if (userAnalyticsDTO != null) {
            return new ResponseEntity<>(userAnalyticsDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
