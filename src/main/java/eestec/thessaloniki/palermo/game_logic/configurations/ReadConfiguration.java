package eestec.thessaloniki.palermo.game_logic.configurations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReadConfiguration {

    private List<String[]> linesInConfiguration;

    public ReadConfiguration() {
        this.linesInConfiguration = new ArrayList<>();
    }
    
    @PostConstruct
    private void init() {
        System.out.println("Starting initialiaze readConfigurations");
        InputStream input =getClass().getClassLoader().getResourceAsStream("roles.txt");
        BufferedReader bf = new BufferedReader(new InputStreamReader(input));
        String t = "";
        try {
            while ((t = bf.readLine()) != null) {
                    System.out.println(t);
                    linesInConfiguration.add(t.split(","));
            }
        } catch (IOException x) {

        }

    }

    public String[] getStringBasedOnNumberOfPlayers(int numberOfPlayers) {
        for (String[] s : linesInConfiguration) {
            if (Integer.valueOf(s[0]).equals(numberOfPlayers)) {
                return s;
            }
        }
        return null;
    }

}
