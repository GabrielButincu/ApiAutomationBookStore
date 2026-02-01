package com.bookstore.api.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

public class ResponseSpecs {
    
    public static ResponseSpecification getDefaultResponseSpec() {
        return new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .expectResponseTime(Matchers.lessThan(30000L))
                .build();
    }
    
    public static ResponseSpecification get200ResponseSpec() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getDefaultResponseSpec())
                .expectStatusCode(200)
                .build();
    }
    
    public static ResponseSpecification get400ResponseSpec() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getDefaultResponseSpec())
                .expectStatusCode(400)
                .build();
    }
    
    public static ResponseSpecification get404ResponseSpec() {
        return new ResponseSpecBuilder()
                .addResponseSpecification(getDefaultResponseSpec())
                .expectStatusCode(404)
                .build();
    }
}
