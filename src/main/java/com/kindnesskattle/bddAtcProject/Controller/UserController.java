package com.kindnesskattle.bddAtcProject.Controller;


import com.kindnesskattle.bddAtcProject.DTO.UserDto;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Services.S3UploadService;
import com.kindnesskattle.bddAtcProject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private S3UploadService s3UploadService;

    @PutMapping("/update/{emailAddress}")
    public ResponseEntity<UserAccount> updateUserProfile(
            @PathVariable String emailAddress,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("profileDescription") String profileDescription,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage
    ) {

        System.out.println("updating profile");
        // Create UserDto with updated data
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setFirstName(firstName);
        updatedUserDto.setLastName(lastName);
        updatedUserDto.setProfileDescription(profileDescription);

        // Upload profile image to S3 if provided
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = s3UploadService.uploadPhotoToProfiles(profileImage);
            updatedUserDto.setImageUrl(imageUrl);
        }

        UserAccount user = userService.getUserByEmail(emailAddress);
        if (user != null) {
            // Exclude the username field from being updated
            updatedUserDto.setUsername(user.getUsername());

            UserAccount updatedUser = userService.updateUserProfile(emailAddress, updatedUserDto);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
