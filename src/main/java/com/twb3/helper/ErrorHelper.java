package com.twb3.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twb3.model.salesforce.rest.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ErrorHelper {
    private final static transient Logger logger = LoggerFactory.getLogger(ErrorHelper.class);

    /**
     * Reads the error responses returned by Salesforce and logs them at
     * error level
     *
     * @param response is the json string of the error response returned
     *                 by Salesforce
     */
    public static void captureRestError(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse[] errorResponses;
        try {
            errorResponses = objectMapper.readValue(response, ErrorResponse[].class);
            for (ErrorResponse errorResponse : errorResponses) {
                logger.error("{}:\n {}", errorResponse.getMessage(), errorResponse.getErrorCode());
            }
        } catch (IOException e) {
            throw new RuntimeException("An unexpected error has occurred!", e);
        }
    }
}
