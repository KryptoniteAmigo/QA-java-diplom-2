package steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikum.yandex.client.IngredientClient;

public class IngredientSteps {
    private final IngredientClient ingredientClient = new IngredientClient();

    @Step("Получаем список всех ингредиентов")
    public Response getIngredients() {
        return ingredientClient.getIngredients();
    }
}
