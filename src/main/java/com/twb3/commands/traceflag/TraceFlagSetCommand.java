package com.twb3.commands.traceflag;

import com.twb3.Org;
import com.twb3.manager.OrgManager;
import com.twb3.enums.LogTypes;
import com.twb3.enums.SObjectTypes;
import com.twb3.model.salesforce.rest.request.TraceFlagRequest;
import com.twb3.service.NameToIdService;
import com.twb3.service.TraceFlagObjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "set", description = "Trace flag related commands.")
public class TraceFlagSetCommand implements Callable<Integer> {
    private final static transient Logger logger = LoggerFactory.getLogger(TraceFlagSetCommand.class);

    @CommandLine.Option(names = {"-l", "--debug-level"}, description = "Debug Level to apply to trace flag.")
    private String debugLevel;

    @CommandLine.ArgGroup(exclusive = true, multiplicity = "1")
    Exclusive exclusive;

    static class Exclusive {
        @CommandLine.Option(names = {"-c", "--apex-class"}, description = "Set a trace flag for an ApexClass by class name", required = true)
        public static String apexClass;

        @CommandLine.Option(names = {"-t", "--apex-trigger"}, description = "Set a trace flag for an ApexTrigger by trigger name", required = true)
        public static String apexTrigger;

        @CommandLine.Option(names = {"-u", "--username"}, description = "Set a trace flag for a User by username", required = true)
        public static String username;
    }

    @Override
    public Integer call() {
        Org selectedOrg = OrgManager.getSelectedOrg();
        NameToIdService nameToIdService = new NameToIdService(selectedOrg, false);
        NameToIdService toolingNameToIdService = new NameToIdService(selectedOrg, true);
        String traceEntityId;
        String debugLevelId;
        TraceFlagRequest traceFlag = null;

        if (debugLevel != null) {
            debugLevelId = toolingNameToIdService.doNameToIdQuery(debugLevel, SObjectTypes.DEBUG_LEVEL);
        } else {
            logger.debug("No debug level supplied.  Defaulting to SFDC_DevConsole.");
            debugLevelId = toolingNameToIdService.doNameToIdQuery("SFDC_DevConsole", SObjectTypes.DEBUG_LEVEL);
        }

        if (Exclusive.apexClass != null) {
            traceEntityId = nameToIdService.doNameToIdQuery(Exclusive.apexClass, SObjectTypes.APEX_CLASS);
            traceFlag = new TraceFlagRequest(traceEntityId, debugLevelId, LogTypes.CLASS_TRACING);
        } else if (Exclusive.apexTrigger != null) {
            traceEntityId = nameToIdService.doNameToIdQuery(Exclusive.apexTrigger, SObjectTypes.APEX_TRIGGER);
            traceFlag = new TraceFlagRequest(traceEntityId, debugLevelId, LogTypes.CLASS_TRACING);
        } else if (Exclusive.username != null) {
            traceEntityId = nameToIdService.doNameToIdQuery(Exclusive.username, SObjectTypes.USER);
            traceFlag = new TraceFlagRequest(traceEntityId, debugLevelId, LogTypes.USER_DEBUG);
        }

        if (traceFlag != null) {
            TraceFlagObjectService traceFlagObjectService = new TraceFlagObjectService(OrgManager.getSelectedOrg());
            traceFlagObjectService.doPost(traceFlag, 3);
        }

        return 0;
    }
}
