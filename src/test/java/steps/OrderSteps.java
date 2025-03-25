package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.yandex.client.OrderClient;
import ru.praktikum.yandex.model.Order;

public class OrderSteps {
    private final OrderClient orderClient = new OrderClient();

    @Step("Создать новый заказ")
    public Response createOrder(Order order, String accessToken) {
        return orderClient.createOrder(order, accessToken);
    }

    @Step("Получить заказы пользователя")
    public Response getUserOrders(String accessToken) {
        return orderClient.getUserOrders(accessToken);
    }
}
