package ru.praktikum.yandex.client;

import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.yandex.config.ApiSpec;
import ru.praktikum.yandex.model.User;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String USER_PATH = "/api/auth/user";
    private static final String LOGIN_PATH = "/api/auth/login";

    private final Gson gson = new Gson();

    @Step("Создаём пользователя {user}")
    public Response createUser(User user) {
        String json = gson.toJson(user);
        return given()
                .spec(ApiSpec.getBaseSpec())
                .body(json)
                .when()
                .post(REGISTER_PATH);
    }

    @Step("Логиним пользователя {user}")
    public Response loginUser(User user) {
        String json = gson.toJson(user);
        return given()
                .spec(ApiSpec.getBaseSpec())
                .body(json)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Обновляем данные пользователя {user} с токеном {token}")
    public Response updateUser(User user, String accessToken) {
        String json = gson.toJson(user);
        return given()
                .spec(ApiSpec.getBaseSpec())
                .header("Authorization", accessToken)
                .body(json)
                .when()
                .patch(USER_PATH);
    }

    @Step("Удаляем пользователя c токеном {token}")
    public void deleteUser(String accessToken) {
        given()
                .spec(ApiSpec.getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH);
    }
}
