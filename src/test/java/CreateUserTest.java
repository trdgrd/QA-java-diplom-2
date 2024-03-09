import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.pojo.User;
import ru.praktikum.steps.ClientSteps;

public class CreateUserTest {

    private ClientSteps clientSteps = new ClientSteps();
    private User user;
    private String accessToken;
    private User userWithoutEmail;
    private User userWithoutPassword;
    private User userWithoutName;

    @Before
    public void setUp() {
        user = new User(
                RandomStringUtils.randomAlphabetic(8) + "@yndx.ru",
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(8)
        );
    }

    @Test
    @DisplayName("Проверка успешного создания пользователя с именем, e-mail и паролем")
    public void createUserTest() {
        ValidatableResponse response1 = clientSteps.registerUser(user);
        clientSteps.checkUserRegistered(response1);

        ValidatableResponse response2 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response2);
        clientSteps.deleteUser(user, accessToken);
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя с существующим логином")
    public void createUserWithExistedEmailTest() {
        ValidatableResponse response1 = clientSteps.registerUser(user);
        clientSteps.checkUserRegistered(response1);

        ValidatableResponse response2 = clientSteps.registerUser(user);
        clientSteps.checkExistedUserNotRegistered(response2);

        ValidatableResponse response3 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response3);
        clientSteps.deleteUser(user, accessToken);
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя с отсутствующим логином")
    public void createUserWithoutEmailTest() {
        userWithoutEmail = new User();
        userWithoutEmail.setName(RandomStringUtils.randomAlphabetic(10));
        userWithoutEmail.setPassword(RandomStringUtils.randomAlphabetic(10));

        ValidatableResponse response = clientSteps.registerUser(userWithoutEmail);
        clientSteps.checkUserWithoutRequiredFieldsNotCreated(response);
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя с отсутствующим паролем")
    public void createUserWithoutPasswordTest() {
        userWithoutPassword = new User();
        userWithoutPassword.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yndx.ru");
        userWithoutPassword.setName(RandomStringUtils.randomAlphabetic(10));

        ValidatableResponse response = clientSteps.registerUser(userWithoutPassword);
        clientSteps.checkUserWithoutRequiredFieldsNotCreated(response);
    }

    @Test
    @DisplayName("Проверка невозможности создания пользователя с отсутствующим именем")
    public void createUserWithoutNameTest() {
        userWithoutName = new User();
        userWithoutName.setPassword(RandomStringUtils.randomAlphabetic(10));
        userWithoutName.setEmail(RandomStringUtils.randomAlphabetic(8) + "@yndx.ru");

        ValidatableResponse response = clientSteps.registerUser(userWithoutName);
        clientSteps.checkUserWithoutRequiredFieldsNotCreated(response);
    }

}