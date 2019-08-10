package eestec.thessaloniki.palermo.game.states;

import eestec.thessaloniki.palermo.game.game_logic.GameOver;
import eestec.thessaloniki.palermo.game.game_logic.WhoDied;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.List;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.core.Response;

/**
 *
 * This will be a class for doing the necessary things while changing between
 * the states
 */
public class ChangeStates {
    
    @Inject
    UserToGameService userToGameService;
    @Inject
    WhoDied whoDied;
    @Inject
    GameOver gameOver;

    /**
     *
     * @param state
     * @param leader
     * @return if everything went OK
     */
    public boolean changeStateTo(String state, UserToGame leader) {
        System.out.println("Changing state to " + state + " in changeStates class");
        switch (state) {
            case "Night":
                return this.changeToNight(leader);
            case "Morning":
                return this.changeToMorning(leader);
            default:
                return false;
        }
        
    }
    
    private boolean changeToNight(UserToGame leader) {
        try {
            gameOver.isGameOver(leader.getGame_id());
            userToGameService.initializeActedAtNight(leader.getGame_id());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean changeToMorning(UserToGame leader) {
        try {
            whoDied.kill(leader);
            gameOver.isGameOver(leader.getGame_id());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Response getDeadPeople(UserToGame userToGame) {
        try {
            List<UserToGame> users = userToGameService.getDeadPlayers(userToGame);
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            for (UserToGame utg : users) {
                jsonArray.add(utg.getUser_id());
            }
            return Response.ok(jsonArray.build()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    public Response getAllRoles(UserToGame userToGame) {
        try {
            List<UserToGame> users = userToGameService.userToGameList(userToGame.getGame_id());
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            for (UserToGame utg : users) {
                jsonArray.add(utg.getRole_type());
            }
            return Response.ok(jsonArray.build()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    public Response getDeadRoles(UserToGame userToGame) {
        try {
            List<UserToGame> users = userToGameService.getRolesDead(userToGame);
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            for (UserToGame utg : users) {
                jsonArray.add(Json.createObjectBuilder().add("user_id", utg.getUser_id()).add("role_type", utg.getRole_type()));
            }
            return Response.ok(jsonArray.build()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    public boolean hasGameEnded() {
        return false; // to do one that will return if the game has endeed
    }
    
}
