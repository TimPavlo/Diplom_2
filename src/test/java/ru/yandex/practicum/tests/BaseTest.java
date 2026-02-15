package ru.yandex.practicum.tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import ru.yandex.practicum.config.ApiConfig;

public class BaseTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = ApiConfig.BASE_URL;
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
