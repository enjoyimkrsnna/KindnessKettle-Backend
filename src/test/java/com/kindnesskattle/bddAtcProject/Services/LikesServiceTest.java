package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.DTO.LikesSummaryDTO;
import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.Likes;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import com.kindnesskattle.bddAtcProject.Repository.LikesRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class LikesServiceTest {

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private DonationPostRepository donationPostRepository;

    @InjectMocks
    private LikesService likesService;

    @Test
    void addLike_Successful() {
        // Mock data
        Long userId = 1L;
        Long postId = 2L;

        UserAccount user = new UserAccount();
        user.setUsername("testUser");

        DonationPost post = new DonationPost();
        post.setPostId(postId);

//        setting up a mock behavior using Mockito
        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(donationPostRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(likesRepository.findByUserAndPost(any(UserAccount.class), any(DonationPost.class))).thenReturn(Optional.empty());

        // Execute
        assertDoesNotThrow(() -> likesService.addLike(userId, postId));

        // Verify
        Mockito.verify(likesRepository, Mockito.times(1)).save(any(Likes.class));
    }

    @Test
    void addLike_UserNotFound() {
        // Mock data
        Long userId = 1L;
        Long postId = 2L;

        //setting up a mock behavior using Mockito
        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(RuntimeException.class, () -> likesService.addLike(userId, postId));
        Mockito.verify(likesRepository, Mockito.never()).save(any(Likes.class));
    }

    @Test
    void addLike_PostNotFound() {
        // Mock data
        Long userId = 1L;
        Long postId = 2L;

        UserAccount user = new UserAccount();
        user.setUsername("testUser");

        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(donationPostRepository.findById(postId)).thenReturn(Optional.empty());

        // Execute and Verify
        assertThrows(RuntimeException.class, () -> likesService.addLike(userId, postId));
        Mockito.verify(likesRepository, Mockito.never()).save(any(Likes.class));
    }

    @Test
    void addLike_UserAndPostNull() {
        // Mock data
        Long userId = null;
        Long postId = null;

        // Execute and Verify
        assertDoesNotThrow(() -> likesService.addLike(userId, postId));
        Mockito.verify(likesRepository, Mockito.never()).save(any(Likes.class));
    }

    @Test
    void addLike_UserOrPostAttributesNull() {
        // Mock data
        Long userId = 1L;
        Long postId = 2L;

        UserAccount user = new UserAccount(); // No username
        DonationPost post = new DonationPost(); // No postId

        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(donationPostRepository.findById(postId)).thenReturn(Optional.of(post));

        // Execute and Verify
        assertDoesNotThrow(() -> likesService.addLike(userId, postId));
        Mockito.verify(likesRepository, Mockito.never()).save(any(Likes.class));
    }

    @Test
    void addLike_UserAlreadyLiked() {
        // Mock data
        Long userId = 1L;
        Long postId = 2L;

        UserAccount user = new UserAccount();
        user.setUsername("testUser");

        DonationPost post = new DonationPost();
        post.setPostId(postId);

        // Setting up mock behaviors
        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(donationPostRepository.findById(postId)).thenReturn(Optional.of(post));

        // Mocking the scenario where the user has already liked the post
        Mockito.when(likesRepository.findByUserAndPost(any(UserAccount.class), any(DonationPost.class))).thenReturn(Optional.of(new Likes()));

        // Execute and Verify
        assertThrows(RuntimeException.class, () -> likesService.addLike(userId, postId));

        // Verify that save method was never called on likesRepository
        Mockito.verify(likesRepository, Mockito.never()).save(any(Likes.class));
    }

    @Test
    void getLikesSummaryByPostId_Successful() {
        // Mock data
        Long postId = 1L;

        Likes like1 = new Likes();
        like1.setUser(new UserAccount());
        like1.setPost(new DonationPost());

        Likes like2 = new Likes();
        like2.setUser(new UserAccount());
        like2.setPost(new DonationPost());

        // Setting up mock behaviors
        Mockito.when(likesRepository.findAllByPost_PostId(postId)).thenReturn(Arrays.asList(like1, like2));

        // Execute
        List<LikesSummaryDTO> likesSummaryList = likesService.getLikesSummaryByPostId(postId);

        // Verify
        assertNotNull(likesSummaryList);
        assertEquals(2, likesSummaryList.size());
    }

    @Test
    void getLikesSummaryByPostId_EmptyList() {
        // Mock data
        Long postId = 1L;

        Mockito.when(likesRepository.findAllByPost_PostId(postId)).thenReturn(List.of());

        // Execute
        List<LikesSummaryDTO> likesSummaryList = likesService.getLikesSummaryByPostId(postId);

        // Verify
        assertNotNull(likesSummaryList);
        assertTrue(likesSummaryList.isEmpty());
    }



}
