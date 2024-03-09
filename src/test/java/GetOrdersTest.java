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

public class GetOrdersTest {

    ClientSteps clientSteps = new ClientSteps();
    OrderSteps orderSteps = new OrderSteps();
    private User user;
    private Order order;
    private String[] ingredients = {"61c0c5a71d1f82001bdaaa77",
            "61c0c5a71d1f82001bdaaa73",
            "61c0c5a71d1f82001bdaaa6d"};
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
    @DisplayName("Проверка успешного получения заказов авторизованного пользователя")
    public void getLoggedInUserOrders() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        orderSteps.createOrder(order, accessToken);

        ValidatableResponse response2 = orderSteps.getUserOrders(accessToken);
        orderSteps.checkGetAuthorizedUserOrders(response2);
    }

    @Test
    @DisplayName("Проверка неуспешного получения заказов неавторизованного пользователя")
    public void getLoggedOutUserOrders() {
        ValidatableResponse response1 = clientSteps.loginUser(user);
        accessToken = clientSteps.extractAccessToken(response1);

        orderSteps.createOrder(order, accessToken);

        ValidatableResponse response2 = orderSteps.getUserOrders();
        orderSteps.checkGetUnauthorizedUserOrders(response2);
    }

}