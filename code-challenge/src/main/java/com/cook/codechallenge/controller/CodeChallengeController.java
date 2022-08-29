package com.cook.codechallenge.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cook.codechallenge.service.CodeChallengeService;

@RestController
@Log4j2
@AllArgsConstructor
@RequestMapping(path = "/challenge")
public class CodeChallengeController {


    /**
     * The service instance used to handle business logic for the controller
     * @see CodeChallengeService
     */
    private final CodeChallengeService codeChallengeService;

    /**
     * REST endpoint to launch the code workflow described in the Bet Fanatics Coding Challenge
     *
     * Retrieve page 3 of the list of all users.
     * Sort the retrieved user list by name.
     * After sorting, log the name of the last user.
     * Update that user's name to a new value and use the correct http method to save it.
     * Delete that user.
     * Attempt to retrieve a nonexistent user with ID 5555. Log the resulting http response code.
     *
     * @return a response entity containing a message and http status code
     */
    @GetMapping(path = "/launch", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> launchFanaticsCodeChallenge() {
        return codeChallengeService.launchCodeChallenge();
    }
}
