package ru.praktikum.yandex.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.yandex.config.ApiSpec;

import static io.restassured.RestAssured.given;

public class IngredientClient {
    public static final String INGREDIENTS_PATH = "/api/ingredients";

    @Step("Получить список всех ингредиентов")
    public Response getIngredients() {
        return given()
                .spec(ApiSpec.getBaseSpec())
                .when()
                .get(INGREDIENTS_PATH);
    }
}
