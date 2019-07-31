package eestec.thessaloniki.palermo.game_logic;

import eestec.thessaloniki.palermo.game_logic.configurations.ReadConfiguration;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;

public class GiveRoles {

    @Inject
    private ReadConfiguration readConfiGuration;

    @Inject
    UserToGameService userToGameService;

    public boolean giveRoles(int game_id) {
        List<Integer> users_id = userToGameService.usersInGame(game_id);
        String[] roles = readConfiGuration.getStringBasedOnNumberOfPlayers(users_id.size());

        return this.giveRoleToPlayers(roles, users_id);
    }

    private boolean giveRoleToPlayers(String[] rolesFromConfiguration, List<Integer> users_id) {
        System.out.println("I want to give roles, these are the roles...");
        Random rand = new Random();
        Collections.shuffle(users_id, new Random(rand.nextInt()));
        UserToGame userToGame;
        int counter = 1;
        try {
            for (int i : users_id) {
                userToGame = userToGameService.findByUserId(i);
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
