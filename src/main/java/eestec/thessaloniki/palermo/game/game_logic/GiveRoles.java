package eestec.thessaloniki.palermo.game.game_logic;

import eestec.thessaloniki.palermo.game.ReadConfiguration;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class GiveRoles {

    @Inject
    private ReadConfiguration readConfiGuration;

    @Inject
    UserToGameService userToGameService;

    public boolean giveRoles(int game_id) {
        List<UserToGame> users_id = userToGameService.userToGameList(game_id);
        String[] roles = readConfiGuration.getStringBasedOnNumberOfPlayers(users_id.size());
        if(roles==null){ // in case something was wrong with the parsing
            return false;
        }
        return this.giveRoleToPlayers(roles, users_id);
    }

    private boolean giveRoleToPlayers(String[] rolesFromConfiguration, List<UserToGame> users_id) {
        System.out.println("I want to give roles, these are the roles...");
        Collections.shuffle(users_id, new SecureRandom());
        
        int counter = 1;
        try {
            for (UserToGame userToGame : users_id) {
                userToGame.setRole_type(rolesFromConfiguration[counter]);
                counter++;
                userToGameService.update(userToGame);
                System.out.println("User id: " + userToGame.getUser_id() + " got role: " + userToGame.getRole_type());
            }
            return true;
        } catch (Exception e) {
            System.err.println("Got error while giving roles");
            return false;
        }

    }

}
