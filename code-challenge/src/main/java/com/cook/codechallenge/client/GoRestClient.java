package com.cook.codechallenge.client;

import com.cook.codechallenge.domain.UserInfo;
import com.cook.codechallenge.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@Log4j2
public class GoRestClient {

    private final WebClient webClient;

    public GoRestClient(final WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Make a GET request to a URL with access token.
     *
     * @param requestUri for the endpoint
     * @param accessToken to add to request headers
     * @return the response
     */
    public Mono<ResponseEntity<String>> processGetRequest(
            final String requestUri, final String accessToken) {
        return webClient
                .get()
                .uri(requestUri)
                .headers(httpHeaders ->
                        httpHeaders.setAll(formatHeaderMap(accessToken)))
                .retrieve()
                .onStatus(HttpStatus::isError, response ->
                        response.toEntity(String.class)
                        .map(entity ->
                                new CustomException(
                                        "GoRest get request failed",
                                        response.statusCode())))
                .toEntity(String.class);

    }

    /**
     * Make a PUT request to a URL with access token.
     *
     * @param requestUri for the endpoint
     * @param accessToken to add to request headers
     * @param requestBody map of body keys and values
     * @return the response
     */
    public Mono<ResponseEntity<String>> processPutRequest(
            final String requestUri, final String accessToken,
            final UserInfo requestBody) {
        return webClient
                .put()
                .uri(requestUri)
                .headers(httpHeaders ->
                        httpHeaders.setAll(formatHeaderMap(accessToken)))
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatus::isError, response ->
                        response.toEntity(String.class)
                        .map(entity -> new CustomException(
                                "GoRest put request failed",
                                response.statusCode())))
                .onStatus(HttpStatus::is3xxRedirection, response ->
                        response.toEntity(String.class)
                        .map(entity -> new CustomException(
                                "GoRest put request failed",
                                response.statusCode())))
                .toEntity(String.class);
    }

    /**
     * Make a DELETE request to a URL with access token.
     *
     * @param requestUri for the endpoint
     * @param accessToken to add to request headers
     * @return the response
     */
    public Mono<ResponseEntity<String>> processDeleteRequest(
            final String requestUri, final String accessToken) {
        return webClient
                .delete()
                .uri(requestUri)
                .headers(httpHeaders ->
                        httpHeaders.setAll(formatHeaderMap(accessToken)))
                .retrieve()
                .onStatus(HttpStatus::isError, response ->
                        response.toEntity(String.class)
                        .map(entity -> new CustomException(
                                "GoRest delete request failed",
                                response.statusCode())))
                .onStatus(HttpStatus::is3xxRedirection, response ->
                        response.toEntity(String.class)
                        .map(entity -> new CustomException(
                                "GoRest delete request failed",
                                response.statusCode())))
                .toEntity(String.class);
    }

    /**
     * Format header info to be used for http requests.
     *
     * @param accessToken to add to request headers
     * @return a map containing http header info
     */
    private Map<String, String> formatHeaderMap(final String accessToken) {
        HttpHeaders headersMap = new HttpHeaders();
        headersMap.add(HttpHeaders.ACCEPT,
                MediaType.APPLICATION_JSON.toString());
        headersMap.add(HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON.toString());
        headersMap.add(HttpHeaders.AUTHORIZATION,
                "Bearer " + accessToken);
        return headersMap.toSingleValueMap();
    }

}
