import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.pojo.User;
import ru.praktikum.steps.ClientSteps;

public class LoginUserTest {

    private ClientSteps clientSteps = new ClientSteps();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        user = new User(
                RandomStringUtils.randomAlphabetic(8) + "@yndx.ru",
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(8)
        );

        clientSteps.registerUser(user);
    }

    @After
    public void tearDown() {
        clientSteps.deleteUser(user, accessToken);
    }

    @Test
    @DisplayName("Проверка успешной авторизации под существующим пользователем")
    public void authExistedUserTest() {
        ValidatableResponse response = clientSteps.loginUser(user);
        clientSteps.checkUserLoggedIn(response);

        accessToken = clientSteps.extractAccessToken(response);
    }

    @Test
    @DisplayName("Проверка невозможности авторизации с неверным логином")
    public void authUserWithInvalidLoginTest() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        user.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yndx.ru");

        ValidatableResponse response2 = clientSteps.loginUser(user);
        clientSteps.checkUserWithInvalidCredentialsNotLoggedIn(response2);
    }

    @Test
    @DisplayName("Проверка невозможности авторизации с неверным паролем")
    public void authUserWithInvalidPasswordTest() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        user.setPassword(RandomStringUtils.randomAlphabetic(10));

        ValidatableResponse response = clientSteps.loginUser(user);
        clientSteps.checkUserWithInvalidCredentialsNotLoggedIn(response);
    }

}