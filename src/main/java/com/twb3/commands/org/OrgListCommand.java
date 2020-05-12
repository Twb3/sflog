package com.twb3.commands.org;

import com.twb3.manager.OrgManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "list", description = "Lists orgs.")
public class OrgListCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        OrgManager.listOrgs();
        return 0;
    }
}
