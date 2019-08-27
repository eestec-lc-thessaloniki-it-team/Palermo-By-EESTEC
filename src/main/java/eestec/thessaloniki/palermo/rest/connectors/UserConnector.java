/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eestec.thessaloniki.palermo.rest.connectors;

import eestec.thessaloniki.palermo.game.game_logic.GiveRoles;
import eestec.thessaloniki.palermo.game.roles.Roles;
import eestec.thessaloniki.palermo.game.states.ChangeStates;
import eestec.thessaloniki.palermo.game.states.NightState;
import eestec.thessaloniki.palermo.game.states.VotingState;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.rest.user_token.UserTokenService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 *
 * @author mavroudo
 */
public class UserConnector {
   @Inject
    UserService userService;
    @Inject
    UserTokenService userTokenService;
    @Inject
    UserToGameService userToGameService;

    @Inject
    Roles roles;

    @Inject
    GiveRoles giveRoles;
    
    public Response deleteUser(UserToken userToken) {
        try {
            userTokenService.deleteUserToken(userToken);
            userToGameService.logOutUserFromGame(userToken.getUser_id());
            userService.removeUser(userToken.getUser_id());
            return Response.ok().build();
        } catch (Exception exception) {
            System.out.println("Problem with deleting user");
            exception.printStackTrace();
            return Response.serverError().build();
        }
    }
    
    public Response logOutUser(UserToken userToken) {
        try {
            userTokenService.deleteUserToken(userToken);
            userToGameService.logOutUserFromGame(userToken.getUser_id());
            return Response.ok().build();
        } catch (Exception anyException) {
            anyException.printStackTrace();
            return Response.serverError().build();
        }

    }
    
    public Response logInUser(User user) {
        User u = userService.findUser(user);
        return Response.ok(userTokenService.getToken(u.getId())).build();

    }
    
    public Response createUser(User user) {
        userService.createUser(user);
        UserToken userToken = userTokenService.getToken(user.getId());
        return Response.ok(userToken).build();
    }
    
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
