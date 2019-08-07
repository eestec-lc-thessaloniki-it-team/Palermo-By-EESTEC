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

    public Response getRoleJson(UserToken userToken) {
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        Role role = this.getUsersRole(userToGame);
        if (role == null) {
            return Response.status(510, "Error with finding the role of the user").build();
        }
        return Response.ok(role.getRoleJson(userToGame).build()).build();
    }

    public Response act(UserToken userToken, List<Integer> ids) {
        System.out.println("In night state act");

        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        Role role = this.getUsersRole(userToGame);

        System.out.println("Role is " + role.getRoleName());
        if (role == null) {
            return Response.status(510, "Error with finding the role of the user").build();
        }
        try {

            return role.action(userToGame,this.getUsers(userToGame, ids));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Response.status(400).build();
        }
    }

    public Response info(UserToken userToken) {
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        Role role = this.getUsersRole(userToGame);
        if (role == null) {
            return Response.status(510, "Error with finding the role of the user").build();
        }
        try {
            return role.info(userToGame);
        } catch (NullPointerException e) {
            return Response.status(400).build();
        }
    }

    private Role getUsersRole(UserToGame userToGame) {
        for (Role r : roles.getRoles()) {
            if (r.getRoleName().equals(userToGame.getRole_type())) {
                return r;
            }
        }
        return null;
    }

    private List<UserToGame> getUsers(UserToGame userToGame, List<Integer> ids) {
        List<UserToGame> users = new ArrayList<>();
        UserToGame u;
        try {
            for (Integer id : ids) {
                u = userToGameService.findByUserId(id);
                if (u.getGame_id() == userToGame.getGame_id()) {
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
