package com.twb3.oauth2.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twb3.Org;
import com.twb3.oauth2.domain.ErrorResponse;
import com.twb3.oauth2.domain.PollingSuccessResponse;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class PollingTokenService {
    private final static transient Logger logger = LoggerFactory.getLogger(PollingTokenService.class);

    private final Org org;
    private String salesforceSubDomain;
    private String deviceCode;
    private Integer interval;
    private PollingSuccessResponse pollingSuccessResponse;

    PollingTokenService(Org org) {
        this.org = org;

        if (org.getIsProduction()) {
            this.salesforceSubDomain = "login";
        } else {
            this.salesforceSubDomain = "test";
        }
    }

    void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    void setInterval(Integer interval) {
        this.interval = interval;
    }

    PollingSuccessResponse getPollingSuccessResponse() {
        return pollingSuccessResponse;
    }

    boolean poll() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        Runnable pollingTask = () -> {
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://" + salesforceSubDomain + ".salesforce.com/services/oauth2/token");

            List<NameValuePair> params = new ArrayList<>(3);
            params.add(new BasicNameValuePair("grant_type", "device"));
            params.add(new BasicNameValuePair("client_id", org.getClientId()));
            params.add(new BasicNameValuePair("code", deviceCode));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                if (httpEntity != null) {
                    ObjectMapper objectMapper = new ObjectMapper();

                    String response = EntityUtils.toString(httpEntity);
                    try {
                        pollingSuccessResponse = objectMapper.readValue(response, PollingSuccessResponse.class);
                        if (pollingSuccessResponse != null) {
                            executorService.shutdown();
                        }
                    } catch (JsonMappingException jme) {
                        ErrorResponse errorResponse = objectMapper.readValue(response, ErrorResponse.class);
                        logger.debug("{}: {}", errorResponse.getError(), errorResponse.getErrorDescription());
                    } catch (JsonParseException jpe) {
                        logger.error("There was an issue connecting to salesforce.  This is likely because their services are down.");
                        System.exit(1);
                    } catch (Exception e) {
                        logger.error("An unexpected error has occurred: {}", e.getMessage());
                        System.exit(1);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        executorService.scheduleAtFixedRate(pollingTask, 3000, (interval * 1000) + 500, TimeUnit.MILLISECONDS);

        while (true) {
            if (executorService.isShutdown()) {
                return true;
            }
        }
    }
}
