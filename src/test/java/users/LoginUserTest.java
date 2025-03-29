package users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.yandex.model.User;
import ru.praktikum.yandex.model.UserCreateResponse;
import steps.UserSteps;

public class LoginUserTest {
    private User user;
    private UserSteps userSteps;
    private UserCreateResponse userCreateResponse = new UserCreateResponse();

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = new User("test1-maraa@yandex.ru", "qwerty12345", "Kuvaldaa");
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginExistingUserTest() {
        Response response = userSteps.loginUser(user);
        userCreateResponse = response.as(UserCreateResponse.class);
        Assert.assertEquals("Статус код при авторизации пользователя должен быть 200", 200, response.getStatusCode());
        Assert.assertTrue("Поле success при авторизации пользователя должно быть true", userCreateResponse.isSuccess());
        Assert.assertNotNull("accessToken при авторизации пользователя не может быть пустым", userCreateResponse.getAccessToken());
        Assert.assertNotNull("refreshToken при авторизации пользователя не может быть пустым", userCreateResponse.getRefreshToken());
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginWithIncorrectLoginAndPasswordTest() {
        user = new User("test-maraa@yandex.ru", "qwerty12345", "Kuvaldaa");
        Response response = userSteps.loginUser(user);
        userCreateResponse = response.as(UserCreateResponse.class);
        Assert.assertEquals("Статус код при авторизации пользователя с невалидным логопассом должен быть 401", 401, response.getStatusCode());
        Assert.assertFalse("Поле success при авторизации пользователя должно быть false", userCreateResponse.isSuccess());
        Assert.assertEquals("Некорректный текст в message", "email or password are incorrect", userCreateResponse.getMessage());
    }
}
