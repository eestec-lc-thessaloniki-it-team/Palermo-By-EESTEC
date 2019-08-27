package eestec.thessaloniki.palermo.game.states;

import eestec.thessaloniki.palermo.GeneralTestMethods;
import eestec.thessaloniki.palermo.rest.connectors.GameConnector;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.rest.vote.Vote;
import eestec.thessaloniki.palermo.rest.vote.VoteService;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenVote;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class VotingStateTest {

    @Inject
    GeneralTestMethods testMethods;
    @Inject
    GameConnector gameConnector;
    @Inject
    UserToGameService userToGameService;
    @Inject
    ChangeStates changeStates;
    @Inject
    GameService gameService;
    @Inject
    VotingState votingState;
    @Inject
    VoteService voteService;

    private List<UserToken> userTokens;
    private List<UserToGame> userToGames;
    private SecureRandom rand = new SecureRandom();
    int counterForVote;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "eestec.thessaloniki.palermo")
                .addPackage(GeneralTestMethods.class.getPackage())
                .addAsResource("roles.txt")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void initializeTest() {
        userTokens = testMethods.connectUsers();
        userToGames = testMethods.createStartGame(userTokens);
        counterForVote = 0;

    }

    @After
    public void finalizeTest() {
        testMethods.logoutFromGame(userTokens);
        voteService.deleteVotes(userToGames.get(0).getGame_id());
        gameConnector.endGame(userTokens.get(0));
        counterForVote = 0;
    }

    @Test
    public void testOneGetsAtLeast3VotesAndDies() {
        changeStates.changeState(userTokens.get(0)); //change state to morning
        changeStates.changeState(userTokens.get(0)); //change state to voting
        assertEquals("Voting", gameService.searchGameByGameID(userToGames.get(0).getGame_id()).getState()); // check if the state has changed
        UserToGame firstVoter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(firstVoter);
        System.out.println("The first voter is " + firstVoter.toString());
        int voteFor = everyOneKillOne(2);
        votingState.vote(new WrapperUserTokenVote(findUserToken(firstVoter), voteFor));
        System.out.println("User_id: " + firstVoter.getUser_id() + " has voted for " + voteFor);
        System.out.println(voteService.getCurrentVotes(firstVoter.getGame_id()).toString());

        makeAVote("all");
        makeAVote("all");
        makeAVote("all");

        assertEquals(0, voteService.getCurrentVotes(firstVoter.getGame_id()).size());
        assertTrue(userToGameService.isVotingOver(firstVoter.getGame_id())); // the voting should be over here
        assertTrue(voteService.isVotingOver(firstVoter.getGame_id()));
        List<UserToGame> deadUsers = userToGameService.getDeadPlayers(firstVoter);
        System.out.println("dead users: " + deadUsers.toString());
        assertEquals(1, deadUsers.size());
        assertEquals(true, deadUsers.get(0).isIsDeadVisible());
    }

    @Test
    public void test2Get2VotesAndGoToNextState() {
        changeStates.changeState(userTokens.get(0)); //change state to morning
        changeStates.changeState(userTokens.get(0)); //change state to voting
        assertEquals("Voting", gameService.searchGameByGameID(userToGames.get(0).getGame_id()).getState());
        UserToGame voter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(voter);
        int voteFor = this.halfHalfVotes();
        votingState.vote(new WrapperUserTokenVote(findUserToken(voter), voteFor));
        System.out.println("User_id: " + voter.getUser_id() + " has voted for " + voteFor);

        makeAVote("half");
        makeAVote("half");
        makeAVote("half");

        List<Vote> votes = voteService.getCurrentVotes(voter.getGame_id());
        System.out.println("Votes: " + votes.toString());
        assertEquals(2, votes.size());
        boolean isVoteOverUserToGame = userToGameService.isVotingOver(voter.getGame_id());
        boolean isVoteOverVotes = voteService.isVotingOver(voter.getGame_id());
        assertFalse(isVoteOverVotes);
        assertFalse(isVoteOverUserToGame);
        List<UserToGame> deadUsers = userToGameService.getDeadPlayers(voter);
        System.out.println("dead users: " + deadUsers.toString());
        assertEquals(0, deadUsers.size());

    }

    @Test
    public void after2CandidatesChoose1AllOfThem() {
        this.test2Get2VotesAndGoToNextState();// after this candidates should be 2
        UserToGame voter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(voter); //someone shpuld vote
        List<Vote> votes = voteService.getCurrentVotes(voter.getGame_id());
        assertEquals(2, votes.size()); //between 2
        int voteFor = this.everyOneKillOne(2);
        votingState.vote(new WrapperUserTokenVote(findUserToken(voter), voteFor));
        System.out.println("User_id: " + voter.getUser_id() + " has voted for " + voteFor);
        makeAVote("all");
        makeAVote("all");
        makeAVote("all");

        assertEquals(0, voteService.getCurrentVotes(voter.getGame_id()).size());
        assertTrue(userToGameService.isVotingOver(voter.getGame_id())); // the voting should be over here
        assertTrue(voteService.isVotingOver(voter.getGame_id()));
        List<UserToGame> deadUsers = userToGameService.getDeadPlayers(voter);
        System.out.println("dead users: " + deadUsers.toString());
        assertEquals(1, deadUsers.size());
        assertEquals(8, deadUsers.get(0).getUser_id());
        assertEquals(true, deadUsers.get(0).isIsDeadVisible());

    }

    @Test
    public void after2CandidatesKillThemAll() {
        this.test2Get2VotesAndGoToNextState();// after this candidates should be 2
        UserToGame voter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(voter); //someone shpuld vote
        List<Vote> votes = voteService.getCurrentVotes(voter.getGame_id());
        assertEquals(2, votes.size()); //between 2
        int voteFor = this.halfHalfVotes();
        votingState.vote(new WrapperUserTokenVote(findUserToken(voter), voteFor));
        System.out.println("User_id: " + voter.getUser_id() + " has voted for " + voteFor);
        makeAVote("half");
        makeAVote("half");
        makeAVote("half");
        assertEquals(0, voteService.getCurrentVotes(voter.getGame_id()).size());
        assertTrue(userToGameService.isVotingOver(voter.getGame_id())); // the voting should be over here
        assertTrue(voteService.isVotingOver(voter.getGame_id()));
        List<UserToGame> deadUsers = userToGameService.getDeadPlayers(voter);
        System.out.println("dead users: " + deadUsers.toString());
        assertEquals(2, deadUsers.size());
        assertEquals(true, deadUsers.get(0).isIsDeadVisible());
        assertEquals(true, deadUsers.get(1).isIsDeadVisible());
    }
    
    private UserToken findUserToken(UserToGame utg) {
        for (UserToken ut : userTokens) {
            if (ut.getUser_id() == utg.getUser_id()) {
                return ut;
            }
        }
        return null;
    }

    private int halfHalfVotes() {
        counterForVote++;
        if (counterForVote % 2 == 0) {
            return userTokens.get(3).getUser_id();
        } else {
            return userTokens.get(2).getUser_id();
        }

    }

    private void makeAVote(String type) {
        UserToGame voter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(voter);
        int voteFor = 6;
        if (type.equals("all")) {
            voteFor = everyOneKillOne(2);
        } else if (type.equals("half")) {
            voteFor = halfHalfVotes();
        }
        votingState.vote(new WrapperUserTokenVote(findUserToken(voter), voteFor));
        System.out.println("User_id: " + voter.getUser_id() + " has voted for " + voteFor);
    }

    private int everyOneKillOne(int n) {
        return userTokens.get(n).getUser_id();
    }

}
