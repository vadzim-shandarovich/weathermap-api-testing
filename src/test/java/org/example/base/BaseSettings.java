package org.example.base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.HashMap;

/**
 * Sets Rest-assured request and response settings.
 */
public class BaseSettings {
    protected API api;
    protected Response response;
    protected RequestSpecification request;
    protected static ResponseSpecification responseSpecification;
    protected static HashMap<String, String> createdTestStations;
    private static final String BASE_URI = "http://api.openweathermap.org";
    private static final String BASE_PATH = "/data";

    /*
     * Constructor for implementing Dependency injection in Cucumber step definitions
     * api object stores request and response state
     */
    public BaseSettings(API api) {
        this.api = api;
    }

    public static void setBaseSettings() {
        RequestSpecification requestSpecification;

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setBasePath(BASE_PATH)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL)
                .build();

        RestAssured.requestSpecification = requestSpecification;

        /* Map of appId and stationId which would be created for tests and will be deleted after */
        createdTestStations = new HashMap<String, String>();
    }
}
