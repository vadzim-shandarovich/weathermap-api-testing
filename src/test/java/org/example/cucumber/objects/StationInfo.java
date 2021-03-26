package org.example.cucumber.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents weather station
 */
public class StationInfo {

    /* This annotation allows include in serialized string only not Null property */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("external_id")
    private String externalId;
    private String name;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal altitude;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer rank;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("source_type")
    private Integer sourceType;

    public StationInfo() {

    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public BigDecimal getAltitude() {
        return altitude;
    }

    public Integer getRank() {
        return rank;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    /* Different id setters for Jackson deserialization */
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("ID")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setID(String ID) {
        this.id = ID;
    }
}