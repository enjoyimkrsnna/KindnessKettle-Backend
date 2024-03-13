package com.kindnesskattle.bddAtcProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kindnesskattle.bddAtcProject.Services.CreateDonationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DonationControllerTest {

    @InjectMocks
    private DonationController donationController;

    @Mock
    private CreateDonationService createDonationService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(donationController).build();
    }

//    @Test
//    public void testCreateDonationPost() throws Exception {
//        DontationAddressDTO requestDTO = new DontationAddressDTO(
//                1L, 2L, "Test address", "123456", 1.0, 2.0, "image.jpg", LocalDateTime.now().now());
//
//        DonationPost donationPost = new DonationPost();
//
//        when(createDonationService.createDonationPost(any())).thenReturn(donationPost);
//
//        mockMvc.perform(post("/donationPosts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Donation post added successfully"));
//    }

    @Test
    public void testDeleteDonationPost() throws Exception {
        Long postId = 1L;

        mockMvc.perform(delete("/donationPosts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().string("Donation post and associated address deleted successfully"));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
