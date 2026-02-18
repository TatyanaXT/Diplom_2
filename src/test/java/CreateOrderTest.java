import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.stellarburgers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateOrderTest {

    private String accessToken;

    UserActions userActions = new UserActions();
    OrderActions orderActions = new OrderActions();
    OrderChecks orderChecks = new OrderChecks();

    List<String> INVALID_HASH = Arrays.asList("60d3b41abdacab0026a733c6", "609646e4dc916e00276b2870");


    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void createOrderUnauthorizedTest() {
        List<String> ingredientIds = orderActions.getAllIngredientIds();
        List<String> orderIngredients = ingredientIds.subList(0, 2);

        Response response = orderActions.createOrder("", orderIngredients);
        orderChecks.checkCreationOrderSuccess(response);

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void createOrderAuthorizedTest() {
        String email = userActions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        Response responseUser = userActions.createUser(user);
        accessToken = userActions.getAccessToken(responseUser);

        List<String> ingredientIds = orderActions.getAllIngredientIds();
        List<String> orderIngredients = ingredientIds.subList(0, 2);

        Response response = orderActions.createOrder(accessToken, orderIngredients);
        orderChecks.checkCreationOrderSuccess(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientTest() {

        Response response = orderActions.createOrder("", Collections.emptyList());
        orderChecks.checkCreationOrderWithoutIngredient(response);

    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithIncorrectHashTest() {

        Response response = orderActions.createOrder("", INVALID_HASH);
        orderChecks.checkCreationOrderWithIncorrectHash(response);

    }

    @AfterEach
    @DisplayName("Удаление пользователя")
    public void deleteUser(){
        if (accessToken != null) {
            userActions.deleteUser(accessToken);
        }
    }

}
