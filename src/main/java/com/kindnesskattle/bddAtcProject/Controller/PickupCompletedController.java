package com.kindnesskattle.bddAtcProject.Controller;

import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.PickupCompleted;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.PickupCompletedRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PickupCompletedController {

    private final PickupCompletedRepository pickupCompletedRepository;
    private final UserAccountRepository userAccountRepository;
    private final DonationPostRepository donationPostRepository;

    @Autowired
    public PickupCompletedController(PickupCompletedRepository pickupCompletedRepository,
                                     UserAccountRepository userAccountRepository,
                                     DonationPostRepository donationPostRepository) {
        this.pickupCompletedRepository = pickupCompletedRepository;
        this.userAccountRepository = userAccountRepository;
        this.donationPostRepository = donationPostRepository;
    }

    @PostMapping("/pickup")
    public ResponseEntity<String> insertPickupCompleted(@RequestBody PickupCompletedRequest request) {
        if (request.getPickedUpByUserId() == null || request.getPostId() == null) {
            return ResponseEntity.badRequest().body("User ID or Post ID cannot be null");
        }

        // Check if user exists
        Optional<UserAccount> userExists = userAccountRepository.findById(request.getPickedUpByUserId());


        // Check if post exists
        Optional<DonationPost> postExists = donationPostRepository.findById(request.getPostId());
        if (userExists.isEmpty() && postExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User and Post not found");
        } else if (userExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } else if (postExists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }


        // Create a new PickupCompleted object
        PickupCompleted pickupCompleted = new PickupCompleted(request.getPickedUpByUserId(), request.getPostId(), LocalDateTime.now());

        // Save the PickupCompleted object to the database
        pickupCompletedRepository.save(pickupCompleted);

        // Return a success response
        return ResponseEntity.status(HttpStatus.CREATED).body("Pickup completed record created successfully");
    }

    // Getters and Setters for Repositories

    static class PickupCompletedRequest {
        private Long pickedUpByUserId;
        private Long postId;

        // Getters and Setters
        public Long getPickedUpByUserId() {
            return pickedUpByUserId;
        }

        public void setPickedUpByUserId(Long pickedUpByUserId) {
            this.pickedUpByUserId = pickedUpByUserId;
        }

        public Long getPostId() {
            return postId;
        }

        public void setPostId(Long postId) {
            this.postId = postId;
        }
    }


    @GetMapping("/getpickup/{userId}")
    public ResponseEntity<List<PickupCompletedWithDonationPostResponse>> getPickupCompletedWithDonationPostByUserId(@PathVariable Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        // Retrieve pickup completed records by userId
        List<PickupCompleted> pickupCompletedList = pickupCompletedRepository.findByPickedUpByUserId(userId);

        // Check if records exist
        if (pickupCompletedList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Create a list to hold PickupCompletedWithDonationPostResponse objects
        List<PickupCompletedWithDonationPostResponse> responseList = new ArrayList<>();

        // Iterate over pickupCompletedList and fetch details of associated donation posts
        for (PickupCompleted pickupCompleted : pickupCompletedList) {
            Long postId = pickupCompleted.getPostId();
            Optional<DonationPost> donationPostOptional = donationPostRepository.findById(postId);
            donationPostOptional.ifPresent(donationPost -> {
                // Create PickupCompletedWithDonationPostResponse object with details of pickup completed record and associated donation post
                PickupCompletedWithDonationPostResponse response = new PickupCompletedWithDonationPostResponse(pickupCompleted, donationPost);
                responseList.add(response);
            });
        }

        // Return the list of PickupCompletedWithDonationPostResponse objects
        return ResponseEntity.ok(responseList);
    }

    // Inner class representing the response including PickupCompleted and DonationPost details
    static class PickupCompletedWithDonationPostResponse {
        private PickupCompleted pickupCompleted;
        private DonationPost donationPost;

        // Constructor
        public PickupCompletedWithDonationPostResponse(PickupCompleted pickupCompleted, DonationPost donationPost) {
            this.pickupCompleted = pickupCompleted;
            this.donationPost = donationPost;
        }

        // Getters
        public PickupCompleted getPickupCompleted() {
            return pickupCompleted;
        }

        public DonationPost getDonationPost() {
            return donationPost;
        }
    }

}
