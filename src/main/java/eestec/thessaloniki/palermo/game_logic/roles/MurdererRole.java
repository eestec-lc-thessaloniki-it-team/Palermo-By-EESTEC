package eestec.thessaloniki.palermo.game_logic.roles;

import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

public class MurdererRole extends Role {

    @Inject
    private UserService userService;

    public MurdererRole() {
        super();
        this.roleName = "Murderer";
        this.roleTeam = RoleTeam.BAD;
        this.description = "This is the worst role in the game. There is one more"
                + "in the game, that you have to team up in order to kill the "
                + "rest players.";
    }

    @Override
    public Response action(List<User> users) {
        //Morderer will suggest someone to be killed
        try {
            User user = userService.findUserByUsername(users.get(0).getUsername());
            userToGameService.voteUser(user.getId());
            return Response.ok().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @Override
    public Response info() {
        List<String> usernames = new ArrayList<>();
        try {
            for (UserToGame utg : userToGameService.getAllPlayesOfType(this.roleName)) {
                usernames.add(userService.findUserById(utg.getUser_id()).getUsername());
            }
            return Response.ok(usernames).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }

    }

}
