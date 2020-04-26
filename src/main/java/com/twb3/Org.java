package com.twb3;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "isProduction",
        "clientId",
        "clientSecret",
        "access_token",
        "instance_url",
        "refresh_token",
        "selected"
})
public class Org {

    @JsonProperty("name")
    private String name;
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientSecret")
    private String clientSecret;
    @JsonProperty("isProduction")
    private Boolean isProduction;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("instance_url")
    private String instanceUrl;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("selected")
    private boolean selected;

    @JsonProperty("name")
    public String getName() { return name; }

    @JsonProperty("name")
    public void setName(String name) { this.name = name; }

    @JsonProperty("clientId")
    public String getClientId() { return clientId; }

    @JsonProperty("clientId")
    public void setClientId(String clientId) { this.clientId = clientId; }

    @JsonProperty("clientSecret")
    public String getClientSecret() {  return clientSecret; }

    @JsonProperty("clientSecret")
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }

    @JsonProperty("isProduction")
    public Boolean getIsProduction() {
        return isProduction;
    }

    @JsonProperty("isProduction")
    public void setIsProduction(Boolean isProduction) {
        this.isProduction = isProduction;
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("instance_url")
    public String getInstanceUrl() {
        return instanceUrl;
    }

    @JsonProperty("instance_url")
    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @JsonProperty("selected")
    public boolean getSelected() {
        return selected;
    }

    @JsonProperty("selected")
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
