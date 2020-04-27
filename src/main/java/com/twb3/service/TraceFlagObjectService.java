package com.twb3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.twb3.Org;
import com.twb3.helper.ErrorHelper;
import com.twb3.manager.AuthManager;
import com.twb3.model.salesforce.rest.request.TraceFlagRequest;
import com.twb3.model.salesforce.rest.request.BaseSObjectRequest;
import com.twb3.model.salesforce.rest.response.GetResponse;
import com.twb3.model.salesforce.rest.response.PostResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TraceFlagObjectService implements BaseRestObjectService {
    private final static transient Logger logger = LoggerFactory.getLogger(TraceFlagObjectService.class);
    private final String OBJECT_ENDPOINT_PART = "/TraceFlag/";
    private Org org;

    public TraceFlagObjectService(Org org) {
        this.org = org;
    }

    @Override
    public GetResponse doGet(String id, int retryCount) {
        return null;
    }

    @Override
    public PostResponse doPost(BaseSObjectRequest request, int retryCount) {
        assert request instanceof TraceFlagRequest;
        ObjectMapper objectMapper = new ObjectMapper();

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(org.getInstanceUrl() + TOOLING_CONNECTION_ENDPOINT + OBJECT_ENDPOINT_PART);
        httpPost.addHeader("Authorization", "Bearer " + org.getAccessToken());
        try {
            logger.debug(objectMapper.writeValueAsString(request));
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(request), ContentType.APPLICATION_JSON));
            logger.debug("Creating trace-flag in org {}", org.getName());
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 401) {
                logger.debug("Received a 401 response code from Salesforce.  Will now reauthorize. Number of tries left: {}",
                        retryCount);
                AuthManager.refreshToken(org);
                if (retryCount < 1) {
                    throw new RuntimeException("Retry count exceeded.  Failed to connect.");
                }
                return doPost(request, --retryCount);
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity);
            try {
                return objectMapper.readValue(response, PostResponse.class);
            } catch (MismatchedInputException mmie) {
                ErrorHelper.captureRestError(response);
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException("An unexpected error has occurred while creating trace-flag in target org.", e);
        }
    }

    @Override
    public boolean doDelete(String id, int retryCount) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(org.getInstanceUrl() + TOOLING_CONNECTION_ENDPOINT + OBJECT_ENDPOINT_PART + id);
        httpDelete.addHeader("Authorization", "Bearer " + org.getAccessToken());
        try {
            logger.debug("Deleting trace-flag with id {} in org {}", id, org.getName());
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            if (httpResponse.getStatusLine().getStatusCode() == 401) {
                logger.debug("Received a 401 response code from Salesforce.  Will now reauthorize. Number of tries left: {}",
                        retryCount);
                AuthManager.refreshToken(org);
                if (retryCount < 1) {
                    throw new RuntimeException("Retry count exceeded.  Failed to connect.");
                }
                return doDelete(id, --retryCount);
            }
            if (httpResponse.getStatusLine().getStatusCode() == 204) {
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException("An unexpected error has occurred while deleting trace-flag in target org.", e);
        }
        return false;
    }
}
