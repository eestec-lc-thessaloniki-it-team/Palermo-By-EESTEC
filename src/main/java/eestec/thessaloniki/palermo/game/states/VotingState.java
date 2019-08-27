package eestec.thessaloniki.palermo.game.states;

import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.vote.Vote;
import eestec.thessaloniki.palermo.rest.vote.VoteService;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenVote;
import java.security.SecureRandom;
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

    private final SecureRandom random = new SecureRandom();

    /**
     * This is what happens the first time that we start the voting. All users
     * will be added with 0 votes in the vote table
     *
     * @param game_id
     */
    public void initliazeVoteState(int game_id) {
        voteService.deleteVotes(game_id);
        List<UserToGame> userToGames = userToGameService.userToGameList(game_id);
        this.initlializeUserToGame(userToGames);
        this.insertCandidates(userToGames, game_id);
        this.chooseNextToVote(game_id);
    }

    private void insertCandidates(List<UserToGame> utgs, int game_id) {
        for (UserToGame utg : utgs) {
            voteService.insert(new Vote(game_id, utg.getUser_id(), 0));
        }
    }

    private void initlializeUserToGame(List<UserToGame> utgs) {
        for (UserToGame utg : utgs) {
            utg.setIs_voting(false);
            utg.setHas_vote(false);
            userToGameService.update(utg);
        }
    }

    public Response vote(WrapperUserTokenVote userTokenVote) {
        UserToGame utg = userToGameService.findByUserId(userTokenVote.getUserToken().getUser_id());
        voteService.voted(userTokenVote.getDeadUser_id());
        if (!this.chooseNextToVote(utg.getGame_id())) {
            this.whenVoteEnds(utg.getGame_id());
        }
        return Response.ok().build();

    }

    public Response getVotes(int game_id) {
        JsonObjectBuilder JsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder jsonVotes = Json.createArrayBuilder();
        for (Vote v : voteService.getCurrentVotes(game_id)) {
            jsonVotes.add(Json.createObjectBuilder().add("user_id", v.getUser_id()).add("votes", v.getVotes()));
        }
        JsonObjectBuilder.add("votes", jsonVotes.build());
        JsonObjectBuilder.add("whoIsVoting", userToGameService.getWhoIsVoting(game_id).getUser_id());
        JsonObjectBuilder.add("is_voting_over", userToGameService.isVotingOver(game_id) && voteService.isVotingOver(game_id));
        return Response.ok(JsonObjectBuilder.build()).build();
    }

    private void whenVoteEnds(int game_id) {
        List<Vote> votes = voteService.getCurrentVotes(game_id);
        int countSameVotes = 1;
        for (int i = 1; i < votes.size(); i++) {
            if (votes.get(0).getVotes() == votes.get(i).getVotes()) {
                countSameVotes++;
            }
        }
        if (countSameVotes == votes.size()) {//then we will kill them all
            this.killThemAll(votes);
            voteService.deleteVotes(game_id);
        } else if (countSameVotes == 1) { // one has the most 
            this.killOnePlayer(votes.get(0));
            voteService.deleteVotes(game_id);
        } else {// initialize the voting again
            voteService.deleteVotes(game_id);
            List<UserToGame> usersToGame = userToGameService.userToGameList(game_id);
            this.initlializeUserToGame(usersToGame);
            this.insertCandidates(this.getFirstNUsersInGame(votes, usersToGame, countSameVotes), game_id);
            this.chooseNextToVote(game_id);
        }
    }

    private List<UserToGame> getFirstNUsersInGame(List<Vote> votes, List<UserToGame> usersToGame, int firstN) {
        List<UserToGame> utgs = new ArrayList<>();
        for (int i = 0; i < firstN; i++) {
            for (UserToGame utg : usersToGame) {
                if (utg.getUser_id() == votes.get(i).getUser_id()) {
                    utgs.add(utg);
                    break;
                }
            }
        }
        return utgs;
    }

    private void killThemAll(List<Vote> votes) {
        for (Vote v : votes) {
            this.killOnePlayer(v);
        }
    }

    private void killOnePlayer(Vote v) {
        UserToGame userToGame = userToGameService.findByUserId(v.getUser_id());
        userToGame.setIs_dead(true);
        userToGame.setIsDeadVisible(true);
        userToGameService.update(userToGame);
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
                utg = utgs.get(random.nextInt(utgs.size()));
                utg.setIs_voting(true);
                userToGameService.update(utg);
                return true;
            }
        } else {
            utg.setHas_vote(true);
            utg.setIs_voting(false);
            userToGameService.update(utg);
            utgs = this.getUsersHaventVote(userToGameService.userToGameList(game_id));
            if (utgs.size() > 0) {
                utg = utgs.get(random.nextInt(utgs.size()));
                utg.setIs_voting(true);
                userToGameService.update(utg);
                return true;
            } else {
                return false; // this will determin who is about to die
            }
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
