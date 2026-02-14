import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.stellarburgers.*;

public class CreateOrderTest {

    private String accessToken;

    UserActions userActions = new UserActions();
    OrderActions orderActions = new OrderActions();
    OrderChecks orderChecks = new OrderChecks();

    public static final String CORRECT_JSON = "{\n\"ingredients\": [\"61c0c5a71d1f82001bdaaa70\",\"61c0c5a71d1f82001bdaaa71\",\"61c0c5a71d1f82001bdaaa6e\"]\n}";
    public static final String INVALID_HASH = "{\n\"ingredients\": [\"60d3b41abdacab0026a733c6\",\"609646e4dc916e00276b2870\"]\n}";
    public static final String EMPTY_INGREDIENTS = "{\n\"ingredients\": []\n}";


    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    public void createOrderUnauthorizedTest() {

        Response response = orderActions.createOrder("", CORRECT_JSON);
        orderChecks.checkCreationOrderSuccess(response);

    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    public void createOrderAuthorizedTest() {
        String email = userActions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        Response responseUser = userActions.createUser(user);
        accessToken = userActions.getAccessToken(responseUser);

        Response response = orderActions.createOrder(accessToken, CORRECT_JSON);
        orderChecks.checkCreationOrderSuccess(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientTest() {

        Response response = orderActions.createOrder("", EMPTY_INGREDIENTS);
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
