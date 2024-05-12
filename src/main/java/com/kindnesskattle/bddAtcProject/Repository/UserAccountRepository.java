package com.kindnesskattle.bddAtcProject.Repository;


import com.kindnesskattle.bddAtcProject.Entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByEmailAddress(String emailAddress);

}

