package com.cook.codechallenge.resource;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cook.codechallenge.client.GoRestClient;
import com.cook.codechallenge.service.CodeChallengeService;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/")
public class CodeChallengeResource {

    private static final String GO_REST_USERS = "/public/v2/users/";
    private static final String ACCESS_TOKEN = "cf08271a4489bba9b6b21ff39fb31bf573b66bcb0c0638ac6043f08dd40370f2";

    /**
     * The client used to send requests to GoRest
     *
     * @see GoRestClient
     */
    private final GoRestClient goRestClient;

    /**
     * The service instance used to provide support for the resource
     * @see CodeChallengeService
     */
    private final CodeChallengeService codeChallengeService;

    @GetMapping(path = "/getChallenge", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getFanaticsCodeChallenge() {


        ResponseEntity<String> response = goRestClient.processGetRequest(GO_REST_USERS, ACCESS_TOKEN).block();
        return codeChallengeService.processResponse(response);
    }
}
