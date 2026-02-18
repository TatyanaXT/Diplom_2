import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.stellarburgers.OrderActions;
import org.stellarburgers.OrderChecks;
import org.stellarburgers.User;
import org.stellarburgers.UserActions;

import java.util.List;

public class GetOrderTest {

    private String accessToken;

    UserActions userActions = new UserActions();
    OrderActions orderActions = new OrderActions();
    OrderChecks orderChecks = new OrderChecks();

    public static final String CORRECT_JSON = "{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa71\",\"61c0c5a71d1f82001bdaaa6e\"]\n}";


    @Test
    @DisplayName("Получение заказов неавторизованным пользователем")
    public void getOrderUnauthorizedTest() {

        Response response = orderActions.getOrder("");
        orderChecks.checkGetOrdersUnauthorised(response);

    }

    @Test
    @DisplayName("Получение заказа авторизованным пользователем")
    public void getOrderAuthorizedTest() {
        String email = userActions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        Response responseUser = userActions.createUser(user);
        accessToken = userActions.getAccessToken(responseUser);

        List<String> ingredientIds = orderActions.getAllIngredientIds();
        List<String> orderIngredients = ingredientIds.subList(0, 2);

        orderActions.createOrder(accessToken, orderIngredients);

        Response response = orderActions.getOrder(accessToken);
        orderChecks.checkGetOrdersSuccess(response);
    }

    @AfterEach
    @DisplayName("Удаление пользователя")
    public void deleteUser(){
        if (accessToken != null) {
            userActions.deleteUser(accessToken);
        }
    }
}
