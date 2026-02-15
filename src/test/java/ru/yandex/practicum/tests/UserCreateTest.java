package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
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

public class UserCreateTest extends BaseTest {
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
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void testCreateUniqueUser() {
        ValidatableResponse response = userClient.createUser(user);
        accessToken = userClient.extractAccessToken(response);
        response.statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    public void testCreateDuplicateUser() {
        userClient.createUser(user);
        ValidatableResponse response = userClient.createUser(user);
        response.statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    public void testCreateUserWithoutEmail() {
        user.setEmail(null);
        ValidatableResponse response = userClient.createUser(user);
        response.statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void testCreateUserWithoutPassword() {
        user.setPassword(null);
        ValidatableResponse response = userClient.createUser(user);
        response.statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void testCreateUserWithoutName() {
        user.setName(null);
        ValidatableResponse response = userClient.createUser(user);
        response.statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }
}