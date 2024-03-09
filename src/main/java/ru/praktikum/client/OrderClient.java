package ru.praktikum.client;

import io.restassured.response.Response;
import ru.praktikum.pojo.Order;

public class OrderClient extends BaseHttpClient {

    private static final String BASE_PATH = "/api/orders";

    public Response create(Order order) {
        return postRequest(BASE_PATH, order);
    }

    public Response create(Order order, String header) {
        return postRequest(BASE_PATH, order, header);
    }

    public Response get() {
        return getRequest(BASE_PATH);
    }

    public Response get(String header) {
        return getRequest(BASE_PATH, header);
    }

}