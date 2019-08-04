package eestec.thessaloniki.palermo.game_logic;

import eestec.thessaloniki.palermo.game_logic.roles.Role;
import eestec.thessaloniki.palermo.game_logic.roles.Roles;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

/**
 *
 * This will be responsible for events that will take place in night state
 */
public class NightService {

    @Inject
    UserToGameService userToGameService;

    @Inject
    UserService userService;

    @Inject
    private Roles roles;

    public Response act(UserToken userToken, List<String> usernames) {
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        Role role = this.getUsersRole(userToGame);
        if (role == null) {
            return Response.status(510, "Error with finding the role of the user").build();
        }
        try {
            return role.action(this.getUsers(userToGame, usernames));
        } catch (NullPointerException e) {
            return Response.status(400).build();
        }
    }
    
    public Response info(UserToken userToken){
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        Role role = this.getUsersRole(userToGame);
        if (role == null) {
            return Response.status(510, "Error with finding the role of the user").build();
        }
        try {
            return role.info();
        } catch (NullPointerException e) {
            return Response.status(400).build();
        }
    }
    

    private Role getUsersRole(UserToGame userToGame) {

        Role role;
        for (Role r : roles.getRoles()) {
            if (r.getRoleName().equals(userToGame.getRole_type())) {
                return r;
            }
        }
        return null;
    }

    private List<User> getUsers(UserToGame userToGame, List<String> usernames) {
        List<User> users = new ArrayList<>();
        User u;
        try {
            for (String username : usernames) {
                u = userService.findUserByUsername(username);
                if (userToGameService.findByUserId(u.getId()).getGame_id() == userToGame.getGame_id()) {
                    users.add(u);
                } else {
                    return null;
                }
            }
            return users;
        } catch (NullPointerException e) {
            return null;
        }
    }

}
