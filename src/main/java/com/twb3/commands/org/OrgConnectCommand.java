package com.twb3.commands.org;

import com.twb3.manager.AuthManager;
import com.twb3.manager.OrgManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "connect", description = "Initializes the connection to Salesforce.")
public class OrgConnectCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        AuthManager.initialize(OrgManager.getSelectedOrg());
        return 0;
    }
}
