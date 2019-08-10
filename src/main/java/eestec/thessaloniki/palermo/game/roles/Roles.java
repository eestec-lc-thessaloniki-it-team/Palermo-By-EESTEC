package eestec.thessaloniki.palermo.game.roles;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class Roles {

    private List<Role> roles;
    
    @Inject
    MurdererRole murderer;
    
    @Inject
    VictimRole victim;
    
    @Inject
    PolicemanRole policeman;

    public Roles() {
        this.roles = new ArrayList<>();
    }

    @PostConstruct
    private void initializeList() {
        this.roles.add(victim);
        this.roles.add(murderer);
        this.roles.add(policeman);
    }
    
    public List<Role> getRoles(){
        return roles;
    }

}
