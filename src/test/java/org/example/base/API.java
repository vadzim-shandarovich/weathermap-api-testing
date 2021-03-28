package org.example.base;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.cucumber.objects.StationInfo;

/**
 * Stores info to share it between different step definitions classes
 * (Dependency injection in Cucumber step definitions)
 */
public class API {
    private Response response;
    private RequestSpecification request;
    private OperationType operationType;
    private String appId;

    /* Station which was created in Rule feature section */
    private StationInfo registeredStation;

    public Response getResponse() {
        return response;
    }

    public RequestSpecification getRequest() {
        return request;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public String getAppId() {
        return appId;
    }

    public StationInfo getRegisteredStation() {
        return registeredStation;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void setRequest(RequestSpecification request) {
        this.request = request;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setRegisteredStation(StationInfo registeredStation) {
        this.registeredStation = registeredStation;
    }
}
