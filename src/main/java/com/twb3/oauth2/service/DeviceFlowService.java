package com.twb3.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twb3.Org;
import com.twb3.manager.OrgManager;
import com.twb3.oauth2.domain.DeviceAuthenticationResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeviceFlowService {
    private final static transient Logger logger = LoggerFactory.getLogger(DeviceFlowService.class);

    private final Org org;
    private String salesforceSubDomain;

    public DeviceFlowService(Org org) {
        this.org = org;

        if (org.getIsProduction()) {
            this.salesforceSubDomain = "login";
        } else {
            this.salesforceSubDomain = "test";
        }
    }

    public void doGet() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://" + salesforceSubDomain + ".salesforce.com/services/oauth2/token");

        List<NameValuePair> params = new ArrayList<>(2);
        params.add(new BasicNameValuePair("response_type", "device_code"));
        params.add(new BasicNameValuePair("client_id", org.getClientId()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                ObjectMapper objectMapper = new ObjectMapper();

                String response = EntityUtils.toString(httpEntity);
                DeviceAuthenticationResponse deviceAuthenticationResponse = objectMapper.readValue(response, DeviceAuthenticationResponse.class);
                logger.info("Follow the link below and enter your code: " + deviceAuthenticationResponse.getUserCode() + " to activate sflog with org.");
                logger.info(deviceAuthenticationResponse.getVerificationUri());

                PollingTokenService pollingTokenService = new PollingTokenService(org);
                pollingTokenService.setDeviceCode(deviceAuthenticationResponse.getDeviceCode());
                pollingTokenService.setInterval(deviceAuthenticationResponse.getInterval());
                if (pollingTokenService.poll()) {
                    org.setAccessToken(pollingTokenService.getPollingSuccessResponse().getAccessToken());
                    org.setInstanceUrl(pollingTokenService.getPollingSuccessResponse().getInstanceUrl());
                    org.setRefreshToken(pollingTokenService.getPollingSuccessResponse().getRefreshToken());
                    OrgManager.updateOrg(org);
                    logger.info("Access token saved. You are now authenticated to use the Salesforce REST API with " + org.getName());
                } else {
                    System.out.println("Fail");
                    System.exit(1);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
