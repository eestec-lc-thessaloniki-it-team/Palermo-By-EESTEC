package eestec.thessaloniki.palermo.game_logic.roles;

import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.wrappers.WrapperUsersMurdererVotes;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import org.json.simple.JSONArray;

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
    public Response action(List<UserToGame> users) {
        //Morderer will suggest someone to be killed
        System.out.println("I am murderer and i am acting");
        try {
            users.get(0).setVotesFromMurderers(users.get(0).getVotesFromMurderers() + 1);
            return Response.ok(userToGameService.update(users.get(0))).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Override
    public Response info(UserToGame userToGame) {
        List<User> users = new ArrayList<>();
        List<WrapperUsersMurdererVotes> usersInGame = new ArrayList<>();
        try {
            for (UserToGame utg : userToGameService.getAllPlayesOfType(this.roleName, userToGame.getGame_id())) {
                users.add(userService.findUserById(utg.getUser_id()).hidePassword());
            }
            System.out.println(users.toString());
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                    .add("murderers", Json.createArrayBuilder(users).build());

            //find the votes
            for (Integer id : userToGameService.usersInGame(userToGame.getGame_id())) {
                usersInGame.add(new WrapperUsersMurdererVotes(userToGameService.findByUserId(id), userService.findUserById(id).getUsername()));
            }
            System.out.println(usersInGame.toString());
            jsonObjectBuilder.add("murdererVotes", Json.createArrayBuilder(usersInGame).build());

            return Response.ok(jsonObjectBuilder.build()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

    }

}
