package com.twb3.service;

import com.twb3.model.salesforce.rest.request.BaseSObjectRequest;
import com.twb3.model.salesforce.rest.response.DeleteResponse;
import com.twb3.model.salesforce.rest.response.GetResponse;
import com.twb3.model.salesforce.rest.response.PostResponse;

public interface BaseRestObjectService {
    String API_VERSION = "v44.0";
    String TOOLING_CONNECTION_ENDPOINT = "/services/data/" + API_VERSION + "/tooling/sobjects";


    GetResponse doGet(String id, int retryCount);
    PostResponse doPost(BaseSObjectRequest baseSObjectRequest, int retryCount);
    DeleteResponse doDelete(int retryCount);
}
