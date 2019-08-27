package eestec.thessaloniki.palermo.rest.Resources.interfaces;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenListIds;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenVote;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ingame")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AuthorizedUser
public interface InGameResource {
    
    @Path("info")
    @POST
    public Response getUserToGame(UserToken userToken) ;

    @Path("nextState")
    @POST
    @Leader
    //check if you can change the state
    public Response changeState(UserToken userToken) ;

    @Path("deadPlayers")
    @POST
    public Response getDeadPlayers(UserToken userToken) ;

    @Path("deadRoles")
    @POST
    public Response getDeadRoles(UserToken userToken) ;

    @Path("allRoles")
    @POST
    public Response getAllRoles(UserToken userToken) ;


    @Path("state")
    @POST
    public Response getState(UserToken userToken) ;

    @Path("roleInfo")
    @POST
    public Response getRoleJson(UserToken userToken);

    @Path("act")
    @POST
    public Response act(WrapperUserTokenListIds wrapper) ;

    // the info that some roles might need to return
    @Path("nightInfo")
    @POST
    public Response nightInfo(UserToken userToken) ;

    @Path("vote")
    @POST
    public Response vote(WrapperUserTokenVote userTokenVote) ;

    @Path("getVotes")
    @POST
    public Response getVotes(UserToken userToken);
    
}
