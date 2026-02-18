import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.stellarburgers.User;
import org.stellarburgers.UserActions;
import org.stellarburgers.UserChecks;

import java.util.stream.Stream;


public class CreateUserTest {

    private String accessToken;

    UserActions actions = new UserActions();
    UserChecks checks = new UserChecks();

    private static Stream<Arguments> createUserParameters() {
        return Stream.of(
                Arguments.of("", "str0nGPasw0rd", "Тестовый пользователь"),
                Arguments.of("test-data@yandex.ru", "", "Тестовый пользователь"),
                Arguments.of("test-data@yandex.ru", "str0nGPasw0rd", "")
        );
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUserWithAllParametersTest() {
        String email = actions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        Response response = actions.createUser(user);
        checks.checkCreationUserSuccess(response);

        accessToken = actions.getAccessToken(response);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUserAlreadyExistTest() {
        String email = actions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        Response responseCreated = actions.createUser(user);
        accessToken = actions.getAccessToken(responseCreated);

        Response response = actions.createUser(user);
        checks.checkCreationUserExist(response);

    }


    @ParameterizedTest
    @DisplayName("Создание пользователя без заполнения одного из обязательных полей")
    @MethodSource("createUserParameters")
    public void createUserWithoutFieldTest(String email, String password, String name) {
        User user = new User(email, password, name);

        Response response = actions.createUser(user);
        checks.checkCreationUserWithoutField(response);

    }


    @AfterEach
    @DisplayName("Удаление пользователя")
    public void deleteUser(){
        if (accessToken != null) {
            actions.deleteUser(accessToken);
        }
    }
}
