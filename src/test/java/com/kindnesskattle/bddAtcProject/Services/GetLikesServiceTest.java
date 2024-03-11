package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.Likes;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import com.kindnesskattle.bddAtcProject.Repository.LikesRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetLikesServiceTest {

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private DonationPostRepository donationPostRepository;

    @InjectMocks
    private GetLikesService getLikesService;

    @Test
    void testAddLike_Successful() {
        // Arrange
        Long userId = 1L;
        Long postId = 1L;

        UserAccount user = new UserAccount();
        user.setUserId(userId);
        user.setUsername("testUser");

        DonationPost post = new DonationPost();
        post.setPostId(postId);

        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        when(donationPostRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likesRepository.findByUserAndPost(user, post)).thenReturn(Optional.empty());

        // Act
        getLikesService.addLike(userId, postId);

        // Assert
        verify(likesRepository, times(1)).save(any());
    }

    @Test
    void testAddLike_UserNotFound() {
        // Arrange
        Long userId = 1L;
        Long postId = 1L;

        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> getLikesService.addLike(userId, postId));

        // Verify interactions
        verifyNoInteractions(likesRepository, donationPostRepository);
        verifyNoMoreInteractions(userAccountRepository);

        // Assert the exception message
        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testAddLike_PostNotFound() {
        // Arrange
        Long userId = 1L;
        Long postId = 1L;

        UserAccount user = new UserAccount();
        user.setUserId(userId);
        user.setUsername("testUser");

        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        when(donationPostRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> getLikesService.addLike(userId, postId));

        // Verify interactions
        verifyNoInteractions(likesRepository);
        verifyNoMoreInteractions(userAccountRepository);

        // Assert the exception message
        String expectedMessage = "Donation post not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testAddLike_UserAlreadyLikedPost() {
        // Arrange
        Long userId = 1L;
        Long postId = 1L;

        UserAccount user = new UserAccount();
        user.setUserId(userId);
        user.setUsername("testUser");

        DonationPost post = new DonationPost();
        post.setPostId(postId);

        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        when(donationPostRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likesRepository.findByUserAndPost(user, post)).thenReturn(Optional.of(new Likes()));

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> getLikesService.addLike(userId, postId));

        // Verify interactions
        verify(likesRepository, never()).save(any()); // Corrected verification
        verifyNoMoreInteractions(userAccountRepository);

        // Assert the exception message
        String expectedMessage = "User has already liked this post";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
