package org.example.cucumber.stepdefs.stations;

import io.cucumber.java.en.Given;
import org.example.base.API;
import org.example.base.BaseSettings;
import org.example.base.EndPoints;
import org.example.base.OperationType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.cucumber.objects.StationInfo;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import java.util.Map;
import static org.hamcrest.Matchers.containsString;
import static io.restassured.RestAssured.given;

/**
 * Contains step definitions which could be used for different station operations
 */
public class BaseStationStepDefs extends BaseSettings {

    /*
     * Constructor for implementing Dependency injection.
     * api object stores request and response state
     */
    public BaseStationStepDefs(API api) {
        super(api);
    }

    @Before
    public void setBaseRestAssuredSettings() {
        setBaseSettings();
    }

    /*
     * Used to convert scenario DataTable with station info
     * to StationInfo object in step definitions
     */
    @DataTableType
    public StationInfo mapToStationInfoConverter(Map<String, String> entry) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(entry, StationInfo.class);
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

        api.setRegisteredStation(response.getBody().as(StationInfo.class));

        /*
         * Store all stations (stationId and appId)
         * created for tests to delete them after test suite end
         */
        createdTestStations.put(api.getRegisteredStation().getId(), appid);
    }

    @Given("a customer with active API key {string} has chosen the station")
    @Given("a customer with inactive API key {string} has chosen the station")
    public void aCustomerWithActiveKeyHasChosenTheStation(String appid) {
        request = given()
                .queryParam("appid", appid)
                .pathParam("stationId", api.getRegisteredStation().getId());

        api.setRequest(request);
        api.setAppId(appid);
    }

    @Given("a customer without API key has chosen the station")
    public void aCustomerWithoutAPIKeyHasChosenTheStation() {
        request = given()
                .pathParam("stationId", api.getRegisteredStation().getId());

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

    @Given("a customer with active API key {string} has not chosen the station")
    public void aCustomerWithActiveAPIKeyHasNotChosenTheStation(String appid) {
        request = given()
                .queryParam("appid", appid);

        api.setRequest(request);
    }

    @Then("the service gives response status code {int}")
    public void theServiceGivesResponseStatusCode(int statusCode) {
        api.getResponse().then()
                .spec(responseSpecification)
                .assertThat().statusCode(statusCode);
        //response.then().assertThat().statusCode(110);

        if (api.getOperationType() == OperationType.STATION_REGISTER) {

            /*
             * If test station was created successfully
             * store stationId and appId to delete station after test suite end
             */
            if (api.getResponse().getStatusCode() == 201) {
                createdTestStations.put(api.getResponse().then().extract().path("ID"), api.getAppId());
            }
        } else if(api.getOperationType() == OperationType.STATION_DELETE){

            /*
             * If test station was deleted successfully during test scenario
             * remove stationId and appId from createdTestStations map
             */
            if (api.getResponse().getStatusCode() == 204) {
                createdTestStations.remove(api.getRegisteredStation().getId());
            }
        }
    }

    @And("response message includes {string}")
    public void responseMessageIncludes(String message) {
        api.getResponse().then()
                .assertThat()
                .body("message", containsString(message));
    }

    /*
     * Delete all test stations which were created during test suite
     */
    @After
    public void deleteAllTestStations() {
        if (!createdTestStations.isEmpty()) {
            for (Map.Entry<String, String> entry : createdTestStations.entrySet()) {
                given()
                    .pathParam("stationId", entry.getKey())
                    .queryParam("appid", entry.getValue())
                    .when()
                        .delete(EndPoints.UNIQUE_STATION)
                        .then()
                            .spec(responseSpecification)
                            .assertThat()
                            .statusCode(204);
            }
        }
    }
}
