package com.twb3.commands.org;

import com.twb3.manager.OrgManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "remove", description = "Removes an org from the list of actionable orgs.")
public class OrgRemoveCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        OrgManager.removeOrg();
        return 0;
    }
}
