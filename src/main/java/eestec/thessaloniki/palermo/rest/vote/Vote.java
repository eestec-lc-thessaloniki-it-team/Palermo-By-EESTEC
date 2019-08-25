
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

    public Vote() {
    }

    public Vote(int game_id, int user_id, int dead_user_id) {
        this.game_id = game_id;
        this.user_id = user_id;
        this.dead_user_id = dead_user_id;
    }

    @Override
    public String toString() {
        return "Vote{" + "game_id=" + game_id + ", user_id=" + user_id + ", dead_user_id=" + dead_user_id + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.game_id;
        hash = 29 * hash + this.user_id;
        hash = 29 * hash + this.dead_user_id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vote other = (Vote) obj;
        if (this.game_id != other.game_id) {
            return false;
        }
        if (this.user_id != other.user_id) {
            return false;
        }
        if (this.dead_user_id != other.dead_user_id) {
            return false;
        }
        return true;
    }
    
    
    
    

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
