package eestec.thessaloniki.palermo.rest.Resources;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.game_logic.GiveRoles;
import eestec.thessaloniki.palermo.game_logic.roles.Roles;
import eestec.thessaloniki.palermo.game_logic.states.ChangeStates;
import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("game")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AuthorizedUser
@GameExists
public class GameResource {

    @Inject
    GameService gameService;

    @Inject
    UserToGameService userToGameService;

    @Inject
    UserService userService;

    @Inject
    Roles roles;

    @Inject
    GiveRoles giveRoles;
    
    @Inject
    ChangeStates changeStates;

    @Path("newGame")
    @POST
    public Response createGame(UserToken userToken) {
        Game game = new Game(userToken.getUser_id());
        while (true) {
            try {
                game.generateRandomId();
                gameService.createGame(game);
                break;
            } catch (ConstraintViolationException exception) {
            }
        }
        userToGameService.addUserToGame(new UserToGame(game.getLeader_id(), game.getId()));
        return Response.ok(game).build();
    }

    @Path("joinGame/{random_id}")
    @POST

    public Response joinGame(@PathParam("random_id") String random_id, UserToken userToken) {
        Game game = gameService.searchGameByRandomId(random_id);
        userToGameService.addUserToGame(new UserToGame(userToken.getUser_id(), game.getId()));
        return Response.ok(game).build();
    }

    @Path("endGame")
    @POST
    public Response endGame(UserToken userToken) {
        Game game = this.findGame(userToken.getUser_id());
        userToGameService.deleteUsersFromGame(game.getId());
        gameService.deleteGame(game);
        return Response.ok().build();
    }

    @Path("getUsers")
    @POST
    public Response getUsersOfGame(UserToken userToken) {
        Game game = this.findGame(userToken.getUser_id());
        List<Integer> users_id = userToGameService.usersInGame(game.getId());
        List<String> usersList = new ArrayList<>();
        for (Integer i : users_id) {
            usersList.add(userService.findUserById(i).getUsername());
        }
        return Response.ok(usersList).build();

    }

    @Path("gameInfo")
    @POST
    public Response getGameInfo(UserToken userToken) {
        return Response.ok(this.findGame(userToken.getUser_id())).build();
    }

    @Path("startGame")
    @POST
    @Leader
    public Response startGame(UserToken userToken) {
        Game game = this.findGame(userToken.getUser_id());      
        if (!giveRoles.giveRoles(game.getId())) {
            return Response.status(505, "couldn't give roless").build();
        }
        game.setStarted(true);
        gameService.updateGame(game);
         changeStates.changeStateTo("Night", userToGameService.findByUserId(userToken.getUser_id()));
        return Response.ok(game).build();

    }

    private Game findGame(int user_id) {
        int game_id = userToGameService.findByUserId(user_id).getGame_id();
        return gameService.searchGameByGameID(game_id);
    }

}
