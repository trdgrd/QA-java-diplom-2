package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum.client.OrderClient;
import ru.praktikum.pojo.Order;

import static org.hamcrest.Matchers.*;

public class OrderSteps {

    OrderClient orderClient = new OrderClient();

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return orderClient.create(order).then();
    }

    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return orderClient.create(order, accessToken).then();
    }

    @Step("Проверка что заказ создан")
    public ValidatableResponse checkOrderCreated(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_OK)
                .body("name", is(notNullValue()))
                .body("order", is(notNullValue()))
                .body("success", is(true));
    }

    @Step("Проверка невозможности создания заказа без авторизации")
    public ValidatableResponse checkLoggedOutUserCantCreateOrder(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Step("Проверка невозможности создания заказа без ингредиентов")
    public ValidatableResponse checkOrderWithoutIngredientsNotCreated(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Step("Проверка невозможности создания заказа с невалидными ингредиентами")
    public ValidatableResponse checkOrderWithInvalidIngredientsNotCreated(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Получение заказов авторизованным пользователем")
    public ValidatableResponse getUserOrders(String accessToken) {
        return orderClient.get(accessToken).then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrders() {
        return orderClient.get().then();
    }

    @Step("Проверка получения заказов авторизованным пользователем")
    public ValidatableResponse checkGetAuthorizedUserOrders(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("orders", hasSize(greaterThan(0)));
    }

    @Step("Проверка невозможности получения заказов неавторизованным пользователем")
    public ValidatableResponse checkGetUnauthorizedUserOrders(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

}