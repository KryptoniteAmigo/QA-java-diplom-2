package ru.praktikum.yandex.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class ApiSpec {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    private static final RequestSpecification BASE_SPEC = new RequestSpecBuilder()
            .setBaseUri(BASE_URL)
            .setContentType("application/json")
            .build();

    public static RequestSpecification getBaseSpec() {
        return BASE_SPEC;
    }

    private ApiSpec() {}
}
