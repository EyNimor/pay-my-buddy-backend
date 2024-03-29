package com.openclassroom.paymybuddy.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import com.openclassroom.paymybuddy.controller.FriendEndpointController;
import com.openclassroom.paymybuddy.dao.TransactionsRepository;
import com.openclassroom.paymybuddy.dao.UserNetworkRepository;
import com.openclassroom.paymybuddy.dao.UsersRepository;
import com.openclassroom.paymybuddy.methods.TestsMethods;
import com.openclassroom.paymybuddy.model.TestsVariables;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest @TestPropertySource(locations="classpath:application-test.properties")
public class FriendEndpointControllerTests extends FriendEndpointController {

    @Autowired
    protected UsersRepository usersRepo;

    @Autowired
    protected TransactionsRepository transactionsRepo;

    @Autowired
    protected UserNetworkRepository userNetworkRepo;

    protected static TestsVariables vars;

    protected static TestsMethods testsMethods;

    @BeforeAll
    static void setUp() {
        vars = new TestsVariables();
    }

    @BeforeEach
    void setUpPerTest() {
        testsMethods = new TestsMethods(usersRepo, transactionsRepo, userNetworkRepo);

        testsMethods.cleanTransactionTable();
        testsMethods.cleanUserNetworkTable();
        testsMethods.cleanUserTable();

        usersRepo.saveAll(vars.getUsersList());
        userNetworkRepo.saveAll(vars.getNetworkList());
        transactionsRepo.saveAll(vars.getTransactionList());
    }

    @Test
    void getFriendsTest() {
        List<String> expectedFriendList = new ArrayList<>();
        expectedFriendList.add(vars.getFriend1Email());
        expectedFriendList.add(vars.getFriend2Email());

        List<String> returnedFriendsList = this.getFriendsRequest(vars.getUserEmail()).getBody();
        assertEquals(expectedFriendList.toString(), returnedFriendsList.toString());
    }

    @Test
    void postNewFriendTest() {
        usersRepo.save(vars.getNewUser());
        usersRepo.save(vars.getNewFriend());
        assertEquals(this.postFriendRequest(vars.getNewNewFriend()).getStatusCode(), HttpStatus.CREATED);

        boolean isSaved = userNetworkRepo.findById(vars.getNewUserNetwork().getKey()).isPresent();
        assertTrue(isSaved);
    }

    @Test
    void deleteFriendTest() {
        postNewFriendTest();
        assertEquals(this.deleteFriendRequest(vars.getNewUserEmail(), vars.getNewFriendEmail()).getStatusCode(), HttpStatus.OK);

        boolean isSaved = userNetworkRepo.findById(vars.getNewUserNetwork().getKey()).isPresent();
        assertFalse(isSaved);
    }

    @AfterAll
    static void cleanAfterTests() {
        testsMethods.cleanTransactionTable();
        testsMethods.cleanUserNetworkTable();
        testsMethods.cleanUserTable();
    }

}
