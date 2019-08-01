package eestec.thessaloniki.palermo.game_logic;

import eestec.thessaloniki.palermo.game_logic.roles.Role;
import eestec.thessaloniki.palermo.game_logic.roles.Roles;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

@ApplicationScoped
public class ReadConfiguration {

    private List<String[]> linesInConfiguration;
    private boolean wasAbleToParse;

    @Inject
    private Roles roles;

    public ReadConfiguration() {
        this.linesInConfiguration = new ArrayList<>();
        wasAbleToParse=true;
    }

    @PostConstruct
    private void init() {
        System.out.println("Starting initialiaze readConfigurations");
        InputStream input = getClass().getClassLoader().getResourceAsStream("roles.txt");
        BufferedReader bf = new BufferedReader(new InputStreamReader(input));
        String t = "";
        String[] line;
        try {
            while ((t = bf.readLine()) != null) {
                line = t.split(",");
                checkConfigurationLine(line);
                linesInConfiguration.add(line);
            }
        } catch (IOException x) {
            wasAbleToParse=false;
        }

    }

    private void checkConfigurationLine(String[] line) {
        try {
            int numberOfPlayers = Integer.valueOf(line[0]);

        } catch (Exception e) {
            System.err.println("Error with parsing file configuration, first element is not an integer");
            wasAbleToParse=false;
        }
        boolean flag = false;
        for (int i = 1; i < line.length; i++) {

            for (Role role : roles.getRoles()) {
                if (line[i].equals(role.getRoleName())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                System.err.println("Error with parsing file configuration, " + line[i] + " is not a valid  role");
                wasAbleToParse=false;
            }
        }

    }

    public String[] getStringBasedOnNumberOfPlayers(int numberOfPlayers) {
        if(!wasAbleToParse){
            return null;
        }
        for (String[] s : linesInConfiguration) {
            if (Integer.valueOf(s[0]).equals(numberOfPlayers)) {
                return s;
            }
        }
        return null;
    }

}
