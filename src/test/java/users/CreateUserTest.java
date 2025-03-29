package users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.yandex.model.User;
import ru.praktikum.yandex.model.UserCreateResponse;
import steps.UserSteps;

public class CreateUserTest {
    private User user;
    private UserSteps userSteps;
    private UserCreateResponse userCreateResponse = new UserCreateResponse();

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = new User("polzak5-unique@yandex.ru", "qwerty54321", "John");
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest() {
        Response response = userSteps.createUser(user);
        userCreateResponse = response.as(UserCreateResponse.class);
        Assert.assertEquals("Статус код при создании пользователя должен быть 200", 200, response.getStatusCode());
        Assert.assertTrue("Поле success при создании пользователя должно быть true", userCreateResponse.isSuccess());
        Assert.assertNotNull("accessToken при создании пользователя не может быть пустым", userCreateResponse.getAccessToken());
        Assert.assertNotNull("refreshToken при создании пользователя не может быть пустым", userCreateResponse.getRefreshToken());


    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    public void createExistingUserTest() {
        Response response = userSteps.createUser(user);
        userCreateResponse = response.as(UserCreateResponse.class);
        Assert.assertEquals("Статус код при создании пользователя должен быть 200", 200, response.getStatusCode());
        Assert.assertTrue("Поле success при создании пользователя должно быть true", userCreateResponse.isSuccess());
        Assert.assertNotNull("accessToken при создании пользователя не может быть пустым", userCreateResponse.getAccessToken());
        Assert.assertNotNull("refreshToken при создании пользователя не может быть пустым", userCreateResponse.getRefreshToken());

        Response secondResponse = userSteps.createUser(user);
        UserCreateResponse userCreateSecondResponse = secondResponse.as(UserCreateResponse.class);
        Assert.assertEquals("При создании существующего пользователя статус код должен быть 403", 403, secondResponse.getStatusCode());
        Assert.assertFalse("Поле success при создании ранее зарегистрированного пользователя должно быть false", userCreateSecondResponse.isSuccess());
        Assert.assertEquals("Некорректный текст в message", "User already exists", userCreateSecondResponse.getMessage());
    }

    @After
    public void tearDown() {
        if (userCreateResponse != null && userCreateResponse.getAccessToken() != null) {
            userSteps.deleteUser(userCreateResponse.getAccessToken());
        }
    }
}
