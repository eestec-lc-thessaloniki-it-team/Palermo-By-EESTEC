package eestec.thessaloniki.palermo.rest.Resources.interfaces;

import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
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
public interface GameResource {
    
    
    @Path("newGame")
    @POST
    public Response createGame(UserToken userToken) ;
    
    @Path("joinGame/{random_id}")
    @POST
    public Response joinGame(@PathParam("random_id") String random_id, UserToken userToken);
    
    @Path("endGame")
    @POST
    public Response endGame(UserToken userToken) ;
    
    @Path("getUsers")
    @POST
    public Response getUsersInGame(UserToken userToken) ;
    
    @Path("gameInfo")
    @POST
    public Response getGameInfo(UserToken userToken) ;

    @Path("startGame")
    @POST
    @Leader
    public Response startGame(UserToken userToken) ;
}
