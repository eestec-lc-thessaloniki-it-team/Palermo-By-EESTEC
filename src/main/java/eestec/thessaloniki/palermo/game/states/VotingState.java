package eestec.thessaloniki.palermo.game.states;

import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.vote.Vote;
import eestec.thessaloniki.palermo.rest.vote.VoteService;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenVote;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;

/**
 *
 * This class will keep all the information needed during the voting state
 */
public class VotingState {

    @Inject
    VoteService voteService;
    @Inject
    UserToGameService userToGameService;

   private final  java.util.Random random= new java.util.Random();

    public void initliazeVoteState(int game_id) {
        voteService.deleteVotes(game_id);
        for (UserToGame utg : userToGameService.userToGameList(game_id)) {
            utg.setIs_voting(false);
            utg.setHas_vote(false);
            userToGameService.update(utg);
        }
        this.chooseNextToVote(game_id);
    }

    /**
     * Make a vote
     *
     * @param vote
     * @return
     */
    public Response vote(WrapperUserTokenVote userTokenVote) {
        UserToGame utg = userToGameService.findByUserId(userTokenVote.getUserToken().getUser_id());
        voteService.insert(new Vote(utg.getGame_id(), utg.getUser_id(), userTokenVote.getDeadUser_id()));
        this.chooseNextToVote(utg.getGame_id());
        return Response.ok().build();

    }

    public Response getVotes(int game_id) {
        JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder jsonVotes = Json.createArrayBuilder();
        for (Vote v : voteService.getCurrentVotes(game_id)) {
            jsonVotes.add(Json.createObjectBuilder().add("user_id", v.getUser_id()).add("deadUser_id", v.getDead_user_id()));
        }
        JsonObjectBuilder.add("votes", jsonVotes.build());
        JsonObjectBuilder.add("whoIsVoting", userToGameService.getWhoIsVoting(game_id).getUser_id());
        JsonObjectBuilder.add("is_voting_over", userToGameService.isVotingOver(game_id));
        return Response.ok(JsonObjectBuilder.build()).build();
    }

    /**
     * Choose the next one to vote
     *
     * @param game_id
     * @return true if there is next candidate, if not = already have voted
     * return false
     */
    private boolean chooseNextToVote(int game_id) {
        //set something equal to true in order to know that the voting is over
        UserToGame utg = userToGameService.getWhoIsVoting(game_id);
        List<UserToGame> utgs = userToGameService.userToGameList(game_id);
        if (utg == null) { //it is either first or last time           
            if (utgs.get(0).isHas_vote()) {// we are at the last
                return false;
            } else {// we are at the first time
                utg = utgs.get(random.nextInt(utgs.size() - 1));
                utg.setIs_voting(true);
                userToGameService.update(utg);
                return true;
            }
        }
        utg.setHas_vote(true);
        utg.setIs_voting(false);
        userToGameService.update(utg);
        utgs = this.getUsersHaventVote(utgs);
        if (utgs.size() > 0) {
            utg = utgs.get(random.nextInt(utgs.size() - 1));
            utg.setIs_voting(true);
            userToGameService.update(utg);
            return true;
        }else{
            return false;
        }

    }

    private List<UserToGame> getUsersHaventVote(List<UserToGame> voters) {
        List<UserToGame> haventVote = new ArrayList<>();
        for (UserToGame voter : voters) {
            if (!voter.isHas_vote()) {
                haventVote.add(voter);
            }
        }
        return haventVote;
    }
    
   }
