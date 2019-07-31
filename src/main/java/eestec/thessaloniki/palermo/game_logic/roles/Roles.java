package eestec.thessaloniki.palermo.game_logic.roles;

import java.util.ArrayList;
import java.util.List;

public class Roles {

    private List<Role> roles;

    public Roles() {
        this.roles = new ArrayList<>();
        initializeList();
    }

    private void initializeList() {
        this.roles.add(new MurdererRole());
        this.roles.add(new VictimRole());
        this.roles.add(new PolicemanRole());
    }
    
    public List<Role> getRoles(){
        return roles;
    }

}
