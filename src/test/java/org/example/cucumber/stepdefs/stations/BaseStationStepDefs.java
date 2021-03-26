package org.example.cucumber.stepdefs.stations;

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
 * Contains different step definitions which could be used for any station operation
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

    @Then("the service gives response status code {int}")
    public void theServiceGivesResponseStatusCode(int statusCode) {
        api.getResponse().then()
                .spec(responseSpecification)
                .assertThat().statusCode(statusCode);
        //response.then().assertThat().statusCode(110);

        /*
         * Store all stations (appId and stationId)
         * created for tests to delete them after test suite end
         */
        if (api.getOperationType() == OperationType.STATION_REGISTER) {
            /* if test station was created successfully */
            if (api.getResponse().getStatusCode() == 201) {
                createdTestStations.put(api.getAppId(), api.getResponse().then().extract().path("ID"));
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
                    .queryParam("appid", entry.getKey())
                    .pathParam("stationId", entry.getValue())
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
