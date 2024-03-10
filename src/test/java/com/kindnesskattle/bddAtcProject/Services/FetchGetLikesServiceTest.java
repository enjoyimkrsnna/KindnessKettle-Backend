package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.DTO.LikesSummaryDTO;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FetchGetLikesServiceTest {

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private DonationPostRepository donationPostRepository;

    @InjectMocks
    private FetchLikesService fetchLikesService;

    FetchGetLikesServiceTest() {
    }

    @Test
    void testGetLikesSummaryByPostId() {

        Long postId = 1L;
        Likes like1 = new Likes(new UserAccount(), new DonationPost());
        Likes like2 = new Likes(new UserAccount(), new DonationPost());
        List<Likes> likesList = Arrays.asList(like1, like2);

        when(likesRepository.findAllByPost_PostId(postId)).thenReturn(likesList);

        // Act
        List<LikesSummaryDTO> result = fetchLikesService.getLikesSummaryByPostId(postId);

        // Assert
        assertEquals(2, result.size());
        verify(likesRepository, times(1)).findAllByPost_PostId(postId);
    }


}
