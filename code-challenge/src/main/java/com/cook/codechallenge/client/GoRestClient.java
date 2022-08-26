package com.cook.codechallenge.client;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GoRestClient {

    private final WebClient webClient;

    public GoRestClient() {
        this.webClient = WebClient.create();
    }

    /**
     * Make a GET request to a URL with an access token
     * @param requestUri for the endpoint
     * @param accessToken to add to request headers
     * @return the response
     */
    public Mono<ResponseEntity<String>> processGetRequest(final String requestUri, final String accessToken) {
        return webClient
                .get()
                .uri(requestUri)
                .headers(httpHeaders -> httpHeaders.setAll(formatHeaderMap(accessToken)))
                .retrieve()
                .toEntity(String.class);
    }

    /**
     * Make a PUT request to a URL with an access token
     * @param requestUri for the endpoint
     * @param accessToken to add to request headers
     * @param requestBody map of body keys and values
     * @return the response
     */
    public Mono<ResponseEntity<String>> processPutRequest(final String requestUri, final String accessToken,
                                                          final MultiValueMap<String,String> requestBody) {
        return webClient
                .put()
                .uri(requestUri)
                .headers(httpHeaders -> httpHeaders.setAll(formatHeaderMap(accessToken)))
                .body(BodyInserters.fromFormData(requestBody))
                .retrieve()
                .toEntity(String.class);
    }

    /**
     * Make a DELETE request to a URL with an access token
     * @param requestUri for the endpoint
     * @param accessToken to add to request headers
     * @return the response
     */
    public Mono<ResponseEntity<String>> processDeleteRequest(final String requestUri, final String accessToken) {
        return webClient
                .delete()
                .uri(requestUri)
                .headers(httpHeaders -> httpHeaders.setAll(formatHeaderMap(accessToken)))
                .retrieve()
                .toEntity(String.class);
    }

    private Map<String, String> formatHeaderMap(final String accessToken) {
        HttpHeaders headersMap = new HttpHeaders();
        headersMap.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
        headersMap.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        headersMap.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        return headersMap.toSingleValueMap();
    }

}
