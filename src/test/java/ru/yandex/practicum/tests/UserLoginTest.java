package ru.yandex.practicum.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.clients.UserClient;
import ru.yandex.practicum.models.User;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class UserLoginTest extends BaseTest {
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = new User();
        String randomEmail = RandomStringUtils.randomAlphanumeric(8) + "@test.com";
        user.setEmail(randomEmail)
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .setName(RandomStringUtils.randomAlphanumeric(8));
        ValidatableResponse response = userClient.createUser(user);
        accessToken = userClient.extractAccessToken(response);
        user.setAccessToken(accessToken);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Авторизация под существующим пользователем")
    public void testLoginExistingUser() {
        ValidatableResponse response = userClient.loginUser(user);
        response.statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация с неверным email")
    public void testLoginWithWrongEmail() {
        user.setEmail("wrong_" + user.getEmail());
        ValidatableResponse response = userClient.loginUser(user);
        response.statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void testLoginWithWrongPassword() {
        user.setPassword("wrong" + user.getPassword());
        ValidatableResponse response = userClient.loginUser(user);
        response.statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }
}