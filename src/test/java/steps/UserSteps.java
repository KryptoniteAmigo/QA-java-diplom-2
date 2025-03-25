package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.yandex.client.UserClient;
import ru.praktikum.yandex.model.User;

public class UserSteps {
    private final UserClient userClient = new UserClient();

    @Step("Создать нового пользователя")
    public Response createUser(User user) {
        return userClient.createUser(user);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(User user) {
        return userClient.loginUser(user);
    }

    @Step("Обновление данных пользователя")
    public Response updateUser(User user, String accessToken) {
        return userClient.updateUser(user, accessToken);
    }

    @Step("Удалить пользователя")
    public void deleteUser(String accessToken) {
        userClient.deleteUser(accessToken);
    }
}
