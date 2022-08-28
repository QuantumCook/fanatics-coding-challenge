package com.cook.codechallenge.client;

import com.cook.codechallenge.domain.UserInfo;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GoRestClientTest {

    private static final String RESPONSE = "response";
    private static final String URI = "TestUri";
    private static final String ACCESS_TOKEN = "accessToken";

    private static final UserInfo USER_INFO =
            new UserInfo("123", "Bet Fanatics",
                    "betFan@gmail.com", "male", "active");
    @Test
    void testGetRequest(){
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
    void testPutRequest(){
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
    void testDeleteRequest(){
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(clientRequest -> Mono.just(ClientResponse.create(HttpStatus.NO_CONTENT)
                        .build())
                );
        GoRestClient goRestClient = new GoRestClient(webClientBuilder);
        ResponseEntity<String> response = goRestClient.processDeleteRequest(URI, ACCESS_TOKEN).block();
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
