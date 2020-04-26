package com.twb3.model.salesforce.rest.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "success",
        "errors",
        "warnings",
        "infos"
})
public class PostResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("errors")
    private List<Object> errors = null;
    @JsonProperty("warnings")
    private List<Object> warnings = null;
    @JsonProperty("infos")
    private List<Object> infos = null;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("errors")
    public List<Object> getErrors() {
        return errors;
    }

    @JsonProperty("errors")
    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    @JsonProperty("warnings")
    public List<Object> getWarnings() {
        return warnings;
    }

    @JsonProperty("warnings")
    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }

    @JsonProperty("infos")
    public List<Object> getInfos() {
        return infos;
    }

    @JsonProperty("infos")
    public void setInfos(List<Object> infos) {
        this.infos = infos;
    }
}
