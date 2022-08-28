package com.cook.codechallenge.service;

import com.cook.codechallenge.client.GoRestClient;
import com.cook.codechallenge.domain.UserInfo;
import com.cook.codechallenge.exception.CustomException;
import org.apache.coyote.Response;
import org.assertj.core.api.Assertions;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@SpringBootTest
public class CodeChallengeServiceTest {

    final private static String body = "[{\"id\":2409,\"name\":\"Anunay Gupta\",\"email\":\"gupta_anunay@lemke.info\",\"gender\":\"female\",\"status\":\"inactive\"},{\"id\":2399,\"name\":\"Triloki Nath Pillai DVM\",\"email\":\"pillai_triloki_dvm_nath@connelly-streich.io\",\"gender\":\"female\",\"status\":\"active\"},{\"id\":2397,\"name\":\"Mr. Menka Banerjee\",\"email\":\"mr_menka_banerjee@bartoletti-powlowski.name\",\"gender\":\"male\",\"status\":\"inactive\"},{\"id\":2395,\"name\":\"Mr. Nawal Bhattathiri\",\"email\":\"mr_bhattathiri_nawal@reilly.net\",\"gender\":\"male\",\"status\":\"inactive\"},{\"id\":2394,\"name\":\"Bhudev Dwivedi\",\"email\":\"dwivedi_bhudev@stroman-hoppe.co\",\"gender\":\"male\",\"status\":\"active\"},{\"id\":2393,\"name\":\"Navin Saini\",\"email\":\"navin_saini@macejkovic.com\",\"gender\":\"male\",\"status\":\"inactive\"},{\"id\":2390,\"name\":\"Anila Ahluwalia\",\"email\":\"anila_ahluwalia@barrows-herman.io\",\"gender\":\"male\",\"status\":\"inactive\"},{\"id\":2389,\"name\":\"Anunay Jha\",\"email\":\"anunay_jha@schowalter.io\",\"gender\":\"female\",\"status\":\"active\"},{\"id\":2388,\"name\":\"Sharmila Gupta DC\",\"email\":\"gupta_sharmila_dc@schiller.org\",\"gender\":\"male\",\"status\":\"inactive\"},{\"id\":2387,\"name\":\"Rajendra Malik\",\"email\":\"malik_rajendra@auer.name\",\"gender\":\"female\",\"status\":\"active\"}]";
    private static final UserInfo USER_INFO =
            new UserInfo("123", "Bet Fanatics",
                    "betFan@gmail.com", "male", "active");
    @Test
    void testGetLastUserOnPage() {
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        headersMap.add("X-Pagination-Pages", "45");
        ResponseEntity<String> mockResponse = new ResponseEntity<>(body, headersMap, HttpStatus.OK);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        UserInfo user = codeChallengeService.getLastUserOnPage();
        assertThat(user.getName()).isEqualTo("Triloki Nath Pillai DVM");
    }

    @Test
    void testModifyLastUser() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.OK);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processPutRequest(anyString(), anyString(), any())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        codeChallengeService.modifyLastUser(USER_INFO);
        verify(goRestClientMock, times(1)).processPutRequest(anyString(), anyString(), any());
    }

    @Test
    void testDeleteLastUser() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processDeleteRequest(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        codeChallengeService.deleteLastUser(USER_INFO.getId());
        verify(goRestClientMock, times(1)).processDeleteRequest(anyString(), anyString());
    }

    @Test
    void testGetNonExistentUser() {
        String body = "{\n" +
                "    \"message\": \"Resource not found\"\n" +
                "}";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);
        codeChallengeService.getNonExistentUser();
        verify(goRestClientMock, times(1)).processGetRequest(anyString(), anyString());
    }
}
