package users;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.yandex.model.User;
import ru.praktikum.yandex.model.UserCreateResponse;
import steps.UserSteps;

@RunWith(Parameterized.class)
public class CreateUserParameterizedTest {
    private UserSteps userSteps;
    private UserCreateResponse userCreateResponse = new UserCreateResponse();

    @Parameterized.Parameter()
    public User user;

    @Parameterized.Parameter(1)
    public String missingFieldDescription;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
    }

    @Parameterized.Parameters(name = "Пропущенное поле: {1}")
    public static Object[][] getUserData() {
        return new Object[][]{
                {new User(null, "qwerty54321", "John"), "Отсутствует email"},
                {new User("some@yandex.ru", null, "John"), "Отсутствует password"},
                {new User("some@yandex.ru", "qwerty54321", null), "Отсутствует name"}
        };
    }

    @Test
    @DisplayName("Создание пользователя с отсутствующим параметром")
    public void createUserWithoutRequiredFieldsTest() {
        Response response = userSteps.createUser(user);
        userCreateResponse = response.as(UserCreateResponse.class);
        Assert.assertEquals("При создании пользователя без обязательного поля статус код должен быть 403", 403, response.getStatusCode());
        Assert.assertFalse("Поле success при создании ранее зарегистрированного пользователя должно быть false", userCreateResponse.isSuccess());
        Assert.assertEquals("Некорректный текст в message", "Email, password and name are required fields", userCreateResponse.getMessage());
    }

    @After
    public void tearDown() {
        if (userCreateResponse != null && userCreateResponse.getAccessToken() != null) {
            userSteps.deleteUser(userCreateResponse.getAccessToken());
        }
    }
}
