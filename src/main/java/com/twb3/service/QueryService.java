package com.twb3.service;

import com.twb3.Org;
import com.twb3.manager.AuthManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class QueryService {
    private final static transient Logger logger = LoggerFactory.getLogger(QueryService.class);

    // TODO: Make API_VERSION configurable via environment variable
    private final String API_VERSION = "v44.0";
    private final String CONNECTION_ENDPOINT = "/services/data/" + API_VERSION + "/query/?q=";
    private final String TOOLING_CONNECTION_ENDPOINT = "/services/data/" + API_VERSION + "/tooling/query/?q=";
    private boolean isTooling;
    private Org org;

    public QueryService(Org org, boolean isTooling) {
        this.org = org;
        this.isTooling = isTooling;
    }

    public String doQuery(String queryString, int retryCount) {
        logger.debug("Query before encode: {}", queryString);
        String encodedQueryString = encodeQueryString(queryString);

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet;
        if (isTooling) {
            logger.debug("Setting tooling api query endpoint: {}", TOOLING_CONNECTION_ENDPOINT);
            httpGet = new HttpGet(org.getInstanceUrl() + TOOLING_CONNECTION_ENDPOINT + encodedQueryString);
        } else {
            logger.debug("Setting api query endpoint: {}", CONNECTION_ENDPOINT);
            httpGet = new HttpGet(org.getInstanceUrl() + CONNECTION_ENDPOINT + encodedQueryString);
        }
        httpGet.addHeader("Authorization", "Bearer " + org.getAccessToken());
        try {
            logger.debug("Performing query on org {}: {}", org.getName(), encodedQueryString);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 401) {
                logger.debug("Received a 401 response code from Salesforce.  Will now reauthorize. Number of tries left: {}",
                        retryCount);
                AuthManager.refreshToken(org);
                if (retryCount < 1) {
                    throw new RuntimeException("Retry count exceeded.  Failed to connect.");
                }
                return doQuery(queryString, --retryCount);
            }
            HttpEntity httpEntity = httpResponse.getEntity();
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            throw new RuntimeException("An unexpected error has occurred while attempting to query the target org.", e);
        }
    }

    private String encodeQueryString(String queryString) {
        try {
            logger.debug("Encoding URL with standard charset {}", StandardCharsets.UTF_8);
            return URLEncoder.encode(queryString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding", e);
        }
    }
}
