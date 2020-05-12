package com.twb3.commands.org;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "org",
        description = "Org related commands.",
        subcommands = {
                OrgAddCommand.class,
                OrgRemoveCommand.class,
                OrgUseCommand.class,
                OrgConnectCommand.class,
                OrgListCommand.class
        }
)
public class OrgCommand implements Callable<Integer> {

        @Override
        public Integer call() {
                return 0;
        }
}
