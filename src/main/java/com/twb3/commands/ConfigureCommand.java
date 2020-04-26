package com.twb3.commands;

import com.twb3.manager.PropertiesManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "configure", description = "Configures the CLI.")
public class ConfigureCommand implements Callable<Integer> {

    @Override
    public Integer call() {
        PropertiesManager.createPropertiesDirectory();
        PropertiesManager.createOrgProperties();
        return 0;
    }
}
