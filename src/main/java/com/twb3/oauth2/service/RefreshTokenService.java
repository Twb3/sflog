package com.twb3.oauth2.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twb3.Org;
import com.twb3.manager.OrgManager;
import com.twb3.oauth2.domain.RefreshTokenResponse;
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

public class RefreshTokenService {
    private final static transient Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    private final Org org;
    private String salesforceSubDomain;

    public RefreshTokenService(Org org) {
        this.org = org;

        if (org.getIsProduction()) {
            this.salesforceSubDomain = "login";
        } else {
            this.salesforceSubDomain = "test";
        }
    }

    public void doRefresh() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://" + salesforceSubDomain + ".salesforce.com/services/oauth2/token");
        List<NameValuePair> params = new ArrayList<>(4);

        params.add(new BasicNameValuePair("grant_type", "refresh_token"));
        params.add(new BasicNameValuePair("refresh_token", org.getRefreshToken()));
        params.add(new BasicNameValuePair("client_id", org.getClientId()));
        params.add(new BasicNameValuePair("client_secret", org.getClientSecret()));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            if (httpEntity != null) {
                ObjectMapper objectMapper = new ObjectMapper();

                String response = EntityUtils.toString(httpEntity);
                try {
                    RefreshTokenResponse refreshTokenResponse = objectMapper.readValue(response, RefreshTokenResponse.class);
                    org.setAccessToken(refreshTokenResponse.getAccessToken());
                    org.setInstanceUrl(refreshTokenResponse.getInstanceUrl());
                    OrgManager.updateOrg(org);
                } catch (JsonMappingException jme) {
                    logger.error("Refresh token not valid.  Run `org connect` to reinitialize the selected org connection.");
                    System.exit(1);
                } catch (JsonParseException jpe) {
                    logger.error("There was an issue connecting to salesforce.  This is likely because their services are down.");
                    System.exit(1);
                } catch (Exception e) {
                    logger.error("An unexpected error has occurred: {}", e.getMessage());
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            logger.error("Http connection failed: {}", e.getMessage());
        }
    }
}
