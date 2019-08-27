package eestec.thessaloniki.palermo.rest.Resources;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.game.states.ChangeStates;
import eestec.thessaloniki.palermo.game.states.NightState;
import eestec.thessaloniki.palermo.game.states.VotingState;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenListIds;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenVote;
import javax.inject.Inject;
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
@GameExists
public class InGameResource {

    @Inject
    GameService gameService;
    @Inject
    UserToGameService userToGameService;
    @Inject
    NightState nightState;
    @Inject
    VotingState votingState;
    @Inject
    ChangeStates changeStates;

    @Path("info")
    @POST
    public Response getUserToGame(UserToken userToken) {
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        if (userToGame != null) {
            return Response.ok(userToGame).build();
        } else {
            return Response.status(404, "No user in game found").build();
        }
    }

    @Path("nextState")
    @POST
    @Leader
    //check if you can change the state
    public Response changeState(UserToken userToken) {
        return changeStates.changeState(userToken);

    }

    @Path("deadPlayers")
    @POST
    public Response getDeadPlayers(UserToken userToken) {
        UserToGame utg = userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getDeadPeople(utg);
    }

    @Path("deadRoles")
    @POST
    public Response getDeadRoles(UserToken userToken) {
        UserToGame utg = userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getDeadRoles(utg);
    }

    @Path("allRoles")
    @POST
    public Response getAllRoles(UserToken userToken) {
        UserToGame utg = userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getAllRoles(utg);
    }

    /**
     * This will inform users for the state of the game
     */
    @Path("state")
    @POST
    public Response getState(UserToken userToken) {
        return this.changeStates.getState(userToken);
    }

    @Path("roleInfo")
    @POST
    public Response getRoleJson(UserToken userToken) {
        return nightState.getRoleJson(userToken);
    }

    @Path("act")
    @POST
    public Response act(WrapperUserTokenListIds wrapper) {
        return nightState.act(wrapper.getUserToken(), wrapper.getIds());
    }

    // the info that some roles might need to return
    @Path("nightInfo")
    @POST
    public Response nightInfo(UserToken userToken) {
        return nightState.info(userToken);

    }

    @Path("vote")
    @POST
    public Response vote(WrapperUserTokenVote userTokenVote) {
        return this.votingState.vote(userTokenVote);
    }

    @Path("getVotes")
    @POST
    public Response getVotes(UserToken userToken){
        return this.votingState.getVotes(userToken);
    }
    }
