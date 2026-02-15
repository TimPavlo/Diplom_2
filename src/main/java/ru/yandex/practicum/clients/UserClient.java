package ru.yandex.practicum.clients;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.models.User;
import ru.yandex.practicum.config.ApiConfig;

public class UserClient extends BaseClient {

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return RestAssured.given(getBaseSpec())
                .body(user)
                .when()
                .post(ApiConfig.REGISTER_PATH)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return RestAssured.given(getBaseSpec())
                .body(user)
                .when()
                .post(ApiConfig.LOGIN_PATH)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return RestAssured.given(getAuthSpec(token))
                .when()
                .delete(ApiConfig.USER_PATH)
                .then();
    }

    @Step("Извлечение accessToken из ответа")
    public String extractAccessToken(ValidatableResponse response) {
        return response.extract().path("accessToken");
    }
}