package orders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import ru.praktikum.yandex.model.*;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.Arrays;

public class GetUserOrdersTest {

    private UserSteps userSteps;
    private OrderSteps orderSteps;
    private UserCreateResponse userCreateResponse;

    @Before
    public void setUp() {
        userSteps = new UserSteps();
        orderSteps = new OrderSteps();

        User user = new User("ordersTest739@ya.ru", "qwerty123", "OrdersTestUser739");
        Response createUserResp = userSteps.createUser(user);
        userCreateResponse = createUserResp.as(UserCreateResponse.class);

        Assert.assertEquals(200, createUserResp.getStatusCode());
        Assert.assertTrue(userCreateResponse.isSuccess());

        String accessToken = userCreateResponse.getAccessToken();
        Order order1 = new Order(Arrays.asList("60d3b41abdacab0026a733c6"));
        Order order2 = new Order(Arrays.asList("609646e4dc916e00276b2870"));

        orderSteps.createOrder(order1, accessToken);
        orderSteps.createOrder(order2, accessToken);
    }

    @Test
    @DisplayName("Получение заказов авторизованным пользователем")
    public void getOrdersAuthorizedTest() {
        String token = userCreateResponse.getAccessToken();

        Response response = orderSteps.getUserOrders(token);
        GetUserOrdersResponse ordersResponse = response.as(GetUserOrdersResponse.class);

        Assert.assertEquals("Статус код при получении заказов авторизованным пользователем", 200, response.getStatusCode());
        Assert.assertTrue("success=true", ordersResponse.isSuccess());
        Assert.assertNotNull("Список orders не должен быть null", ordersResponse.getOrders());
        Assert.assertTrue("total >= 2", ordersResponse.getTotal() >= 2);
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    public void getOrdersUnauthorizedTest() {
        String noToken = "";

        Response response = orderSteps.getUserOrders(noToken);

        Assert.assertEquals("Ожидаем 401 Unauthorized", 401, response.getStatusCode());
        if (response.getContentType().contains("application/json")) {
            GetUserOrdersResponse ordersResponse = response.as(GetUserOrdersResponse.class);
            Assert.assertFalse("success должно быть false", ordersResponse.isSuccess());
            Assert.assertEquals("Некорректное сообщение", "You should be authorised", ordersResponse.getMessage());
        } else {
            System.out.println("Тело ответа (не JSON): " + response.asString());
        }
    }

    @After
    public void tearDown() {
        if (userCreateResponse != null && userCreateResponse.getAccessToken() != null) {
            userSteps.deleteUser(userCreateResponse.getAccessToken());
        }
    }
}
