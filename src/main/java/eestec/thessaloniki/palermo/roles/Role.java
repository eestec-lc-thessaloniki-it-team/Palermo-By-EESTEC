package eestec.thessaloniki.palermo.roles;

import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;


public class Role {
    protected String roleName;
    protected RoleTeam roleTeam;
    protected String description;
    @Inject
    UserToGameService userToGameService;

    public Role() {
    }
    
    protected Response action(List<User> users){
        //This will be override by every role 
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

    
    
    
}
