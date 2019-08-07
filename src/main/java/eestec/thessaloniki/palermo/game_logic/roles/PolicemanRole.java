package eestec.thessaloniki.palermo.game_logic.roles;

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
    public Response action(UserToGame userToGame,List<UserToGame> users) {
         System.out.println("Here is police act and the list of users has this "+users.toString());
         JsonObjectBuilder jsonObjectBuilder= Json.createObjectBuilder()
                 .add("isMurderer", users.get(0).getRole_type().equals("Murderer"));
         return Response.ok(jsonObjectBuilder.build()).build();
    }

    
    
    
}
