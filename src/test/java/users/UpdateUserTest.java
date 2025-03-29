package users;

import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.yandex.model.User;
import ru.praktikum.yandex.model.UserCreateResponse;
import steps.UserSteps;

public class UpdateUserTest {
    private User user;
    private UserSteps userSteps;
    private UserCreateResponse userCreateResponse = new UserCreateResponse();

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        user = new User("testtest-mara@yandex.ru", "qwerty12345", "Kuvalda");
        Response response = userSteps.createUser(user);
        userSteps.loginUser(user);
        userCreateResponse = response.as(UserCreateResponse.class);

        Assert.assertEquals("Статус код при авторизации пользователя должен быть 200", 200, response.getStatusCode());
        Assert.assertTrue("Поле success при авторизации пользователя должно быть true", userCreateResponse.isSuccess());
        Assert.assertNotNull("accessToken при авторизации пользователя не может быть пустым", userCreateResponse.getAccessToken());
        Assert.assertNotNull("refreshToken при авторизации пользователя не может быть пустым", userCreateResponse.getRefreshToken());
    }

    @Test
    public void updateUserInfoWithLoginTest() {
        String token = userCreateResponse.getAccessToken();
        User updatedData = new User("update-mara@yandex.ru", "qwerty54321", "Kuvalda1");

        Response updateResponse = userSteps.updateUser(updatedData, token);
        UserCreateResponse updateUserResponse = updateResponse.as(UserCreateResponse.class);

        Assert.assertEquals("Статус код при обновлении пользователя должен быть 200", 200, updateResponse.getStatusCode());
        Assert.assertTrue("Поле success при обновлении пользователя должно быть true", updateUserResponse.isSuccess());
        Assert.assertEquals("Неверный email после обновления", "update-mara@yandex.ru", updateUserResponse.getUser().getEmail());
        Assert.assertEquals("Неверное имя после обновления", "Kuvalda1", updateUserResponse.getUser().getName());
    }

    @Test
    public void updateUserInfoWithoutLoginTest() {
        String noToken = "";
        User updatedData = new User("someEmail@yandex.ru", "somePassword", "SomeName");

        Response updateResponse = userSteps.updateUser(updatedData, noToken);
        UserCreateResponse updateUserResponse = updateResponse.as(UserCreateResponse.class);

        Assert.assertEquals("Ожидаемый код при отсутствии авторизации - 401", 401, updateResponse.getStatusCode());
        Assert.assertFalse("Поле success должно быть false", updateUserResponse.isSuccess());
        Assert.assertEquals("Некорректное сообщение об ошибке", "You should be authorised", updateUserResponse.getMessage());
    }

    @After
    public void tearDown() {
        if (userCreateResponse != null && userCreateResponse.getAccessToken() != null) {
            userSteps.deleteUser(userCreateResponse.getAccessToken());
        }
    }
}
