/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eestec.thessaloniki.palermo.rest.connectors;

import eestec.thessaloniki.palermo.game.game_logic.GiveRoles;
import eestec.thessaloniki.palermo.game.states.ChangeStates;
import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
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
        changeStates.changeStateTo("Night", userToGameService.findByUserId(leaderToken.getUser_id()));
        return Response.ok(game).build();

    }

    public Response gameInfo(UserToken userToken) {
        return Response.ok(this.findGame(userToken.getUser_id())).build();
    }

    public Response getUsersInGame(UserToken userToken) {
        Game game = this.findGame(userToken.getUser_id());
        List<UserToGame> users_id = userToGameService.userToGameList(game.getId());
        List<String> usersList = new ArrayList<>();
        for (UserToGame utg : users_id) {
            usersList.add(userService.findUserById(utg.getUser_id()).getUsername());
        }
        return Response.ok(usersList).build();
    }

    public Response endGame(UserToken userToken) {
        Game game = this.findGame(userToken.getUser_id());
        userToGameService.deleteUsersFromGame(game.getId());
        gameService.deleteGame(game);
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
