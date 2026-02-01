package com.bookstore.api.specs;

import com.bookstore.api.config.ConfigurationManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecs {

    public static RequestSpecification getDefaultRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getConfig().baseUri())
                .setBasePath(ConfigurationManager.getConfig().apiVersion())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification getBooksRequestSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getDefaultRequestSpec())
                .setBasePath(ConfigurationManager.getConfig().apiVersion() + ConfigurationManager.getConfig().booksEndpoint())
                .build();
    }

    public static RequestSpecification getAuthorsRequestSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(getDefaultRequestSpec())
                .setBasePath(ConfigurationManager.getConfig().apiVersion() + ConfigurationManager.getConfig().authorsEndpoint())
                .build();
    }
}
