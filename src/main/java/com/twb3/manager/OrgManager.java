package com.twb3.manager;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.twb3.Org;
import com.twb3.exception.ConfigurationNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twb3.exception.NoSelectedOrgException;
import com.twb3.exception.OrgNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrgManager {
    private final static transient Logger logger = LoggerFactory.getLogger(OrgManager.class);

    
    public static void updateOrg(Org org) {
        List<Org> orgs = loadOrgs();

        if (orgs.stream().noneMatch(org1 -> org1.getName().equals(org.getName()))) {
            logger.error("There are no orgs matching that name.");
            System.exit(1);
        }

        for (int i = 0; i < orgs.size(); i++) {
            if (orgs.get(i).getName().equals(org.getName())) {
                logger.debug("Updating org {} with new values.", org.getName());
                orgs.set(i, org);
                break;
            }
        }

        saveOrgs(orgs);
    }

    public static void addOrg() {
        List<Org> orgs;
        Org newOrg = new Org();

        orgs = loadOrgs();

        Scanner userInput = new Scanner(System.in);
        while(true) {
            System.out.print("Unique name for this org (this will be used for tailing): ");
            String name = userInput.nextLine().trim();
            if (orgs.stream().noneMatch(org -> org.getName().equals(name))) {
                newOrg.setName(name);
                break;
            }
            System.out.println("You already used this name.");
        }
        System.out.print("Client Id: ");
        newOrg.setClientId(userInput.nextLine());
        System.out.print("Client Secret: ");
        newOrg.setClientSecret(userInput.nextLine());
        System.out.print("Is Production? (true or false): ");
        while (!userInput.hasNextBoolean()) {
            System.out.print("Is Production? (true or false): ");
            userInput.next();
        }
        newOrg.setIsProduction(userInput.nextBoolean());
        orgs.add(newOrg);

        saveOrgs(orgs);
        logger.info(newOrg.getName() + " successfully added!");
    }

    public static void removeOrg() {
        List<Org> orgs = loadOrgs();

        Scanner userInput = new Scanner(System.in);
        System.out.print("Unique name of org to remove: ");
        String orgToRemove = userInput.nextLine().trim();

        if (orgs.removeIf(org -> org.getName().equalsIgnoreCase(orgToRemove))) {
            saveOrgs(orgs);
        } else {
            logger.error(orgToRemove + " does not exist.");
            System.exit(1);
        }
    }

    public static void setSelectedOrg(String name) {
        List<Org> orgs = loadOrgs();

        if (orgs.stream().noneMatch(org1 -> org1.getName().equals(name))) {
            logger.error("Org name {} not found.", name);
            System.exit(1);
        }

        for (Org org : orgs) {
            if (org.getSelected()) {
                logger.debug("Unsetting org {} as selected org.", org.getName());
                org.setSelected(false);
            }

            if (org.getName().equals(name)) {
                logger.debug("Setting org {} as selected org.", org.getName());
                org.setSelected(true);
            }
        }
        saveOrgs(orgs);
        logger.info("Org {} is now selected.", name);
    }

    public static Org getSelectedOrg() {
        logger.debug("Getting the selected org from the configuration file orgs.json.");
        return loadOrgs().stream().filter(Org::getSelected).findFirst().orElseThrow(
                () ->  new NoSelectedOrgException("No orgs selected.  Use `org use` to select an org.", "")
        );
    }

    private static void saveOrgs(List<Org> orgs) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            logger.debug("Saving orgs to the configuration file orgs.json.");
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(String.valueOf(PropertiesManager.getOrgPropertiesPath())), orgs);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static List<Org> loadOrgs() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(String.valueOf(PropertiesManager.getOrgPropertiesPath()));
        try {
            logger.debug("Loading orgs from the configuration file orgs.json.");
            return objectMapper.readValue(file, new TypeReference<List<Org>>() {});
        } catch (FileNotFoundException fnfe) {
            throw new ConfigurationNotFoundException("Configuration file not found.  Have you configured the application?", fnfe.getMessage());
        } catch (MismatchedInputException mie) {
            logger.debug("This is the first org to be created.");
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file.  This should not happen.", e);
        }
    }
}
