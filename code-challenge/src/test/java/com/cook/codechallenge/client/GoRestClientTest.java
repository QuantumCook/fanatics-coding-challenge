package com.cook.codechallenge.client;

import com.cook.codechallenge.domain.UserInfo;
import com.cook.codechallenge.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

public class GoRestClientTest {

    private static final String RESPONSE = "response";
    private static final String URI = "TestUri";
    private static final String ACCESS_TOKEN = "accessToken";

    private static final UserInfo USER_INFO =
            new UserInfo("123", "Bet Fanatics",
                    "betFan@gmail.com", "male", "active");
    @Test
    void testGetRequest_Success() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .body(RESPONSE)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        ResponseEntity<String> response = goRestClient.processGetRequest(URI, ACCESS_TOKEN).block();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testGetRequest_BadResponse() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND)
                        .body(RESPONSE)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        CustomException thrown = catchThrowableOfType(() -> goRestClient.processGetRequest(URI, ACCESS_TOKEN).block(), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testPutRequest_Success() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.OK)
                        .body(RESPONSE)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        ResponseEntity<String> response =
                goRestClient.processPutRequest(URI, ACCESS_TOKEN, USER_INFO).block();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testPutRequest_BadRequest() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(RESPONSE)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        CustomException thrown = catchThrowableOfType(() -> goRestClient.processPutRequest(URI, ACCESS_TOKEN, USER_INFO).block(), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testPutRequest_Redirect() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.NOT_MODIFIED)
                        .body(RESPONSE)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        CustomException thrown = catchThrowableOfType(() -> goRestClient.processPutRequest(URI, ACCESS_TOKEN, USER_INFO).block(), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
    }

    @Test
    void testDeleteRequest_Success() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.NO_CONTENT)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        ResponseEntity<String> response = goRestClient.processDeleteRequest(URI, ACCESS_TOKEN).block();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testDeleteRequest_BadRequest() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.FORBIDDEN)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        CustomException thrown = catchThrowableOfType(() -> goRestClient.processDeleteRequest(URI, ACCESS_TOKEN).block(), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void testDeleteRequest_Redirect() {
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.NOT_MODIFIED)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        CustomException thrown = catchThrowableOfType(() -> goRestClient.processDeleteRequest(URI, ACCESS_TOKEN).block(), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
    }
}
