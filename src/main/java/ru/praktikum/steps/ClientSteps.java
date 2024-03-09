package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import ru.praktikum.client.AuthClient;
import ru.praktikum.pojo.User;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ClientSteps {

    AuthClient authClient = new AuthClient();

    @Step("Создание пользователя")
    public ValidatableResponse registerUser(User user) {
        return authClient.register(user).then();
    }

    @Step("Проверка что пользователь зарегистрирован")
    public ValidatableResponse checkUserRegistered(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true));
    }

    @Step("Проверка невозможности регистрации уже зарегистрированного пользователя")
    public ValidatableResponse checkExistedUserNotRegistered(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("User already exists"));
    }

    @Step("Проверка невозможности регистрации пользователя без заполнения обязательных полей")
    public ValidatableResponse checkUserWithoutRequiredFieldsNotCreated(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(User user) {
        return authClient.login(user).then();
    }

    @Step("Проверка что пользователь залогинен")
    public ValidatableResponse checkUserLoggedIn(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("accessToken", is(notNullValue()))
                .body("refreshToken", is(notNullValue()));
    }

    @Step("Проверка невозможности логина пользователя с невалидными данными учетной записи")
    public ValidatableResponse checkUserWithInvalidCredentialsNotLoggedIn(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Step("Изменение данных о пользователе")
    public ValidatableResponse updateUser(User user) {
        return authClient.update(user).then();
    }

    @Step("Изменение данных авторизованного пользователя")
    public ValidatableResponse updateUser(User user, String accessToken) {
        return authClient.update(user, accessToken).then();
    }

    @Step("Проверка что данные пользователя были изменены")
    public ValidatableResponse checkUserUpdated(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_OK)
                .body("success", is(true))
                .body("user", is(notNullValue()));
    }

    @Step("Проверка невозможности изменения данных неавторизованного пользователя")
    public ValidatableResponse checkLoggedOutUserNotUpdated(ValidatableResponse response) {
        return response
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(User user, String accessToken) {
        return authClient.delete(user, accessToken).then();
    }

    @Step("Получение access token")
    public String extractAccessToken(ValidatableResponse response) {
        return response.extract().body().path("accessToken");
    }

}