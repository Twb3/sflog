package com.twb3.commands.traceflag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.twb3.Org;
import com.twb3.enums.SObjectTypes;
import com.twb3.helper.ErrorHelper;
import com.twb3.manager.OrgManager;
import com.twb3.model.salesforce.rest.response.SObjectQueryResponse;
import com.twb3.service.NameToIdService;
import com.twb3.service.QueryService;
import com.twb3.service.TraceFlagObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "delete", description = "Delete a trace-flag.")
public class TraceFlagDeleteCommand implements Callable<Integer> {
    private final static transient Logger logger = LoggerFactory.getLogger(TraceFlagDeleteCommand.class);

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    Exclusive exclusive;

    static class Exclusive {
        @CommandLine.Option(names = {"-c", "--apex-class"}, description = "Delete a trace flag for an ApexClass by class name", required = true)
        public static String apexClass;

        @CommandLine.Option(names = {"-t", "--apex-trigger"}, description = "Delete a trace flag for an ApexTrigger by trigger name", required = true)
        public static String apexTrigger;

        @CommandLine.Option(names = {"-u", "--username"}, description = "Delete a trace flag for a User by username", required = true)
        public static String username;
    }

    @Override
    public Integer call() {
        ObjectMapper objectMapper = new ObjectMapper();
        Org selectedOrg = OrgManager.getSelectedOrg();
        NameToIdService nameToIdService = new NameToIdService(selectedOrg, false);
        QueryService toolingQueryService = new QueryService(selectedOrg, true);
        String traceEntityId = null;


        if (Exclusive.apexClass != null) {
            traceEntityId = nameToIdService.doNameToIdQuery(Exclusive.apexClass, SObjectTypes.APEX_CLASS);
        } else if (Exclusive.apexTrigger != null) {
            traceEntityId = nameToIdService.doNameToIdQuery(Exclusive.apexTrigger, SObjectTypes.APEX_TRIGGER);
        } else if (Exclusive.username != null) {
            traceEntityId = nameToIdService.doNameToIdQuery(Exclusive.username, SObjectTypes.USER);
        }

        if (traceEntityId == null) {
            return 1;
        }

        String queryResponse = toolingQueryService.doQuery(
                "SELECT Id, TracedEntityId FROM TraceFlag WHERE TracedEntityId = '" + traceEntityId + "'", 3);
        SObjectQueryResponse sObjectQueryResponse = new SObjectQueryResponse();
        try {
            sObjectQueryResponse = objectMapper.readValue(queryResponse, SObjectQueryResponse.class);
            if (sObjectQueryResponse == null || sObjectQueryResponse.getRecords().size() == 0) {
                logger.info("No trace-flag to delete.");
                return 1;
            }
        } catch (MismatchedInputException mmie) {
            ErrorHelper.captureRestError(queryResponse);
        } catch (IOException e) {
            throw new RuntimeException("An unexpected error has occurred!", e);
        }

        TraceFlagObjectService traceFlagObjectService = new TraceFlagObjectService(OrgManager.getSelectedOrg());
        // there should only ever be one record
        if (traceFlagObjectService.doDelete(sObjectQueryResponse.getRecords().get(0).getId(), 3)) {
            logger.info("Trace-flag deleted.");
        }
        return 0;
    }
}
