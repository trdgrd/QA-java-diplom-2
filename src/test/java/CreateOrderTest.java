import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.pojo.Order;
import ru.praktikum.pojo.User;
import ru.praktikum.steps.ClientSteps;
import ru.praktikum.steps.OrderSteps;

public class CreateOrderTest {

    ClientSteps clientSteps = new ClientSteps();
    OrderSteps orderSteps = new OrderSteps();
    private User user;
    private Order order;
    private String[] ingredients = {"61c0c5a71d1f82001bdaaa77",
            "61c0c5a71d1f82001bdaaa73",
            "61c0c5a71d1f82001bdaaa6d"};

    private String[] emptyIngredients = {};
    private String[] invalidIngredients = {RandomStringUtils.randomAlphabetic(10)};
    private String accessToken;


   @Before
    public void setUp() {
        user = new User(
                RandomStringUtils.randomAlphabetic(8) + "@yndx.ru",
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(8)
        );

        order = new Order(ingredients);

        clientSteps.registerUser(user);
    }

    @After
    public void tearDown() {
        clientSteps.deleteUser(user, accessToken);
    }

    @Test
    @DisplayName("Проверка успешного создания заказа с ингредиентами авторизованным пользователем")
    public void createOrderLoggedIn() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        ValidatableResponse response2 = orderSteps.createOrder(order, accessToken);
        orderSteps.checkOrderCreated(response2);
    }

    @Test
    @DisplayName("Проверка неуспешного создания заказа неавторизованным пользователем")
    @Issue("bug")
    public void createOrderLoggedOut() {
        ValidatableResponse response1 = orderSteps.createOrder(order);
        orderSteps.checkLoggedOutUserCantCreateOrder(response1);

        ValidatableResponse response2 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response2);
    }

    @Test
    @DisplayName("Проверка неуспешного создания заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        order.setIngredients(emptyIngredients);
        ValidatableResponse response2 = orderSteps.createOrder(order, accessToken);
        orderSteps.checkOrderWithoutIngredientsNotCreated(response2);
    }

    @Test
    @DisplayName("Проверка неуспешного создания заказа с невалидным ингредиентом")
    public void createOrderWithInvalidIngredients() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        order.setIngredients(invalidIngredients);
        ValidatableResponse response2 = orderSteps.createOrder(order, accessToken);
        orderSteps.checkOrderWithInvalidIngredientsNotCreated(response2);
    }

}