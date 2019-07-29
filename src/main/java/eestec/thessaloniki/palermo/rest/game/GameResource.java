package eestec.thessaloniki.palermo.rest.game;

import eestec.thessaloniki.palermo.annotations.AuthorizedUser;
import eestec.thessaloniki.palermo.rest.user.User;
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
public class GameResource {

    @Inject
    GameService gameService;
    
    @Inject
    UserToGameService userToGameService;
    
    @Inject 
    UserService userService;

    @Path("newGame")
    @POST
    @AuthorizedUser
    public Response createGame(UserToken userToken) {
        Game game=new Game(userToken.getUser_id());
        while (true) {
            try {
                game.generateRandomId();
                gameService.createGame(game);
                break;
            } catch (ConstraintViolationException exception) {
            }
        }
        userToGameService.addUserToGame(new UserToGame(game.getLeader_id(),game.getId()));
        return Response.ok(game).build();
    }

    @Path("joinGame/{random_id}")
    @POST
    @AuthorizedUser
    public Response joinGame(@PathParam("random_id") String random_id,UserToken userToken) {
        Game game = gameService.searchGameByRandomId(random_id);
        userToGameService.addUserToGame(new UserToGame(userToken.getUser_id(),game.getId()));
        return Response.ok(game).build();
    }
    
    @Path("endGame/{random_id}")
    @POST
    public Response endGame(@PathParam("random_id") String random_id,UserToken userToken){
        Game game = gameService.searchGameByRandomId(random_id);
        userToGameService.deleteUsersFromGame(game.getId());
        gameService.deleteGame(game);
        return Response.ok().build();
    }
    
    @Path("getUsers/{random_id}")
    @POST
    @AuthorizedUser
    public Response getUsersOfGame(@PathParam("random_id") String random_id,UserToken userToken){
        Game game = gameService.searchGameByRandomId(random_id);
        List<Integer> users_id=userToGameService.usersInGame(game.getId());
        List<String> usersList = new ArrayList<>();
        for(Integer i :users_id){
            usersList.add(userService.findUserById(i).getUsername());
        }
        return Response.ok(usersList).build();
        
    }

}
