package com.twb3.service;

import com.twb3.Org;
import com.twb3.manager.AuthManager;
import com.twb3.model.salesforce.rest.request.BaseSObjectRequest;
import com.twb3.model.salesforce.rest.response.DeleteResponse;
import com.twb3.model.salesforce.rest.response.GetResponse;
import com.twb3.model.salesforce.rest.response.PostResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ApexLogObjectService implements BaseRestObjectService {
    private final static transient Logger logger = LoggerFactory.getLogger(ApexLogObjectService.class);
    private final String OBJECT_ENDPOINT_PART = "/ApexLog/";
    private final String BODY_ENDPOINT_PART = "/Body";

    private Org org;

    public ApexLogObjectService(Org org) {
        this.org = org;
    }

    public String doBodyGet(String id, int retryCount) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(org.getInstanceUrl() + TOOLING_CONNECTION_ENDPOINT +
                OBJECT_ENDPOINT_PART + id + BODY_ENDPOINT_PART);
        httpGet.addHeader("Authorization", "Bearer " + org.getAccessToken());
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 401) {
                logger.debug("Received a 401 response code from Salesforce.  Will now reauthorize. Number of tries left: {}",
                        retryCount);
                AuthManager.refreshToken(org);
                if (retryCount < 1) {
                    throw new RuntimeException("Retry count exceeded.  Failed to connect.");
                }
                return doBodyGet(id, --retryCount);
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public GetResponse doGet(String id, int retryCount) {
        return null;
    }

    @Override
    public PostResponse doPost(BaseSObjectRequest baseSObjectRequest, int retryCount) {
        return null;
    }

    @Override
    public DeleteResponse doDelete(int retryCount) {
        return null;
    }
}
