package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.DTO.UserDto;
import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccount getUserByEmail(String emailAddress) {
        return userAccountRepository.findByEmailAddress(emailAddress);
    }

    public UserAccount registerUser(UserDto userDto) {
        // You can perform any additional validation or business logic here before saving the user
                UserAccount userAccount = new UserAccount();

                userAccount.setFirstName(userDto.getFirstName());
                userAccount.setLastName((userDto.getLastName()));
                userAccount.setImageUrl(userDto.getImageUrl());
                userAccount.setUsername(userDto.getUsername());
                userAccount.setEmailAddress(userDto.getEmailAddress());
                userAccount.setProfileDescription(userDto.getProfileDescription());
                userAccount.setIsActive(userDto.isIs_active());

        return userAccountRepository.save(userAccount);


    }

}

