package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.Likes;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import com.kindnesskattle.bddAtcProject.Repository.LikesRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class GetLikesService {

    private final LikesRepository likesRepository;
    private final UserAccountRepository userAccountRepository;
    private final DonationPostRepository donationPostRepository;

    @Autowired
    public GetLikesService(LikesRepository likesRepository, UserAccountRepository userAccountRepository, DonationPostRepository donationPostRepository) {
        this.likesRepository = likesRepository;
        this.userAccountRepository = userAccountRepository;
        this.donationPostRepository = donationPostRepository;
    }

    public void addLike(Long userId, Long postId) {
        // Null checks for userId and postId
        if (userId == null || postId == null) {
            log.warn("User ID or Post ID is null. Skipping like operation.");
            return;
        }

        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DonationPost post = donationPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Donation post not found"));

        // Null checks for username and post ID
        if (user.getUsername() == null || post.getPostId() == null) {
            log.warn("Username or post ID is null. Skipping like operation.");
            return;
        }

        log.info("Log message: username = " + user.getUsername() + ", post id = " + post.getPostId());

        Optional<Likes> existingLike = likesRepository.findByUserAndPost(user, post);
        if (existingLike.isPresent()) {
            throw new RuntimeException("User has already liked this post.");
        }

        Likes like = new Likes();
        like.setUser(user);
        like.setPost(post);
        like.setLikeDateTime(LocalDateTime.now());

        likesRepository.save(like);
    }


}
