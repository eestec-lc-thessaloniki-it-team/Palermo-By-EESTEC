package eestec.thessaloniki.palermo.game_logic.roles;

import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class MurdererRole extends Role {

    @Inject
    UserService userService;

    @Inject
    UserToGameService userToGameService;

    public MurdererRole() {
        super();
        this.roleName = "Murderer";
        this.roleTeam = RoleTeam.BAD;
        this.description = "This is the worst role in the game. There is one more"
                + "in the game, that you have to team up in order to kill the "
                + "rest players.";
    }

    @Override
    public Response action(UserToGame userToGame,List<UserToGame> users) {
        //Morderer will suggest someone to be killed
        try {
            users.get(0).setVotesFromMurderers(users.get(0).getVotesFromMurderers() + 1);
            userToGameService.update(users.get(0));
            return Response.ok(this.getMurdererVotes(userToGame.getGame_id())).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Override
    public Response info(UserToGame userToGame) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try {
            jsonObjectBuilder.add("murdererVotes", this.getMurdererVotes(userToGame.getGame_id()));
            return Response.ok(jsonObjectBuilder.build()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

    }

    private JsonArray getMurderers(int game_id) {
        User u;
        JsonArrayBuilder jsonUsersBuilder = Json.createArrayBuilder();
        for (UserToGame utg : userToGameService.getAllPlayesOfType(this.roleName, game_id)) {
            u = userService.findUserById(utg.getUser_id());
            jsonUsersBuilder.add(Json.createObjectBuilder().add("user_id", u.getId()).add("username", u.getUsername()));
        }
        return jsonUsersBuilder.build();
    }

    private JsonArray getMurdererVotes(int game_id) {
        UserToGame utg;
        JsonArrayBuilder jsonVotesBuilder = Json.createArrayBuilder();
        for (Integer id : userToGameService.usersInGame(game_id)) {
            utg = userToGameService.findByUserId(id);
            jsonVotesBuilder.add(Json.createObjectBuilder().add("user_id", utg.getUser_id()).add("username", userService.findUserById(id).getUsername()).add("votes", utg.getVotesFromMurderers()));
        }
        return jsonVotesBuilder.build();
    }

    @Override
    public JsonObjectBuilder getRoleJson(UserToGame userToGame) {
        JsonObjectBuilder jsonObjectBuilder= super.getRoleJson(userToGame); 
        jsonObjectBuilder.add("murderers", this.getMurderers(userToGame.getGame_id()));
        return jsonObjectBuilder;
    }

    
    
    

}
