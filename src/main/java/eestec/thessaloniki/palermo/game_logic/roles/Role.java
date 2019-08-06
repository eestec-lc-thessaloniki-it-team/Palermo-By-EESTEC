package eestec.thessaloniki.palermo.game_logic.roles;

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

    @Inject
    protected UserToGameService userToGameService;

    public Role() {
    }
    
    public Response action(List<UserToGame> users){
        //This will be override by every role 
        return Response.ok().build();
    }
    
    public Response info(UserToGame userToGame){
        //This will be override by any role that has to ask for info, like murderer and prostitude
        return Response.ok().build();
    }
    
    protected enum RoleTeam{
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

    @Override
    public String toString() {
        return "Role{" + "roleName=" + roleName + ", roleTeam=" + roleTeam + ", description=" + description + '}';
    }

    public JsonObject getRoleJson(){
        JsonObjectBuilder jsonObjectBuilder= Json.createObjectBuilder()
                 .add("roleName",this.roleName).add("roleTeam", this.roleTeam.toString()).add("description", this.description);
        return jsonObjectBuilder.build();
    }
    

    
    
    
}
