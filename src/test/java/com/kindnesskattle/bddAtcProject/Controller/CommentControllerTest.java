package com.kindnesskattle.bddAtcProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindnesskattle.bddAtcProject.DTO.CommentRequestDTO;
import com.kindnesskattle.bddAtcProject.Entities.Comment;
import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    public void testAddComment() throws Exception {

        CommentRequestDTO requestDTO = new CommentRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setPostId(1L);
        requestDTO.setCommentContent("Test comment");


        Comment comment = new Comment();
        comment.setComment_id(1);
        comment.setUser_id(new UserAccount());
        comment.setDonation_post(new DonationPost());
        comment.setComment_content(requestDTO.getCommentContent());
        comment.setComment_date_time(new Date());

        when(commentService.addComment(anyLong(), anyLong(), anyString())).thenReturn(comment);


        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment_id").value(1))
                .andExpect(jsonPath("$.comment_content").value("Test comment"));

        verify(commentService, times(1)).addComment(anyLong(), anyLong(), anyString());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testDeleteComment() throws Exception {

        Long commentId = 1L;

        when(commentService.deleteComment(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/delete_comments/{commentId}", commentId))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully"));


        verify(commentService, times(1)).deleteComment(anyLong());
    }

    @Test
    public void testDeleteCommentNotFound() throws Exception {

        Long commentId = 1L;

        when(commentService.deleteComment(anyLong())).thenReturn(false);


        mockMvc.perform(delete("/delete_comments/{commentId}", commentId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Comment not found or could not be deleted"));

        verify(commentService, times(1)).deleteComment(anyLong());
    }

}
