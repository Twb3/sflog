package com.twb3;

import com.twb3.commands.ConfigureCommand;
import com.twb3.commands.logs.LogsCommand;
import com.twb3.commands.org.OrgCommand;
import com.twb3.commands.traceflag.TraceFlagCommand;
import picocli.CommandLine;

import java.util.concurrent.Callable;


@CommandLine.Command(name = "EntryCommand",
        description = "Description",
        version = "1.0",
        subcommands = {
                OrgCommand.class,
                ConfigureCommand.class,
                LogsCommand.class,
                TraceFlagCommand.class
        }
)
public class EntryCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        return 0;
    }

    public static void main(String... args) {
        System.exit(new CommandLine(new EntryCommand()).execute(args));
    }
}
