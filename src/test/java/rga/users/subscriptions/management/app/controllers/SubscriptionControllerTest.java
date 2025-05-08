package rga.users.subscriptions.management.app.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import rga.users.subscriptions.management.app.dtos.AddSubscriptionDto;
import rga.users.subscriptions.management.app.dtos.ResponseMessageDto;
import rga.users.subscriptions.management.app.dtos.SubscriptionDto;
import rga.users.subscriptions.management.app.dtos.UserAuthResponseDto;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestPropertySource(locations = "/application-test.yml")
@DisplayName("Test SubscriptionController methods:")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubscriptionControllerTest {

    private final TestRestTemplate testRestTemplate;

    private final String ENDPOINT = "/rest/v1/users";

    private HttpHeaders adminHttpHeaders;
    private HttpHeaders userHttpHeaders;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        adminHttpHeaders = new HttpHeaders();
        adminHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final String adminEmail = "ivanov123@mail.ru";
        final String adminPassword = "passwordADMIN";
        prepareHttpHeaders(adminHttpHeaders, adminEmail, adminPassword);

        userHttpHeaders = new HttpHeaders();
        userHttpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final String userEmail = "pet89rov@mail.ru";
        final String userPassword = "passwordUSER";
        prepareHttpHeaders(userHttpHeaders, userEmail, userPassword);
    }

    private void prepareHttpHeaders(HttpHeaders httpHeaders, String email, String password) {
        var userJwt = getJwt(email, password);
        httpHeaders.setBearerAuth(userJwt);
    }

    private String getJwt(String email, String password) {
        final String AUTH_REQUEST = "{\"email\":\"%s\",\"password\":\"%s\"}".formatted(email, password);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> HTTP_ENTITY = new HttpEntity<>(AUTH_REQUEST, httpHeaders);

        final String AUTH_URL = "/rest/v1/security/auth";
        ResponseEntity<UserAuthResponseDto> authResponse = testRestTemplate.postForEntity(AUTH_URL, HTTP_ENTITY, UserAuthResponseDto.class);
        return Objects.requireNonNull(authResponse.getBody()).jwt();
    }

    @Test
    @Order(0)
    @DisplayName("Test access application")
    void accessApplication() {
        System.out.println(port);
    }

    @Test
    @Order(1)
    @DisplayName("Test context loads")
    public void contextLoads() {
    }

    @Test
    @Order(10)
    @DisplayName("Test add new subscription")
    void testAddNewSubscription(){
        AddSubscriptionDto dto = new AddSubscriptionDto(
                "New title",
                "New description",
                "ivanov123@mail.ru");
        HttpEntity<AddSubscriptionDto> httpEntity = new HttpEntity<>(dto, adminHttpHeaders);
        ResponseEntity<SubscriptionDto> response = testRestTemplate.postForEntity(ENDPOINT + "/1", httpEntity, SubscriptionDto.class);

        assertNotNull(response.getBody());
        assertEquals(4L, response.getBody().getId());
        assertEquals("New title", response.getBody().getTitle());
        assertEquals("New description", response.getBody().getDescription());
        assertSame(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    @Order(11)
    @DisplayName("Test add new subscription to another user")
    void testAddNewSubscriptionToAnotherUser(){
        AddSubscriptionDto dto = new AddSubscriptionDto(
                "New title",
                "New description",
                "pet89rov@mail.ru");
        HttpEntity<AddSubscriptionDto> httpEntity = new HttpEntity<>(dto, adminHttpHeaders);
        ResponseEntity<ResponseMessageDto> response = testRestTemplate.postForEntity(ENDPOINT + "/2", httpEntity, ResponseMessageDto.class);

        assertEquals("ResponseMessageDto[url=/rest/v1/users/2, message=Access is denied. Current user does not have permission to create subscription.]", Objects.requireNonNull(response.getBody()).toString());
        assertSame(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(12)
    @DisplayName("Test add new subscription by unauthenticated user")
    void testAddNewSubByUnauthenticatedUser() {
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(null);
        ResponseEntity<ResponseMessageDto> response = testRestTemplate.exchange(ENDPOINT + "/2", HttpMethod.GET, stringHttpEntity, ResponseMessageDto.class);

        assertNull(response.getBody());
        assertSame(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(20)
    @DisplayName("Test get subscriptions page (by user id) by user with role ROLE_ADMIN")
    void testGetSubsPageByUserIdByAdmin(){
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(adminHttpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(ENDPOINT + "/2/subscriptions", HttpMethod.GET, stringHttpEntity, String.class);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("[{\"id\":2,\"title\":\"Subscription 2\",\"description\":\"Description 2"));
        assertSame(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(22)
    @DisplayName("Test get subscriptions page (by another user id) by user with role ROLE_USER")
    void testGetSubsPageByAnotherUserIdByUser(){
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(userHttpHeaders);
        ResponseEntity<ResponseMessageDto> response = testRestTemplate.exchange(ENDPOINT + "/3/subscriptions", HttpMethod.GET, stringHttpEntity, ResponseMessageDto.class);

        assertEquals("ResponseMessageDto[url=/rest/v1/users/3/subscriptions, message=Access denied. Current user does not have the required permissions to get subscriptions page by user id]", Objects.requireNonNull(response.getBody()).toString());
        assertSame(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(21)
    @DisplayName("Test get own subscriptions page by user with role ROLE_USER")
    void testGetOwnSubsPageByUser(){
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(userHttpHeaders);
        ResponseEntity<String> response = testRestTemplate.exchange(ENDPOINT + "/2/subscriptions", HttpMethod.GET, stringHttpEntity, String.class);

        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("[{\"id\":2,\"title\":\"Subscription 2\",\"description\":\"Description 2"));
        assertSame(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(23)
    @DisplayName("Test get subscriptions page by unauthenticated user")
    void testGetSubsByUnauthenticatedUser() {
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(null);
        ResponseEntity<ResponseMessageDto> response = testRestTemplate.exchange(ENDPOINT + "/2/subscriptions", HttpMethod.GET, stringHttpEntity, ResponseMessageDto.class);

        assertNull(response.getBody());
        assertSame(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(24)
    @DisplayName("Test get subscriptions page (by nonexistent user id) by user with role ROLE_ADMIN")
    void testGetSubsPageByNonexistentUserIdByAdmin(){
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(adminHttpHeaders);
        ResponseEntity<ResponseMessageDto> response = testRestTemplate.exchange(ENDPOINT + "/33/subscriptions", HttpMethod.GET, stringHttpEntity, ResponseMessageDto.class);

        assertNotNull(response.getBody());
        assertEquals("ResponseMessageDto[url=/rest/v1/users/33/subscriptions, message=User with id 33 is not found]", response.getBody().toString());
        assertSame(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @Order(30)
    @DisplayName("Test delete subscription (by id) by not its subscriber")
    void testDeleteSubByNotItsSubscriber(){
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(userHttpHeaders);
        ResponseEntity<ResponseMessageDto> response = testRestTemplate.exchange(ENDPOINT + "/3/subscriptions/3", HttpMethod.DELETE, stringHttpEntity, ResponseMessageDto.class);

        assertNotNull(response.getBody());
        assertEquals("ResponseMessageDto[url=/rest/v1/users/3/subscriptions/3, message=Access denied. Current user does not have the required permissions to delete subscription]", Objects.requireNonNull(response.getBody()).toString());
        assertSame(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    @Order(31)
    @DisplayName("Test delete subscription (by id) by its subscriber")
    void testDeleteSubByItsSubscriber(){
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(userHttpHeaders);
        ResponseEntity<Void> response = testRestTemplate.exchange(ENDPOINT + "/2/subscriptions/2", HttpMethod.DELETE, stringHttpEntity, Void.class);

        assertNull(response.getBody());
        assertSame(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(32)
    @DisplayName("Test delete subscription by unauthenticated user")
    void testDeleteSubByUnauthenticatedUser() {
        HttpEntity<String> stringHttpEntity = new HttpEntity<>(null);
        ResponseEntity<ResponseMessageDto> response = testRestTemplate.exchange(ENDPOINT + "/3/subscriptions/3", HttpMethod.DELETE, stringHttpEntity, ResponseMessageDto.class);

        assertNull(response.getBody());
        assertSame(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

}
