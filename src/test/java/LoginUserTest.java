import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.stellarburgers.User;
import org.stellarburgers.UserActions;
import org.stellarburgers.UserChecks;


public class LoginUserTest {

    private String accessToken;

    UserActions actions = new UserActions();
    UserChecks checks = new UserChecks();


    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginUserWithCorrectDataTest() {
        String email = actions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        actions.createUser(user);
        Response response = actions.authorizationUser(user);
        checks.checkLoginUserSuccess(response);

        accessToken = actions.getAccessToken(response);
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginUserWithIncorrectDataTest() {
        String email = actions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");
        Response responseCreate = actions.createUser(user);

        User userLogin = new User(email, "Pasw0rd");
        Response response = actions.authorizationUser(userLogin);
        checks.checkLoginUserIncorrect(response);

        accessToken = actions.getAccessToken(responseCreate);
    }

    @AfterEach
    @DisplayName("Удаление пользователя")
    public void deleteUser(){
        if (accessToken != null) {
            actions.deleteUser(accessToken);
        }
    }
}
