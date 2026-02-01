package com.bookstore.api.utils;

import com.bookstore.api.models.Author;
import com.bookstore.api.models.Book;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiAssertions {
    
    @Step("Verify status code is: {expectedStatusCode}")
    public static void verifyStatusCode(Response response, int expectedStatusCode) {
        assertThat(response.getStatusCode())
                .as("Status code verification")
                .isEqualTo(expectedStatusCode);
    }
    
    @Step("Verify response contains header: {headerName}")
    public static void verifyHeaderExists(Response response, String headerName) {
        assertThat(response.getHeader(headerName))
                .as("Header %s should exist", headerName)
                .isNotNull();
    }
    
    @Step("Verify content type is JSON")
    public static void verifyJsonContentType(Response response) {
        assertThat(response.getContentType())
                .as("Content-Type should be JSON")
                .contains("application/json");
    }
    
    @Step("Verify response time is under {maxTimeMs}ms")
    public static void verifyResponseTime(Response response, long maxTimeMs) {
        assertThat(response.getTime())
                .as("Response time should be under %d ms", maxTimeMs)
                .isLessThan(maxTimeMs);
    }
    
    @Step("Verify book details match")
    public static void verifyBookDetails(Book actual, Book expected) {
        SoftAssertions softly = new SoftAssertions();
        
        if (expected.getId() != null) {
            softly.assertThat(actual.getId())
                    .as("Book ID")
                    .isEqualTo(expected.getId());
        }
        
        if (expected.getTitle() != null) {
            softly.assertThat(actual.getTitle())
                    .as("Book title")
                    .isEqualTo(expected.getTitle());
        }
        
        if (expected.getDescription() != null) {
            softly.assertThat(actual.getDescription())
                    .as("Book description")
                    .isEqualTo(expected.getDescription());
        }
        
        if (expected.getPageCount() != null) {
            softly.assertThat(actual.getPageCount())
                    .as("Page count")
                    .isEqualTo(expected.getPageCount());
        }
        
        softly.assertAll();
    }
    
    @Step("Verify author details match")
    public static void verifyAuthorDetails(Author actual, Author expected) {
        SoftAssertions softly = new SoftAssertions();
        
        if (expected.getId() != null) {
            softly.assertThat(actual.getId())
                    .as("Author ID")
                    .isEqualTo(expected.getId());
        }
        
        if (expected.getFirstName() != null) {
            softly.assertThat(actual.getFirstName())
                    .as("First name")
                    .isEqualTo(expected.getFirstName());
        }
        
        if (expected.getLastName() != null) {
            softly.assertThat(actual.getLastName())
                    .as("Last name")
                    .isEqualTo(expected.getLastName());
        }
        
        if (expected.getIdBook() != null) {
            softly.assertThat(actual.getIdBook())
                    .as("Book ID reference")
                    .isEqualTo(expected.getIdBook());
        }
        
        softly.assertAll();
    }
    
    @Step("Verify list is not empty")
    public static void verifyListNotEmpty(Response response) {
        assertThat(response.jsonPath().getList("$"))
                .as("Response list should not be empty")
                .isNotEmpty();
    }
    
    @Step("Verify error response")
    public static void verifyErrorResponse(Response response, int expectedStatus) {
        verifyStatusCode(response, expectedStatus);
        assertThat(response.getBody().asString())
                .as("Error response should not be empty")
                .isNotEmpty();
    }
}
