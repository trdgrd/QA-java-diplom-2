import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.pojo.User;
import ru.praktikum.steps.ClientSteps;

public class UpdateUserTest {

    private ClientSteps clientSteps = new ClientSteps();
    private User user;
    private String accessToken;

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
    @DisplayName("Проверка успешного обновления имени авторизованного юзера")
    public void loggedInUserNameUpdateTest() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        user.setName(RandomStringUtils.randomAlphabetic(8));

        ValidatableResponse response2 = clientSteps.updateUser(user, accessToken);
        clientSteps.checkUserUpdated(response2);
    }

    @Test
    @DisplayName("Проверка успешного обновления пароля авторизованного юзера")
    public void loggedInUserPasswordUpdateTest() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        user.setPassword(RandomStringUtils.randomAlphabetic(10));

        ValidatableResponse response2 = clientSteps.updateUser(user, accessToken);
        clientSteps.checkUserUpdated(response2);
    }

    @Test
    @DisplayName("Проверка успешного обновления e-mail авторизованного юзера")
    public void loggedInUserEmailUpdateTest() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        user.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yndx.ru");

        ValidatableResponse response2 = clientSteps.updateUser(user, accessToken);
        clientSteps.checkUserUpdated(response2);
    }

    @Test
    @DisplayName("Проверка невозможности обновления информации неавторизованного юзера")
    public void loggedOutUserUpdateTest() {
        user.setName(RandomStringUtils.randomAlphabetic(8));

        ValidatableResponse response1 = clientSteps.updateUser(user);
        clientSteps.checkLoggedOutUserNotUpdated(response1);

        ValidatableResponse response2 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response2);
    }

}