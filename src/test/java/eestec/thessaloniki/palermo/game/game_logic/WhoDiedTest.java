package eestec.thessaloniki.palermo.game.game_logic;

import eestec.thessaloniki.palermo.GeneralTestMethods;
import eestec.thessaloniki.palermo.rest.connectors.GameConnector;
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
    GeneralTestMethods testMethods;
    @Inject
    GameConnector gameConnector;

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
        List<UserToken> userTokens = testMethods.connectUsers();
        List<UserToGame> userToGames = testMethods.createStartGame(userTokens);
        System.out.println(userToGames.toString());

    }

    @After
    public void finalizeTest() {
        List<UserToken> userTokens=testMethods.connectUsers();
        testMethods.logoutFromGame(userTokens);
        gameConnector.endGame(userTokens.get(0));
    }

    @Test
    public void test() {
        assertTrue(true);
    }
    
    @Test
    public void testRoles(){
        //This will test if all roles are connected
    }


}
