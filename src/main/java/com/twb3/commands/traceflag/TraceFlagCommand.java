package com.twb3.commands.traceflag;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "trace-flag", description = "Trace flag related commands.",
        subcommands = {
                TraceFlagSetCommand.class,
                TraceFlagDeleteCommand.class
        }
)
public class TraceFlagCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        return 0;
    }
}
