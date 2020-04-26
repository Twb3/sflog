package com.twb3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twb3.Org;
import com.twb3.model.salesforce.rest.response.SObjectQueryResponse;
import com.twb3.enums.SObjectTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NameToIdService extends QueryService {
    private final static transient Logger logger = LoggerFactory.getLogger(NameToIdService.class);

    public NameToIdService(Org org, boolean isTooling) {
        super(org, isTooling);
    }

    public String doNameToIdQuery(String name, SObjectTypes sObjectType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (sObjectType.equals(SObjectTypes.APEX_CLASS)) {
                SObjectQueryResponse apexClass = objectMapper.readValue(super.doQuery(
                        "SELECT Id, Name FROM ApexClass WHERE Name = '" + name + "'", 3),
                        SObjectQueryResponse.class);
                if (apexClass == null || apexClass.getRecords().size() == 0) {
                    logger.error("Apex class {} not found.", name);
                    return null;
                }
                return apexClass.getRecords().get(0).getId();
            } else if (sObjectType.equals(SObjectTypes.APEX_TRIGGER)) {
                SObjectQueryResponse apexTrigger = objectMapper.readValue(super.doQuery(
                        "SELECT Id, Name FROM ApexTrigger WHERE Name = '" + name + "'", 3),
                        SObjectQueryResponse.class);
                if (apexTrigger == null || apexTrigger.getRecords().size() == 0) {
                    logger.error("Apex trigger {} not found.", name);
                    return null;
                }
                return apexTrigger.getRecords().get(0).getId();
            } else if (sObjectType.equals(SObjectTypes.USER)) {
                SObjectQueryResponse user = objectMapper.readValue(super.doQuery(
                        "SELECT Id, Name FROM User WHERE Username = '" + name + "'", 3),
                        SObjectQueryResponse.class);
                if (user == null || user.getRecords().size() == 0) {
                    logger.error("User with username {} not found.", name);
                    return null;
                }
                return user.getRecords().get(0).getId();
            } else if (sObjectType.equals(SObjectTypes.DEBUG_LEVEL)) {
                SObjectQueryResponse debugLevel = objectMapper.readValue(super.doQuery(
                        "SELECT Id, DeveloperName FROM DebugLevel WHERE DeveloperName = '" + name + "'", 3),
                        SObjectQueryResponse.class);
                if (debugLevel == null || debugLevel.getRecords().size() == 0) {
                    logger.error("User with username {} not found.", name);
                    return null;
                }
                return debugLevel.getRecords().get(0).getId();
            }
        } catch (IOException e) {
            // TODO: Handle exceptions better
            throw new RuntimeException("An unexpected error occurred!", e);
        }
        return null;
    }
}
