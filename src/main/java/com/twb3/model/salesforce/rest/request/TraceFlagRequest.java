package com.twb3.model.salesforce.rest.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.twb3.enums.LogTypes;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ApexCode",
    "ApexProfiling",
    "Callout",
    "Database",
    "DebugLevelId",
    "ExpirationDate",
    "LogType",
    "StartDate",
    "TracedEntityId",
    "Validation",
    "Visualforce",
    "Workflow"
})
public class TraceFlagRequest implements BaseSObjectRequest {

    @JsonProperty("ApexCode")
    private String apexCode;
    @JsonProperty("ApexProfiling")
    private String apexProfiling;
    @JsonProperty("Callout")
    private String callout;
    @JsonProperty("Database")
    private String database;
    @JsonProperty("DebugLevelId")
    private String debugLevelId;
    @JsonProperty("ExpirationDate")
    private String expirationDate;
    @JsonProperty("LogType")
    private String logType;
    @JsonProperty("StartDate")
    private String startDate;
    @JsonProperty("TracedEntityId")
    private String tracedEntityId;
    @JsonProperty("Validation")
    private String validation;
    @JsonProperty("Visualforce")
    private String visualforce;
    @JsonProperty("Workflow")
    private String workflow;

    public TraceFlagRequest(String tracedEntityId, String debugLevelId, LogTypes logType) {
        this.debugLevelId = debugLevelId;
        this.logType = logType.name();
        this.tracedEntityId = tracedEntityId;
    }

    @JsonProperty("ApexCode")
    public String getApexCode() {
        return apexCode;
    }

    @JsonProperty("ApexCode")
    public void setApexCode(String apexCode) {
        this.apexCode = apexCode;
    }

    @JsonProperty("ApexProfiling")
    public String getApexProfiling() {
        return apexProfiling;
    }

    @JsonProperty("ApexProfiling")
    public void setApexProfiling(String apexProfiling) {
        this.apexProfiling = apexProfiling;
    }

    @JsonProperty("Callout")
    public String getCallout() {
        return callout;
    }

    @JsonProperty("Callout")
    public void setCallout(String callout) {
        this.callout = callout;
    }

    @JsonProperty("Database")
    public String getDatabase() {
        return database;
    }

    @JsonProperty("Database")
    public void setDatabase(String database) {
        this.database = database;
    }

    @JsonProperty("DebugLevelId")
    public String getDebugLevelId() {
        return debugLevelId;
    }

    @JsonProperty("DebugLevelId")
    public void setDebugLevelId(String debugLevelId) {
        this.debugLevelId = debugLevelId;
    }

    @JsonProperty("ExpirationDate")
    public String getExpirationDate() {
        return expirationDate;
    }

    @JsonProperty("ExpirationDate")
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @JsonProperty("LogType")
    public String getLogType() {
        return logType;
    }

    @JsonProperty("LogType")
    public void setLogType(String logType) {
        this.logType = logType;
    }

    @JsonProperty("StartDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("StartDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("TracedEntityId")
    public String getTracedEntityId() {
        return tracedEntityId;
    }

    @JsonProperty("TracedEntityId")
    public void setTracedEntityId(String tracedEntityId) {
        this.tracedEntityId = tracedEntityId;
    }

    @JsonProperty("Validation")
    public String getValidation() {
        return validation;
    }

    @JsonProperty("Validation")
    public void setValidation(String validation) {
        this.validation = validation;
    }

    @JsonProperty("Visualforce")
    public String getVisualforce() {
        return visualforce;
    }

    @JsonProperty("Visualforce")
    public void setVisualforce(String visualforce) {
        this.visualforce = visualforce;
    }

    @JsonProperty("Workflow")
    public String getWorkflow() {
        return workflow;
    }

    @JsonProperty("Workflow")
    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

}
