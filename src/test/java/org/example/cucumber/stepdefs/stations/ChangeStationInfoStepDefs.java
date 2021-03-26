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
import static io.restassured.RestAssured.given;
import static org.exparity.hamcrest.date.DateMatchers.within;
import static org.junit.Assert.assertEquals;

/**
 * Contains step definitions for change station feature
 */
public class ChangeStationInfoStepDefs extends BaseSettings {
    private StationInfo registeredStation;

    /*
     * Constructor for implementing Dependency injection.
     * api object stores request and response state
     */
    public ChangeStationInfoStepDefs(API api) {
        super(api);
    }

    @Given("there is registered weather station for {string} API key")
    public void thereIsRegisteredWeatherStationForAPIKey(String appid, StationInfo stationInfo) {
        request = given().queryParam("appid", appid);
        response = request.when()
                .body(stationInfo)
                .post(EndPoints.STATIONS);

        response.then()
                .spec(responseSpecification)
                .assertThat()
                .statusCode(201);

        registeredStation = response.getBody()
                .as(StationInfo.class);

        /*
         * Store all stations (appId and stationId)
         * created for tests to delete them after test suite end
         */
        createdTestStations.put(appid, registeredStation.getId());
    }

    @Given("a customer with active API key {string} has chosen the station")
    @Given("a customer with inactive API key {string} has chosen the station")
    public void aCustomerWithActiveKeyHasChosenTheStation(String appid) {
        request = given()
                .queryParam("appid", appid)
                .pathParam("stationId", registeredStation.getId());

        api.setRequest(request);
        api.setAppId(appid);
    }

    @When("a customer provides valid station info for change")
    @When("a customer provides station info for change with incorrect longitude or latitude")
    @When("a customer provides station info for change with empty position params")
    @When("a customer provides empty station name or external_id for change")
    public void aCustomerProvidesValidStationInfoForChange(StationInfo stationInfo) {
        response = api.getRequest().when()
                .body(stationInfo)
                .put(EndPoints.UNIQUE_STATION);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_CHANGE);
    }

    @And("response includes valid station info, station ID and update time")
    public void responseIncludesValidStationInfoStationIDAndUpdateTime(StationInfo stationInfo) {
        StationInfo responseStationInfo;

        responseStationInfo = api.getResponse().getBody().as(StationInfo.class);

        assertEquals(responseStationInfo.getExternalId(), stationInfo.getExternalId());
        assertEquals(responseStationInfo.getName(), stationInfo.getName());
        assertEquals(responseStationInfo.getLatitude().compareTo(stationInfo.getLatitude()), 0);
        assertEquals(responseStationInfo.getLongitude().compareTo(stationInfo.getLongitude()), 0);
        assertEquals(responseStationInfo.getAltitude().compareTo(stationInfo.getAltitude()), 0);
        assertEquals(responseStationInfo.getId(), registeredStation.getId());

        /* Asserts that station creation time is within 1 micros of it's real creation */
        MatcherAssert.assertThat(responseStationInfo.getCreatedAt(),
                                 within(1, ChronoUnit.MICROS, registeredStation.getCreatedAt()));
        /* Asserts that station update time is within 1 minute of now */
        MatcherAssert.assertThat(responseStationInfo.getUpdatedAt(),
                                 within(1, ChronoUnit.MINUTES, new Date()));
    }

    @When("a customer does not provide station info for change")
    public void aCustomerDoesNotProvideStationInfoForChange() {
        response = api.getRequest().when()
                .put(EndPoints.UNIQUE_STATION);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_CHANGE);
    }

    @When("a customer provides wrong station info for change")
    public void aCustomerProvidesWrongStationInfo(Map<String,String> wrongStationInfo) {
        response = api.getRequest().when()
                .body(wrongStationInfo)
                .put(EndPoints.UNIQUE_STATION);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_CHANGE);
    }

    @Given("a customer without API key has chosen the station")
    public void aCustomerWithoutAPIKeyHasChosenTheStation() {
        request = given()
                .pathParam("stationId", registeredStation.getId());
        api.setRequest(request);
    }

    @Given("a customer with active API key has chosen nonexistent station")
    public void aCustomerWithActiveAPIKeyHasChosenNonexistentStation(Map<String, String> requestParams) {
        request = given()
                .queryParam("appid", requestParams.get("appid"))
                .pathParam("stationId", requestParams.get("stationId"));

        api.setRequest(request);
        api.setAppId(requestParams.get("appid"));
    }
}