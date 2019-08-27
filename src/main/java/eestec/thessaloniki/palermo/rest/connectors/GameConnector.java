package eestec.thessaloniki.palermo.rest.connectors;

import eestec.thessaloniki.palermo.game.game_logic.GiveRoles;
import eestec.thessaloniki.palermo.game.states.ChangeStates;
import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;

/**
 *
 * This call will contain all the necessary logic that the endpoint will need.
 * By removing the logic from theResource file, it is easier to test and handle
 * the logic. All methods will return Response and will be directly returned
 * from Resource file
 */
@RequestScoped
public class GameConnector {

    @Inject
    GameService gameService;
    @Inject
    GiveRoles giveRoles;
    @Inject
    UserToGameService userToGameService;
    @Inject
    ChangeStates changeStates;
    @Inject
    UserService userService;

    public Response startGame(UserToken leaderToken) {
        Game game = this.findGame(leaderToken.getUser_id());
        if (!giveRoles.giveRoles(game.getId())) {
            return Response.status(505, "couldn't give roless").build();
        }
        game.setStarted(true);
        gameService.updateGame(game);
        changeStates.changeState(leaderToken);
        return Response.ok(game).build();

    }

    public Response gameInfo(UserToken userToken) {
        return Response.ok(this.findGame(userToken.getUser_id())).build();
    }

    public Response getUsersInGame(UserToken userToken) {
        Game game = this.findGame(userToken.getUser_id());
        List<UserToGame> users_id = userToGameService.userToGameList(game.getId());
        JsonObjectBuilder jsonObjectBuilder=Json.createObjectBuilder();
        JsonArrayBuilder jsonArray=Json.createArrayBuilder();
        for (UserToGame utg : users_id) {
            User u=userService.findUserById(utg.getUser_id());
            jsonArray.add(Json.createObjectBuilder().add("user_id", u.getId()).add("username", u.getUsername()));
        }
        jsonObjectBuilder.add("users", jsonArray.build());
        return Response.ok(jsonObjectBuilder.build()).build();
    }

    public Response endGame(UserToken userToken) {
        try {
            Game game = this.findGame(userToken.getUser_id());
            userToGameService.deleteUsersFromGame(game.getId());
            gameService.deleteGame(game);
        } catch (NullPointerException e) { // this will be that there is no user in the game, after all logged out
            gameService.deleteGame(gameService.searchGameByLeaderID(userToken.getUser_id()));
        }
        
        return Response.ok().build();
    }

    public Response joinGame(String random_id, UserToken userToken) {
        Game game = gameService.searchGameByRandomId(random_id);
        userToGameService.addUserToGame(new UserToGame(userToken.getUser_id(), game.getId()));
        return Response.ok(game).build();
    }

    public Response createGame(UserToken leaderToken) {
        Game game = gameService.createGame(leaderToken);
        userToGameService.addUserToGame(new UserToGame(game.getLeader_id(), game.getId()));
        return Response.ok(game).build();
    }

    private Game findGame(int user_id) {
        int game_id = userToGameService.findByUserId(user_id).getGame_id();
        return gameService.searchGameByGameID(game_id);
    }

}
