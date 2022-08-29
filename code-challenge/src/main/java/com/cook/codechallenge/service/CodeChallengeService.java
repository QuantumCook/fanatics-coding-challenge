package com.cook.codechallenge.service;

import com.cook.codechallenge.client.GoRestClient;
import com.cook.codechallenge.domain.UserInfo;
import com.cook.codechallenge.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class CodeChallengeService {

    private static final String BASE_URL = "https://gorest.co.in";
    private static final String GO_REST_USERS = "/public/v2/users";
    private static final String PAGE_NUMBER = "3";
    private static final String PAGE = "page";
    private static final String ACCESS_TOKEN =
            "cf08271a4489bba9b6b21ff39fb31bf573b66bcb0c0638ac6043f08dd40370f2";
    private static final String LEA_COOK = "Lea Cook";
    private static final String NONEXISTENT_USER_ID = "5555";
    private static final String X_PAGINATION_PAGES = "X-Pagination-Pages";
    private static final String MODIFY = "Modify";
    private static final String DELETE = "Delete";

    /**
     * The client used to send requests to GoRest.
     *
     * @see GoRestClient
     */
    private final GoRestClient goRestClient;

    /**
     * Retrieve page 3 of the list of all users.
     * Sort the retrieved user list by name.
     * After sorting, log the name of the last user.
     * Update that user's name to a new value.
     * Use the correct http method to save the updated user.
     * Delete that user.
     * Attempt to retrieve a nonexistent user with ID 5555.
     * Log the resulting http response code.
     *
     * @return a response entity containing a message and http status code
     */
    public ResponseEntity<String> launchCodeChallenge() {
        try {
            final UserInfo user = getLastUserOnPage();
            modifyLastUser(user);
            deleteLastUser(user.getId());
            getNonExistentUser();
            return new ResponseEntity<>("Workflow complete", HttpStatus.OK);

        } catch (CustomException exception) {
            return new ResponseEntity<>(exception.getMessage(),
                    exception.getStatusCode());
        }
    }

    /**
     * Obtains page of users from the list users endpoint of the goRestClient.
     * Sorts user list by name, returns a record for the last user in the list.
     *
     * @return a single {@link UserInfo} object
     */
    public UserInfo getLastUserOnPage() {
        final String uri = BASE_URL + GO_REST_USERS + "?"
                + PAGE + "=" + PAGE_NUMBER;
        final ResponseEntity<String> response =
                goRestClient.processGetRequest(uri, ACCESS_TOKEN).block();

        if (response != null) {
            if (response.getStatusCode().is2xxSuccessful()) {
                final HttpHeaders headers = response.getHeaders();
                log.info("Total number of pages: {}",
                        headers.get(X_PAGINATION_PAGES));
                try {
                    return processUserListBody(response.getBody());
                } catch (IOException exception) {
                    throw new CustomException(exception.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                throw new CustomException("Get user request: failed",
                        response.getStatusCode());
            }
        } else {
            throw new CustomException("Get user request: failed",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Sorts user list by name.
     * Returns a record for the last user in the list
     *
     * @param body JSON string from the response body
     *
     * @return a single {@link UserInfo} object
     */
    private UserInfo processUserListBody(final String body)
            throws JsonProcessingException {
        //Read JSON string into a list of UserInfo objects
        final ObjectMapper mapper = new ObjectMapper();
        List<UserInfo> userInfoList;

        userInfoList = mapper.readValue(
                body,
                mapper.getTypeFactory()
                        .constructCollectionType(List.class,
                                UserInfo.class));

        //Sort list by UserInfo.name field
        userInfoList.sort((Comparator.comparing(UserInfo::getName)));

        //Identify last user record in list
        final UserInfo lastUserName = userInfoList.get(userInfoList.size() - 1);
        log.info("Name of last user in list: {}",
                lastUserName.getName());

        return lastUserName;
    }

    /**
     * Changes the name of a user record.
     * Persists the update through the goRestClient
     *
     * @param user takes in a single user record {@link UserInfo}
     */
    public void modifyLastUser(final UserInfo user) {
        user.setName(LEA_COOK);
        final String uri = BASE_URL + GO_REST_USERS + "/" + user.getId();
        final ResponseEntity<String> response =
                goRestClient.processPutRequest(uri, ACCESS_TOKEN, user).block();
        logResponse(response, MODIFY);
    }

    /**
     * Deletes a user record through the goRestClient.
     *
     * @param userId unique id of the user to delete
     */
    public void deleteLastUser(final String userId) {
        final String uri = BASE_URL + GO_REST_USERS + "/" + userId;
        final ResponseEntity<String> response =
                goRestClient.processDeleteRequest(uri, ACCESS_TOKEN).block();
        logResponse(response, DELETE);
    }

    /**
     * Check response from goRestClient.
     * Log success or throw error.
     *
     * @param response response entity to validate
     * @param requestType type of http request
     */
    private void logResponse(final ResponseEntity<String> response,
                             final String requestType) {
        final String failureMessage = requestType + " user request: failed";
        if (response != null) {
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("{} user request: success", requestType);
            } else {
                throw new CustomException(failureMessage,
                        response.getStatusCode());
            }
        } else {
            throw new CustomException(failureMessage,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Attempts to make a get request for a user record that does.
     * not exist, through the goRestClient.
     */
    public void getNonExistentUser() {
        final String getBadUserUri = BASE_URL + GO_REST_USERS
                + "/" + NONEXISTENT_USER_ID;
        try {
            goRestClient.processGetRequest(getBadUserUri, ACCESS_TOKEN).block();
        } catch (CustomException exception) {
            log.info("Status Code from goRest invalid get user: {}",
                    exception.getStatusCode());
        }
    }
}
