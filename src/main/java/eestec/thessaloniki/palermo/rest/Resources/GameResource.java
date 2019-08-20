package eestec.thessaloniki.palermo.rest.Resources;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.rest.connectors.GameConnector;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import javax.inject.Inject;
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
    GameConnector gameConnector;

    @Path("newGame")
    @POST
    public Response createGame(UserToken userToken) {
       return gameConnector.createGame(userToken);
    }

    @Path("joinGame/{random_id}")
    @POST

    public Response joinGame(@PathParam("random_id") String random_id, UserToken userToken) {
        return gameConnector.joinGame(random_id, userToken);
    }

    @Path("endGame")
    @POST
    public Response endGame(UserToken userToken) {
        return gameConnector.endGame(userToken);
    }

    @Path("getUsers")
    @POST
    public Response getUsersInGame(UserToken userToken) {
        return gameConnector.getUsersInGame(userToken);

    }

    @Path("gameInfo")
    @POST
    public Response getGameInfo(UserToken userToken) {
        return gameConnector.gameInfo(userToken);
    }

    @Path("startGame")
    @POST
    @Leader
    public Response startGame(UserToken userToken) {
        return gameConnector.startGame(userToken);

    }

}
