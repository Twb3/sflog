package com.twb3.commands.org;

import com.twb3.manager.OrgManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "add", description = "Adds an org to the list of actionable orgs.")
public class OrgAddCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        OrgManager.addOrg();
        return 0;
    }
}
