package com.twb3.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_code",
        "device_code",
        "interval",
        "verification_uri"
})
public class DeviceAuthenticationResponse {

    @JsonProperty("user_code")
    private String userCode;
    @JsonProperty("device_code")
    private String deviceCode;
    @JsonProperty("interval")
    private Integer interval;
    @JsonProperty("verification_uri")
    private String verificationUri;

    @JsonProperty("user_code")
    public String getUserCode() {
        return userCode;
    }

    @JsonProperty("user_code")
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @JsonProperty("device_code")
    public String getDeviceCode() {
        return deviceCode;
    }

    @JsonProperty("device_code")
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @JsonProperty("interval")
    public Integer getInterval() {
        return interval;
    }

    @JsonProperty("interval")
    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    @JsonProperty("verification_uri")
    public String getVerificationUri() {
        return verificationUri;
    }

    @JsonProperty("verification_uri")
    public void setVerificationUri(String verificationUri) {
        this.verificationUri = verificationUri;
    }

}
