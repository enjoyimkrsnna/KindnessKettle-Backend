package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.DTO.DontationAddressDTO;
import com.kindnesskattle.bddAtcProject.Entities.Address;
import com.kindnesskattle.bddAtcProject.Entities.DonationPost;
import com.kindnesskattle.bddAtcProject.Entities.FoodType;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.AddressRepository;
import com.kindnesskattle.bddAtcProject.Repository.DonationPostRepository;
import com.kindnesskattle.bddAtcProject.Repository.FoodTypeRepository;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateDonationServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private FoodTypeRepository foodTypeRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private DonationPostRepository donationPostRepository;

    @InjectMocks
    private CreateDonationService createDonationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createDonationPost_ValidInput_ShouldReturnDonationPost() {
        // Mocking data
        DontationAddressDTO request = new DontationAddressDTO();
        request.setAddressLine("Sample Address");
        request.setPincode("12345");
        request.setLatitude(123.456);
        request.setLongitude(456.789);
        request.setUserId(1L);
        request.setFoodTypeId(1L);
        request.setFoodImageUrl("http://example.com/food.jpg");
        request.setTimeAvailable(LocalDateTime.now());

        UserAccount user = new UserAccount();
        user.setUserId(1L);
        FoodType foodType = new FoodType();
        foodType.setFoodId(1L);
        Address address = new Address();
        address.setAddressId(1L);
        DonationPost donationPost = new DonationPost();
        donationPost.setPostId(1L);


        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(user));
        when(foodTypeRepository.findById(1L)).thenReturn(Optional.of(foodType));
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(donationPostRepository.save(any(DonationPost.class))).thenReturn(donationPost);


        DonationPost result = createDonationService.createDonationPost(request);

        assertNotNull(result);
        assertEquals(1L, result.getPostId());
    }

    @Test
    void deleteDonationPost_ValidPostId_ShouldDeletePostAndAddress() {

        DonationPost donationPost = new DonationPost();
        donationPost.setPostId(1L);
        Address address = new Address();
        address.setAddressId(1L);
        donationPost.setAddress(address);


        when(donationPostRepository.findById(1L)).thenReturn(Optional.of(donationPost));
        doNothing().when(donationPostRepository).delete(donationPost);
        doNothing().when(addressRepository).delete(address);


        assertDoesNotThrow(() -> createDonationService.deleteDonationPost(1L));


        verify(donationPostRepository, times(1)).findById(1L);
        verify(donationPostRepository, times(1)).delete(donationPost);
        verify(addressRepository, times(1)).delete(address);
    }

    @Test
    void deleteDonationPost_PostNotFound_ShouldThrowIllegalArgumentException() {

        when(donationPostRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(IllegalArgumentException.class, () -> createDonationService.deleteDonationPost(1L));


        verify(donationPostRepository, times(1)).findById(1L);
        verify(donationPostRepository, never()).delete(any());
        verify(addressRepository, never()).delete(any());
    }

    @Test
    void deleteDonationPost_ExceptionThrown_ShouldThrowException() {

        DonationPost donationPost = new DonationPost();
        donationPost.setPostId(1L);
        Address address = new Address();
        address.setAddressId(1L);
        donationPost.setAddress(address);


        when(donationPostRepository.findById(1L)).thenReturn(Optional.of(donationPost));
        doThrow(new DataIntegrityViolationException("Constraint violation")).when(donationPostRepository).delete(donationPost);

        assertThrows(DataIntegrityViolationException.class, () -> createDonationService.deleteDonationPost(1L));


        verify(donationPostRepository, times(1)).findById(1L);
        verify(donationPostRepository, times(1)).delete(donationPost);
        verify(addressRepository, never()).delete(any());
    }


}
