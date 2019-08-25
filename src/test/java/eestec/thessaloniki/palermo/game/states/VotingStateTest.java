package eestec.thessaloniki.palermo.game.states;

import eestec.thessaloniki.palermo.GeneralTestMethods;
import eestec.thessaloniki.palermo.rest.connectors.GameConnector;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.rest.vote.VoteService;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenVote;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
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
    private SecureRandom rand=new SecureRandom();

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

    }

    @After
    public void finalizeTest() {
        testMethods.logoutFromGame(userTokens);
        voteService.deleteVotes(userToGames.get(0).getGame_id());
        gameConnector.endGame(userTokens.get(0));
    }

    @Test
    public void testGetVotes() {
        changeStates.changeState(userTokens.get(0)); //change state to morning
        changeStates.changeState(userTokens.get(0)); //change state to voting
        assertEquals("Voting", gameService.searchGameByGameID(userToGames.get(0).getGame_id()).getState()); // check if the state has changed
        UserToGame firstVoter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(firstVoter);
        System.out.println("The first voter is " + firstVoter.toString());
        int voteFor = voteSomeone();
        votingState.vote(new WrapperUserTokenVote(findUserToken(firstVoter), voteFor));
        System.out.println("User_id: " + firstVoter.getUser_id() + " has voted for " + voteFor);
        System.out.println(voteService.getCurrentVotes(firstVoter.getGame_id()).toString());
        //there will be 3 new votes and then we will test the functionality
        UserToGame voter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(voter);
        assertNotEquals(voter, firstVoter);
        voteFor = voteSomeone();
        votingState.vote(new WrapperUserTokenVote(findUserToken(voter), voteFor));
        System.out.println("User_id: " + voter.getUser_id() + " has voted for " + voteFor);
        
        voter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(voter);
        assertNotEquals(voter, firstVoter);
        voteFor = voteSomeone();
        votingState.vote(new WrapperUserTokenVote(findUserToken(voter), voteFor));
        System.out.println("User_id: " + voter.getUser_id() + " has voted for " + voteFor);
        
        voter = userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id());
        assertNotNull(voter);
        assertNotEquals(voter, firstVoter);
        voteFor = voteSomeone();
        votingState.vote(new WrapperUserTokenVote(findUserToken(voter), voteFor));
        System.out.println("User_id: " + voter.getUser_id() + " has voted for " + voteFor);
        
        System.out.println(voteService.getCurrentVotes(firstVoter.getGame_id()).toString());
        assertTrue(userToGameService.isVotingOver(voter.getGame_id())); // the voting should be over here
        

    }

    private UserToken findUserToken(UserToGame utg) {
        for (UserToken ut : userTokens) {
            if (ut.getUser_id() == utg.getUser_id()) {
                return ut;
            }
        }
        return null;
    }

    private int voteSomeone() {
        return userTokens.get(rand.nextInt(4)).getUser_id();
    }

}
