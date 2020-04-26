package com.twb3.commands.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.twb3.Org;
import com.twb3.enums.SObjectTypes;
import com.twb3.helper.ErrorHelper;
import com.twb3.manager.OrgManager;
import com.twb3.model.salesforce.rest.response.Record;
import com.twb3.model.salesforce.rest.response.SObjectQueryResponse;
import com.twb3.service.ApexLogObjectService;
import com.twb3.service.NameToIdService;
import com.twb3.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "get", description = "Get logs")
public class LogsGetCommand implements Callable<Integer> {
    private final static transient Logger logger = LoggerFactory.getLogger(LogsGetCommand.class);

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    Exclusive exclusive;

    static class Exclusive {
        @CommandLine.Option(names = {"-a", "--all"}, description = "Get all logs", required = true)
        public static boolean all;

        @CommandLine.Option(names = {"-u", "--username"}, description = "Get all logs for a User by username", required = true)
        public static String username;

    }

    @Override
    public Integer call() {
        ObjectMapper objectMapper = new ObjectMapper();
        Org selectedOrg = OrgManager.getSelectedOrg();
        NameToIdService nameToIdService = new NameToIdService(selectedOrg, false);
        QueryService toolingQueryService = new QueryService(selectedOrg, true);
        String logUserId = null;

        /*
         First let's get the id of the user.
         Return exit code 1 if user is not found by name.
         */
        if (Exclusive.username != null) {
            logUserId = nameToIdService.doNameToIdQuery(Exclusive.username, SObjectTypes.USER);
            if (logUserId == null) {
                return 1;
            }
        }

        /*
         Second we are going to get all the Ids of the ApexLog records
         that correspond to our user's Id
         */
        String queryResponse = toolingQueryService.doQuery(
                "SELECT Id, LogUserId FROM ApexLog WHERE LogUserId = '" + logUserId + "'", 3);
        SObjectQueryResponse sObjectQueryResponse = new SObjectQueryResponse();
        try {
            sObjectQueryResponse = objectMapper.readValue(queryResponse, SObjectQueryResponse.class);
        } catch (MismatchedInputException mmie) {
            ErrorHelper.captureRestError(queryResponse);
        } catch (IOException e) {
            throw new RuntimeException("An unexpected error has occurred!", e);
        }

        for (Record record : sObjectQueryResponse.getRecords()) {
            ApexLogObjectService apexLogObjectService = new ApexLogObjectService(selectedOrg);
            logger.info(apexLogObjectService.doBodyGet(record.getId(), 3));

        }

        return 0;
    }
}
