package orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import ru.praktikum.yandex.model.Order;
import ru.praktikum.yandex.model.OrderResponse;
import ru.praktikum.yandex.model.User;
import ru.praktikum.yandex.model.UserCreateResponse;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.Arrays;
import java.util.Collections;


public class CreateOrderTest {

    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private UserCreateResponse userCreateResponse;

    private final String VALID_INGREDIENT_1 = "61c0c5a71d1f82001bdaaa6d";
    private final String VALID_INGREDIENT_2 = "61c0c5a71d1f82001bdaaa6f";
    private final String INVALID_INGREDIENT = "123invalid456hash789";

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();

        User user = new User("someuser987@yandex.ru", "qwerty123", "someuser987");
        Response response = userSteps.createUser(user);
        userCreateResponse = response.as(UserCreateResponse.class);

        Assert.assertEquals("Код ответа должен быть 200", 200, response.getStatusCode());
        Assert.assertTrue("success должно быть true", userCreateResponse.isSuccess());
        Assert.assertNotNull("Токен не должен быть null", userCreateResponse.getAccessToken());
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и валидными ингредиентами")
    public void createOrderWithAuthAndValidIngredientsTest() {
        Order order = new Order(Arrays.asList(VALID_INGREDIENT_1, VALID_INGREDIENT_2));
        String token = userCreateResponse.getAccessToken();

        Response response = orderSteps.createOrder(order, token);
        OrderResponse orderResponse = response.as(OrderResponse.class);

        Assert.assertEquals("Статус код должен быть 200", 200, response.getStatusCode());
        Assert.assertTrue("success=true", orderResponse.isSuccess());
        Assert.assertNotNull("Поле order не должно быть null", orderResponse.getOrder());
        Assert.assertTrue("Номер заказа должен быть > 0", orderResponse.getOrder().getNumber() > 0);

        Assert.assertNotNull("Поле name не должно быть null", orderResponse.getName());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthTest() {
        Order order = new Order(Collections.singletonList(VALID_INGREDIENT_1));
        String noToken = "";

        Response response = orderSteps.createOrder(order, noToken);
        OrderResponse orderResponse = response.as(OrderResponse.class);

        Assert.assertEquals("Статус код должен быть 200", 200, response.getStatusCode());
        Assert.assertTrue("success=true", orderResponse.isSuccess());
        Assert.assertNotNull("Поле order не должно быть null", orderResponse.getOrder());
        Assert.assertTrue("Номер заказа должен быть > 0", orderResponse.getOrder().getNumber() > 0);

        Assert.assertNotNull("Поле name не должно быть null", orderResponse.getName());
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиента")
    public void createOrderWithInvalidIngredientTest() {
        Order order = new Order(Arrays.asList(VALID_INGREDIENT_1, INVALID_INGREDIENT));
        String token = userCreateResponse.getAccessToken();

        Response response = orderSteps.createOrder(order, token);

        int actualStatus = response.getStatusCode();
        String contentType = response.getContentType();
        Assert.assertEquals("Ожидаем 500 при невалидном хеше ингредиента", 500, actualStatus);
        Assert.assertTrue("Ответ должен быть HTML при 500", contentType.contains("text/html"));

        String htmlBody = response.asString();
        System.out.println("HTML-ответ от сервера:\n" + htmlBody);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithNoIngredientsTest() {
        Order order = new Order(Collections.emptyList());
        String token = userCreateResponse.getAccessToken();

        Response response = orderSteps.createOrder(order, token);
        OrderResponse orderResponse = response.as(OrderResponse.class);

        Assert.assertEquals("Ожидаем 400 Bad Request", 400, response.getStatusCode());
        Assert.assertFalse("success=false", orderResponse.isSuccess());
        Assert.assertEquals("Сообщение должно быть 'Ingredient ids must be provided'",
                "Ingredient ids must be provided", orderResponse.getMessage());
    }

    @After
    public void tearDown() {
        if (userCreateResponse != null && userCreateResponse.getAccessToken() != null) {
            userSteps.deleteUser(userCreateResponse.getAccessToken());
        }
    }
}
