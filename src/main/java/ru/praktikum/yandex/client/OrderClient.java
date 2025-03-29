package ru.praktikum.yandex.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.yandex.config.ApiSpec;
import ru.praktikum.yandex.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String CREATE_ORDER_PATH = "/api/orders";
    private static final String GET_USER_ORDERS_PATH = "/api/orders";

    @Step("Создать заказ")
    public Response createOrder(Order order, String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return given()
                    .spec(ApiSpec.getBaseSpec())
                    .body(order)
                    .when()
                    .post(CREATE_ORDER_PATH);
        } else {
            return given()
                    .spec(ApiSpec.getBaseSpec())
                    .header("Authorization", accessToken)
                    .body(order)
                    .when()
                    .post(CREATE_ORDER_PATH);
        }
    }

    @Step("Получить заказы конкретного пользователя")
    public Response getUserOrders(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            return given()
                    .spec(ApiSpec.getBaseSpec())
                    .when()
                    .get(GET_USER_ORDERS_PATH);
        } else {
            return given()
                    .spec(ApiSpec.getBaseSpec())
                    .header("Authorization", accessToken)
                    .when()
                    .get(GET_USER_ORDERS_PATH);
        }
    }


}
