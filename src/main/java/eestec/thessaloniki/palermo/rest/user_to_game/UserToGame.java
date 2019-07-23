package eestec.thessaloniki.palermo.rest.user_to_game;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "user_to_game")
public class UserToGame implements Serializable {
    
    @Id
    private int user_id;
    @Id
    private int game_id;
    private String role_type;
    private boolean is_dead;
    
    @PrePersist
    private void init(){
        this.role_type="";
        this.is_dead=false;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getRole_type() {
        return role_type;
    }

    public void setRole_type(String role_type) {
        this.role_type = role_type;
    }

    public boolean isIs_dead() {
        return is_dead;
    }

    public void setIs_dead(boolean is_dead) {
        this.is_dead = is_dead;
    }
    
}
