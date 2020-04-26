package com.twb3.commands.org;

import com.twb3.Org;
import com.twb3.manager.OrgManager;
import com.twb3.manager.AuthManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;


@CommandLine.Command(name = "use", description = "Selects an org to use.")
public class OrgUseCommand implements Callable<Integer> {
    private final static transient Logger logger = LoggerFactory.getLogger(OrgUseCommand.class);


    @CommandLine.Option(names = {"-n", "--name"}, required = true)
    private String name;

    @Override
    public Integer call() {
        OrgManager.setSelectedOrg(name);
        Org selectedOrg = OrgManager.getSelectedOrg();
        if (selectedOrg.getInstanceUrl() == null || selectedOrg.getAccessToken() == null || selectedOrg.getRefreshToken() == null) {
            logger.debug("Org {} is not connected.  Executing connection steps.", selectedOrg.getName());
            AuthManager.initialize(selectedOrg);
        }
        return 0;
    }
}
