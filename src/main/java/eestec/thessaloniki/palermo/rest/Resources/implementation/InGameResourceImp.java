package eestec.thessaloniki.palermo.rest.Resources.implementation;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.game.states.ChangeStates;
import eestec.thessaloniki.palermo.game.states.NightState;
import eestec.thessaloniki.palermo.game.states.VotingState;
import eestec.thessaloniki.palermo.rest.Resources.interfaces.InGameResource;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenListIds;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenVote;
import javax.inject.Inject;
import javax.ws.rs.core.Response;


@AuthorizedUser
@GameExists
public class InGameResourceImp implements InGameResource{

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

    @Override
    public Response getUserToGame(UserToken userToken) {
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        if (userToGame != null) {
            return Response.ok(userToGame).build();
        } else {
            return Response.status(404, "No user in game found").build();
        }
    }

    @Override
    @Leader
    //check if you can change the state
    public Response changeState(UserToken userToken) {
        return changeStates.changeState(userToken);

    }

    @Override
    public Response getDeadPlayers(UserToken userToken) {
        UserToGame utg = userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getDeadPeople(utg);
    }

    @Override
    public Response getDeadRoles(UserToken userToken) {
        UserToGame utg = userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getDeadRoles(utg);
    }

    @Override
    public Response getAllRoles(UserToken userToken) {
        UserToGame utg = userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getAllRoles(utg);
    }

    /**
     * This will inform users for the state of the game
     */
    @Override
    public Response getState(UserToken userToken) {
        return this.changeStates.getState(userToken);
    }

    @Override
    public Response getRoleJson(UserToken userToken) {
        return nightState.getRoleJson(userToken);
    }

    @Override
    public Response act(WrapperUserTokenListIds wrapper) {
        return nightState.act(wrapper.getUserToken(), wrapper.getIds());
    }

    // the info that some roles might need to return
    @Override
    public Response nightInfo(UserToken userToken) {
        return nightState.info(userToken);

    }

    @Override
    public Response vote(WrapperUserTokenVote userTokenVote) {
        return this.votingState.vote(userTokenVote);
    }

    @Override
    public Response getVotes(UserToken userToken) {
        return this.votingState.getVotes(userToken);
    }
}
