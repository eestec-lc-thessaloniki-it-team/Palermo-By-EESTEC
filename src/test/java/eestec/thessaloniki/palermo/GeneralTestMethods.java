package eestec.thessaloniki.palermo;

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


public class GeneralTestMethods {
    @Inject
    UserTokenService userTokenService;
    @Inject
    UserService userService;
    @Inject
    GameService gameService;
    @Inject
    UserToGameService userToGameService;
    @Inject
    GameConnector gameConnector;
    
     public List<UserToken> connectUsers() {
        List<UserToken> userTokens = new ArrayList<>();
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo").getId()));
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo2").getId()));
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo3").getId()));
        userTokens.add(userTokenService.getToken(userService.findUserByUsername("mavroudo4").getId()));
        return userTokens;
    }
     
     public List<UserToGame> createStartGame(List<UserToken> userTokens) {
        List<UserToGame> userToGames = new ArrayList<>();
        Game game = gameService.createGame(userTokens.get(0));//first one is the leader
        for (int i = 0; i < 4; i++) {
            userToGames.add(userToGameService.addUserToGame(new UserToGame(userTokens.get(i).getUser_id(), game.getId())));
        }
        gameConnector.startGame(userTokens.get(0));
        return userToGames;
    }

    public void logoutFromGame(List<UserToken> userTokens) {
        int game_id = userToGameService.findByUserId(userTokens.get(0).getUser_id()).getGame_id();
        userToGameService.deleteUsersFromGame(game_id);
    }

    
}
