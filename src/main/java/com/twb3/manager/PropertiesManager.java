package com.twb3.manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PropertiesManager {
    private final static transient Logger logger = LoggerFactory.getLogger(PropertiesManager.class);


    private static final String PROPERTIES_DIRECTORY_NAME = ".sflog";
    private static final String ORG_PROPERTIES_FILE_NAME = "orgs.json";
    private static final String HOME_PATH = System.getProperty("user.home");


    public static void createPropertiesDirectory() {
        try {
            Files.createDirectories(getPropertiesDirectoryPath());
            logger.debug("Properties directory created at {}", getPropertiesDirectoryPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create properties directory.", e);
        }
    }

    public static void createOrgProperties() {
        try {
            Files.createFile(getOrgPropertiesPath());
            logger.debug("Org properties file created at {}", getOrgPropertiesPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create org properties.", e);
        }
    }

    public static Path getOrgPropertiesPath() {
        return Paths.get(HOME_PATH, PROPERTIES_DIRECTORY_NAME, ORG_PROPERTIES_FILE_NAME);
    }

    public static Path getPropertiesDirectoryPath() {
        return Paths.get(HOME_PATH, PROPERTIES_DIRECTORY_NAME);
    }

}
