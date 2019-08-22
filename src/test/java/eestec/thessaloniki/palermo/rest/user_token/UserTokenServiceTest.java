package eestec.thessaloniki.palermo.rest.user_token;

import eestec.thessaloniki.palermo.GeneralTestMethods;
import eestec.thessaloniki.palermo.rest.connectors.GameConnector;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
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
public class UserTokenServiceTest {

    @Inject
    GeneralTestMethods testMethods;
    @Inject
    GameConnector gameConnector;
    @Inject
    UserToGameService userToGameService;
    
    private List<UserToken> userTokens ;
    private List<UserToGame> userToGames;
    
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
        userTokens= testMethods.connectUsers();
        userToGames = testMethods.createStartGame(userTokens);

    }

    @After
    public void finalizeTest() {
        testMethods.logoutFromGame(userTokens);
        gameConnector.endGame(userTokens.get(0));
    }
    
    @Test
    public void testGetWhoIsVoting(){
        assertNull(userToGameService.getWhoIsVoting(userToGames.get(0).getGame_id()));
    }

}
