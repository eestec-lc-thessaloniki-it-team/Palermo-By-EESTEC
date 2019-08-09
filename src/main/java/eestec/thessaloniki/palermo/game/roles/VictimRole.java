package eestec.thessaloniki.palermo.game.roles;

import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import java.util.List;
import javax.ws.rs.core.Response;

public class VictimRole extends Role{

    public VictimRole() {
        super();
        this.roleName="Victim";
        this.roleTeam=RoleTeam.GOOD;
        this.description="You are a possible victim, the onlly thing that you can "
                + "do is to be aware for the murderers, try to spot them and then"
                + "vote for them. Try not to be an easy target!";
    }

    @Override
    public Response action(UserToGame userToGame,List<UserToGame> users) {
        //Thanks for your cooperation :P :P
        return Response.ok().build(); 
    }
    
    
    
    
    
}
