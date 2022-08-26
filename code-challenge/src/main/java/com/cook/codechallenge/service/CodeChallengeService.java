package com.cook.codechallenge.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CodeChallengeService {

    public String processResponse(final ResponseEntity<String> response) {
        if (response == null) {
            return null;
        } else {
            return response.toString();
        }
    }
}
