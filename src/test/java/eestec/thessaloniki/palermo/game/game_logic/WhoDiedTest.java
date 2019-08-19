package eestec.thessaloniki.palermo.game.game_logic;

import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.rest.user_token.UserTokenService;
import java.util.ArrayList;
import java.util.List;
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
public class WhoDiedTest {

    @Inject
    UserService userService;
    @Inject
    UserToGameService userToGameService;
    @Inject
    UserTokenService userTokenService;
    @Inject
    GameService gameService;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "eestec.thessaloniki.palermo.rest")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void initializeTest() {
        List<UserToken> userTokens = this.connectUsers();
        List<UserToGame> userToGames=this.createStartGame(userTokens);
        System.out.println(userToGames.toString());

    }
    
    @After
    public void finalizeTest(){
        this.logoutFromGame(this.connectUsers());
    }

    @Test
    public void test() {
        assertTrue(true);
    }

    private List<UserToken> connectUsers() {
        List<UserToken> userTokens = new ArrayList<>();
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo").getId()));
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo2").getId()));
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo3").getId()));
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo4").getId()));
        return userTokens;
    }

    private List<UserToGame> createStartGame(List<UserToken> userTokens) {
        List<UserToGame> userToGames=new ArrayList<>();
        Game game = gameService.createGame(userTokens.get(0));//first one is the leader
        for (int i = 0; i < 4; i++) {
            userToGames.add(userToGameService.addUserToGame(new UserToGame(userTokens.get(i).getUser_id(), game.getId())));
        }
        return userToGames;
    }
    
    private void logoutFromGame(List<UserToken> userTokens){
        int game_id=userToGameService.findByUserId(userTokens.get(0).getUser_id()).getGame_id();
        userToGameService.deleteUsersFromGame(game_id);
    }

}
