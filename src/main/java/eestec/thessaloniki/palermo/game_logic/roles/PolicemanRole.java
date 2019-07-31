package eestec.thessaloniki.palermo.game_logic.roles;

import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;

public class PolicemanRole extends Role {

    public PolicemanRole() {
        super();
        this.roleTeam=RoleTeam.GOOD;
        this.roleName="Policeman";
        this.description="Polceman is a good role. His ability is to ask every night"
                + "for a suspecious player and he will get an answer if he is a murderer"
                + "or not.";
    }

    @Override
    protected Response action(List<User> users) {
         //Askes if a user is  murderer
         UserToGame askingForUser=userToGameService.findByUserId(users.get(0).getId());
         boolean isMurderer=false;
         if( askingForUser.getRole_type().equals("Murderer")){
             isMurderer=true;
         }
         JsonObjectBuilder jsonObjectBuilder= Json.createObjectBuilder()
                 .add("isMurderer", isMurderer);
         return Response.ok(jsonObjectBuilder).build(); //this might have a problem didn't test it
    }

    
    
    
}
