package eestec.thessaloniki.palermo.game.roles;

import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.List;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;

public class Role {

    protected String roleName;
    protected RoleTeam roleTeam;
    protected String description;
    protected String whenCanTakeAction; //need to be override if it can act at day

    @Inject
    protected UserToGameService userToGameService;

    public Role() {
        this.whenCanTakeAction = "Night";
    }

    public Response action(UserToGame userToGame, List<UserToGame> users) {
        //This will be override by every role 
        return Response.ok().build();
    }

    public Response info(UserToGame userToGame) {
        //This will be override by any role that has to ask for info, like murderer and prostitude
        return Response.ok().build();
    }

    public UserToGame hasActed(UserToGame userToGame) {
        userToGame.setActedAtNight(true);
        userToGameService.update(userToGame);
        return userToGame;
    }

    public enum RoleTeam {
        GOOD, BAD, NEUTRAL
    }

    public String getRoleName() {
        return roleName;
    }

    public RoleTeam getRoleTeam() {
        return roleTeam;
    }

    public String getDescription() {
        return description;
    }

    public String getWhenCanTakeAction() {
        return whenCanTakeAction;
    }

    @Override
    public String toString() {
        return "Role{" + "roleName=" + roleName + ", roleTeam=" + roleTeam + ", description=" + description + '}';
    }

    public JsonObjectBuilder getRoleJson(UserToGame userToGame) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder()
                .add("roleName", this.roleName).add("roleTeam", this.roleTeam.toString()).add("description", this.description);
        return jsonObjectBuilder;
    }

}
