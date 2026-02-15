package ru.yandex.practicum.clients;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.Order;
import ru.yandex.practicum.config.ApiConfig;
import java.util.List;

public class OrderClient extends BaseClient {

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String token) {
        if (token == null) {
            return RestAssured.given(getBaseSpec())
                    .body(order)
                    .when()
                    .post(ApiConfig.ORDERS_PATH)
                    .then();
        }
        return RestAssured.given(getAuthSpec(token))
                .body(order)
                .when()
                .post(ApiConfig.ORDERS_PATH)
                .then();
    }

    @Step("Получение ингредиентов")
    public ValidatableResponse getIngredients() {
        return RestAssured.given(getBaseSpec())
                .when()
                .get(ApiConfig.INGREDIENTS_PATH)
                .then();
    }

    @Step("Получение валидных ID ингредиентов")
    public String[] getValidIngredientIds() {
        ValidatableResponse response = getIngredients();
        List<String> ids = response.extract().path("data._id");
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("API не вернуло ингредиенты");
        }
        return ids.toArray(new String[0]);
    }
}