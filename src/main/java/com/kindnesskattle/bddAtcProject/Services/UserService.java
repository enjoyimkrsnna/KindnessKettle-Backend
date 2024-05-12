package com.kindnesskattle.bddAtcProject.Services;

import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import com.kindnesskattle.bddAtcProject.Repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public UserAccount getUserByEmail(String emailAddress) {
        return userAccountRepository.findByEmailAddress(emailAddress);
    }
}

