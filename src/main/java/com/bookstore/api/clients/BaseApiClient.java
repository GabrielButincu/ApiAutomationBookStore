package com.bookstore.api.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseApiClient {
    
    protected abstract RequestSpecification getRequestSpec();
    
    @Step("GET request to: {endpoint}")
    public Response get(String endpoint) {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(endpoint);
    }
    
    @Step("GET request to: {endpoint} with ID: {id}")
    public Response getById(String endpoint, int id) {
        return given()
                .spec(getRequestSpec())
                .pathParam("id", id)
                .when()
                .get(endpoint + "/{id}");
    }
    
    @Step("POST request to: {endpoint}")
    public Response post(String endpoint, Object body) {
        return given()
                .spec(getRequestSpec())
                .body(body)
                .when()
                .post(endpoint);
    }
    
    @Step("PUT request to: {endpoint} with ID: {id}")
    public Response put(String endpoint, int id, Object body) {
        return given()
                .spec(getRequestSpec())
                .pathParam("id", id)
                .body(body)
                .when()
                .put(endpoint + "/{id}");
    }
    
    @Step("DELETE request to: {endpoint} with ID: {id}")
    public Response delete(String endpoint, int id) {
        return given()
                .spec(getRequestSpec())
                .pathParam("id", id)
                .when()
                .delete(endpoint + "/{id}");
    }
    
    @Step("GET request with query params: {queryParams}")
    public Response getWithQueryParams(String endpoint, Map<String, String> queryParams) {
        return given()
                .spec(getRequestSpec())
                .queryParams(queryParams)
                .when()
                .get(endpoint);
    }
}
