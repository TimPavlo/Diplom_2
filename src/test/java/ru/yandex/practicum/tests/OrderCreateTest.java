package ru.yandex.practicum.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.clients.OrderClient;
import ru.yandex.practicum.clients.UserClient;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.models.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;

public class OrderCreateTest extends BaseTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private User user;
    private String accessToken;
    private String[] validIngredientIds;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = new User();
        String randomEmail = RandomStringUtils.randomAlphanumeric(8) + "@test.com";
        user.setEmail(randomEmail)
                .setPassword(RandomStringUtils.randomAlphanumeric(10))
                .setName(RandomStringUtils.randomAlphanumeric(8));
        ValidatableResponse createResponse = userClient.createUser(user);
        accessToken = userClient.extractAccessToken(createResponse);
        user.setAccessToken(accessToken);
        validIngredientIds = orderClient.getValidIngredientIds();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void testCreateOrderWithAuth() {
        List<String> ingredients = Arrays.asList(validIngredientIds[0], validIngredientIds[1]);
        Order order = new Order(ingredients);
        ValidatableResponse response = orderClient.createOrder(order, accessToken);
        response.statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        List<String> ingredients = Arrays.asList(validIngredientIds[0], validIngredientIds[1]);
        Order order = new Order(ingredients);
        ValidatableResponse response = orderClient.createOrder(order, null);
        response.statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void testCreateOrderWithoutIngredients() {
        Order order = new Order(new ArrayList<>());
        ValidatableResponse response = orderClient.createOrder(order, accessToken);
        response.statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем")
    public void testCreateOrderWithInvalidHash() {
        List<String> ingredients = Arrays.asList("invalid_hash_123", "invalid_hash_456");
        Order order = new Order(ingredients);
        ValidatableResponse response = orderClient.createOrder(order, accessToken);
        response.statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}