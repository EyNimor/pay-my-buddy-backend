package com.openclassroom.paymybuddy.dao;

import java.util.List;

import com.openclassroom.paymybuddy.model.entity.User;
import com.openclassroom.paymybuddy.model.entity.UserNetwork;
import com.openclassroom.paymybuddy.model.entity.composedKey.UserNetworkComposedKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserNetworkRepository extends JpaRepository<UserNetwork, UserNetworkComposedKey> {

    @Query("select n from UserNetwork n where key.userEmail = :user")
    List<UserNetwork> findUserNetworkByUserEmail(User user);

    @Query("select n from UserNetwork n where key.friendEmail = :user")
    List<UserNetwork> findUserNetworkByFriendEmail(User user);
    
}
