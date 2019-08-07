
package eestec.thessaloniki.palermo.rest.vote;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "vote")
public class Vote implements Serializable {
    @NotNull
    private int game_id;
    @Id
    private int user_id;
    @NotNull
    private int dead_user_id;

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDead_user_id() {
        return dead_user_id;
    }

    public void setDead_user_id(int dead_user_id) {
        this.dead_user_id = dead_user_id;
    }
    
    
    
    
}
