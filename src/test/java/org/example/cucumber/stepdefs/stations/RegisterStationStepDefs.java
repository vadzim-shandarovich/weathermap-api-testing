package org.example.cucumber.stepdefs.stations;

import org.example.base.API;
import org.example.base.BaseSettings;
import org.example.base.EndPoints;
import org.example.base.OperationType;
import org.example.cucumber.objects.StationInfo;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.hamcrest.MatcherAssert;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import static org.exparity.hamcrest.date.DateMatchers.within;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

/**
 * Contains step definitions for register station feature
 */
public class RegisterStationStepDefs extends BaseSettings {

    /*
     * Constructor for implementing Dependency injection.
     * api object stores request and response state
     */
    public RegisterStationStepDefs(API api) {
        super(api);
    }

    @Given("a customer has active API key {string}")
    @Given("a customer has inactive API key {string}")
    public void aCustomerHasActiveAPIKey(String appid) {
        request = given()
                .queryParam("appid", appid);

        api.setRequest(request);
        api.setAppId(appid);
    }

    @When("a customer provides valid station info")
    @When("a customer provides station info with incorrect longitude or latitude")
    @When("a customer provides empty station name or external_id")
    @When("a customer provides station info with empty position params")
    public void aCustomerProvidesValidStationInfo(StationInfo stationInfo) {
        response = api.getRequest().when()
                .body(stationInfo)
                .post(EndPoints.STATIONS);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_REGISTER);
    }

    @And("response includes valid station info, station ID and creation time")
    public void responseIncludesValidStationInfo(StationInfo stationInfo) {
        StationInfo responseStationInfo;

        responseStationInfo = api.getResponse().getBody().as(StationInfo.class);

        assertEquals(responseStationInfo.getExternalId(), stationInfo.getExternalId());
        assertEquals(responseStationInfo.getName(), stationInfo.getName());
        assertEquals(responseStationInfo.getLatitude().compareTo(stationInfo.getLatitude()), 0);
        assertEquals(responseStationInfo.getLongitude().compareTo(stationInfo.getLongitude()), 0);
        assertEquals(responseStationInfo.getAltitude().compareTo(stationInfo.getAltitude()), 0);
        assertEquals(responseStationInfo.getId().length(), 24);

        /* Asserts that station creation time is within 1 minute of now */
        MatcherAssert.assertThat(responseStationInfo.getCreatedAt(),
                                 within(1, ChronoUnit.MINUTES, new Date()));
        MatcherAssert.assertThat(responseStationInfo.getUpdatedAt(),
                                 within(1, ChronoUnit.MINUTES, new Date()));

        /*
        response.then()
                .assertThat()
                //.body("[0].name", equalTo("Minsk Test Station"));
                //.body("name", hasItem("Minsk Test Station"));
                //.body("ID", hasLength(24));
                .body(".", hasSize(2));
         */
    }

    @Given("a customer has not API key")
    public void aCustomerHasNotAPIKey() {
        request = given();
        api.setRequest(request);
    }

    @When("a customer does not provide station info")
    public void aCustomerDoesNotProvideStationInfo() {
        response = request.when()
                .post(EndPoints.STATIONS);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_REGISTER);
    }

    @When("a customer provides wrong station info")
    public void aCustomerProvidesWrongStationInfo(Map<String,String> wrongStationInfo) {
        response = api.getRequest().when()
                .body(wrongStationInfo)
                .post(EndPoints.STATIONS);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_REGISTER);
    }
}