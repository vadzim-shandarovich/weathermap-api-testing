package org.example.cucumber.stepdefs.stations;

import io.cucumber.java.en.When;
import org.example.base.API;
import org.example.base.BaseSettings;
import org.example.base.EndPoints;
import org.example.base.OperationType;

/**
 * Contains step definitions for delete station feature
 */
public class DeleteStationStepDefs extends BaseSettings {

    /*
     * Constructor for implementing Dependency injection.
     * api object stores request and response state
     */
    public DeleteStationStepDefs(API api) {
        super(api);
    }

    @When("^a customer performs delete request$")
    public void aCustomerPerformsDeleteRequest() {
        response = api.getRequest().when()
                .delete(EndPoints.UNIQUE_STATION);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_DELETE);
    }

    @When("a customer performs delete request on STATIONS endpoint")
    public void aCustomerPerformsDeleteRequestOnSTATIONSEndpoint() {
        response = api.getRequest().when()
                .delete(EndPoints.STATIONS);

        api.setResponse(response);
        api.setOperationType(OperationType.STATION_DELETE);
    }
}
