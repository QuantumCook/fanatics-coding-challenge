package com.cook.codechallenge.service;

import com.cook.codechallenge.client.GoRestClient;
import com.cook.codechallenge.domain.UserInfo;
import com.cook.codechallenge.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

public class CodeChallengeServiceTest {

    final private static String BODY = "[{\"id\":2409,\"name\":\"Anunay Gupta\"," +
            "\"email\":\"gupta_anunay@lemke.info\",\"gender\":\"female\"," +
            "\"status\":\"inactive\"},{\"id\":2399,\"name\":\"Triloki Nath Pillai DVM\"," +
            "\"email\":\"pillai_triloki_dvm_nath@connelly-streich.io\"," +
            "\"gender\":\"female\",\"status\":\"active\"}," +
            "{\"id\":2397,\"name\":\"Mr. Menka Banerjee\"," +
            "\"email\":\"mr_menka_banerjee@bartoletti-powlowski.name\"," +
            "\"gender\":\"male\",\"status\":\"inactive\"}," +
            "{\"id\":2395,\"name\":\"Mr. Nawal Bhattathiri\"," +
            "\"email\":\"mr_bhattathiri_nawal@reilly.net\",\"gender\":\"male\"," +
            "\"status\":\"inactive\"},{\"id\":2394,\"name\":\"Bhudev Dwivedi\"," +
            "\"email\":\"dwivedi_bhudev@stroman-hoppe.co\",\"gender\":\"male\"," +
            "\"status\":\"active\"},{\"id\":2393,\"name\":\"Navin Saini\"," +
            "\"email\":\"navin_saini@macejkovic.com\",\"gender\":\"male\"," +
            "\"status\":\"inactive\"},{\"id\":2390,\"name\":\"Anila Ahluwalia\"" +
            ",\"email\":\"anila_ahluwalia@barrows-herman.io\",\"gender\":\"male\"," +
            "\"status\":\"inactive\"},{\"id\":2389,\"name\":\"Anunay Jha\"," +
            "\"email\":\"anunay_jha@schowalter.io\",\"gender\":\"female\"," +
            "\"status\":\"active\"},{\"id\":2388,\"name\":\"Sharmila Gupta DC\"," +
            "\"email\":\"gupta_sharmila_dc@schiller.org\",\"gender\":\"male\"," +
            "\"status\":\"inactive\"},{\"id\":2387,\"name\":\"Rajendra Malik\"," +
            "\"email\":\"malik_rajendra@auer.name\",\"gender\":\"female\",\"status\":\"active\"}]";
    private static final UserInfo USER_INFO =
            new UserInfo("123", "Bet Fanatics",
                    "betFan@gmail.com", "male", "active");
    @Test
    void testLaunchCodeChallenge_Success() {
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        headersMap.add("X-Pagination-Pages", "45");
        ResponseEntity<String> mockListUserResponse = new ResponseEntity<>(BODY, headersMap, HttpStatus.OK);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString()))
                .thenReturn(Mono.just(mockListUserResponse));

        ResponseEntity<String> mockModifyUserResponse = new ResponseEntity<>(HttpStatus.OK);
        when(goRestClientMock.processPutRequest(anyString(), anyString(), any()))
                .thenReturn(Mono.just(mockModifyUserResponse));

        ResponseEntity<String> mockDeleteUserResponse = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(goRestClientMock.processDeleteRequest(anyString(), anyString()))
                .thenReturn(Mono.just(mockDeleteUserResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        ResponseEntity<String> response = codeChallengeService.launchCodeChallenge();
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo("Workflow complete");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testLaunchCodeChallenge_Error() {
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        headersMap.add("X-Pagination-Pages", "45");
        ResponseEntity<String> mockListUserResponse = new ResponseEntity<>(BODY, headersMap, HttpStatus.OK);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString()))
                .thenReturn(Mono.just(mockListUserResponse));

        ResponseEntity<String> mockModifyUserResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(goRestClientMock.processPutRequest(anyString(), anyString(), any()))
                .thenReturn(Mono.just(mockModifyUserResponse));

        ResponseEntity<String> mockDeleteUserResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(goRestClientMock.processDeleteRequest(anyString(), anyString()))
                .thenReturn(Mono.just(mockDeleteUserResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        ResponseEntity<String> response = codeChallengeService.launchCodeChallenge();
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo("Modify user request: failed");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetLastUserOnPage_Success() {
        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        headersMap.add("X-Pagination-Pages", "45");
        ResponseEntity<String> mockResponse = new ResponseEntity<>(BODY, headersMap, HttpStatus.OK);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        UserInfo user = codeChallengeService.getLastUserOnPage();
        assertThat(user.getName()).isEqualTo("Triloki Nath Pillai DVM");
    }

    @Test
    void testGetLastUserOnPage_NullResponse() {
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString())).thenReturn(Mono.empty());

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        CustomException thrown = catchThrowableOfType(codeChallengeService::getLastUserOnPage, CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testGetLastUserOnPage_BadResponse() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(BODY,HttpStatus.NOT_FOUND);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        CustomException thrown = catchThrowableOfType(codeChallengeService::getLastUserOnPage, CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetLastUserOnPage_ResponseParsingThrowsError() {
        String body = "[{id:2409, ame:\"Anunay Gupta\", email :\"gupta_anunay@lemke.info\", " +
                "gender:\"female\",\"status\":\"inactive\"}]";
        ResponseEntity<String> mockResponse = new ResponseEntity<>(body,HttpStatus.OK);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processGetRequest(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        CustomException thrown = catchThrowableOfType(codeChallengeService::getLastUserOnPage, CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
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
    void testModifyLastUser_BadResponse() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processPutRequest(anyString(), anyString(), any())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        CustomException thrown = catchThrowableOfType(() ->
                codeChallengeService.modifyLastUser(USER_INFO), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.NOT_MODIFIED);
    }

    @Test
    void testModifyLastUserOnPage_NullResponse() {
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processPutRequest(anyString(), anyString(), any())).thenReturn(Mono.empty());

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        CustomException thrown = catchThrowableOfType(() ->
                codeChallengeService.modifyLastUser(USER_INFO), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
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
    void testDeleteLastUser_BadResponse() {
        ResponseEntity<String> mockResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processDeleteRequest(anyString(), anyString())).thenReturn(Mono.just(mockResponse));

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        CustomException thrown = catchThrowableOfType(() ->
                codeChallengeService.deleteLastUser(USER_INFO.getId()), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testDeleteLastUserOnPage_NullResponse() {
        GoRestClient goRestClientMock = Mockito.mock(GoRestClient.class);
        when(goRestClientMock.processDeleteRequest(anyString(), anyString())).thenReturn(Mono.empty());

        CodeChallengeService codeChallengeService = new CodeChallengeService(goRestClientMock);

        CustomException thrown = catchThrowableOfType(() ->
                codeChallengeService.deleteLastUser(USER_INFO.getId()), CustomException.class);
        Assertions.assertThat(thrown.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
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
