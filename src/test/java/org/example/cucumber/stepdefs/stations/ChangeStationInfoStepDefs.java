package org.example.cucumber.stepdefs.stations;

import org.example.base.API;
import org.example.base.BaseSettings;
import org.example.base.EndPoints;
import org.example.base.OperationType;
import org.example.cucumber.objects.StationInfo;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.hamcrest.MatcherAssert;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import static org.exparity.hamcrest.date.DateMatchers.within;
import static org.junit.Assert.assertEquals;

/**
 * Contains step definitions for change station feature
 */
public class ChangeStationInfoStepDefs extends BaseSettings {

    /*
     * Constructor for implementing Dependency injection.
     * api object stores request and response state
     */
    public ChangeStationInfoStepDefs(API api) {
        super(api);
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

        assertEquals(stationInfo.getExternalId(), responseStationInfo.getExternalId());
        assertEquals(stationInfo.getName(), responseStationInfo.getName());
        assertEquals(0, responseStationInfo.getLatitude().compareTo(stationInfo.getLatitude()));
        assertEquals(0, responseStationInfo.getLongitude().compareTo(stationInfo.getLongitude()));
        assertEquals(0, responseStationInfo.getAltitude().compareTo(stationInfo.getAltitude()));
        assertEquals(api.getRegisteredStation().getId(), responseStationInfo.getId());

        /* Asserts that station creation time is within 1 micros of it's real creation */
        MatcherAssert.assertThat(responseStationInfo.getCreatedAt(),
                                 within(1, ChronoUnit.MICROS, api.getRegisteredStation().getCreatedAt()));
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
}