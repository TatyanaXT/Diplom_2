import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.stellarburgers.User;
import org.stellarburgers.UserActions;
import org.stellarburgers.UserChecks;

import java.util.stream.Stream;


public class UpdateUserTest {

    private String accessToken;

    UserActions actions = new UserActions();
    UserChecks checks = new UserChecks();

    private static Stream<Arguments> updateUserParameters() {
        return Stream.of(
                Arguments.of(1, "str0nGPasw0rd", "Тестовый пользователь"),
                Arguments.of(0, "newstr0nGPasw0rd", "Тестовый пользователь"),
                Arguments.of(0, "str0nGPasw0rd", "Самый классный пользователь")
        );
    }

    @ParameterizedTest
    @DisplayName("Обновление данных пользователя")
    @MethodSource("updateUserParameters")
    public void updateUserParametersTest(int emailFlag, String password, String name) {
        String email = actions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        Response responseCreate = actions.createUser(user);
        accessToken = actions.getAccessToken(responseCreate);

        if (emailFlag == 1) {
            email = "email_for_my_tests@ya.ru";
        }

        User newDataUser = new User(email, password, name);

        Response responseUpdate = actions.updateUser(newDataUser, accessToken);
        checks.checkUpdateUserSuccess(responseUpdate);
    }

    @ParameterizedTest
    @DisplayName("Обновление данных без авторизации")
    @MethodSource("updateUserParameters")
    public void updateUserUnauthorisedTest(int emailFlag, String password, String name) {
        String email = actions.generateUserEmail();
        User user = new User(email, "str0nGPasw0rd", "Тестовый пользователь");

        Response responseCreate = actions.createUser(user);
        accessToken = actions.getAccessToken(responseCreate);

        if (emailFlag == 1) {
            email = "email_for_my_tests@ya.ru";
        }

        User newDataUser = new User(email, password, name);

        Response responseUpdate = actions.updateUser(newDataUser, "");
        checks.checkUpdateUserUnauthorised(responseUpdate);
    }

    @AfterEach
    @DisplayName("Удаление пользователя")
    public void deleteUser(){
        if (accessToken != null) {
            actions.deleteUser(accessToken);
        }
    }


}
