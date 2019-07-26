package eestec.thessaloniki.palermo.roles;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Produces;

/**
 * 
 * This class will contain a producer that will returns a list with all the 
 * objects role that we have created
 */
public class RolesFactory {
    
    @Produces
    public List<Role> getRolesAvaliable(){
        List<Role> roles= new ArrayList<>();
        roles.add(new MurdererRole());
        roles.add(new PolicemanRole());
        roles.add(new VictimRole());
        return roles;
    }
}
