package com.twb3.commands.logs;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "logs", description = "Log related commands.",
        subcommands = {
                LogsGetCommand.class
        }
)
public class LogsCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        return 0;
    }
}
