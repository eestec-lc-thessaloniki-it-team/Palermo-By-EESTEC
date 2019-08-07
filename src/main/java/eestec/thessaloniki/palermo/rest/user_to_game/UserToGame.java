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
    private int game_id;
    private String role_type;
    private boolean is_dead;
    private int votesFromMurderers;
    private boolean isDeadVisible;
    private boolean actedAtNight;

    public UserToGame(int user_id, int game_id) {
        this.user_id = user_id;
        this.game_id = game_id;
    }

    public UserToGame() {
    }

    @PrePersist
    private void init() {
        this.role_type = "";
        this.is_dead = false;
        this.votesFromMurderers = 0;
        this.actedAtNight = false;
        this.isDeadVisible = true;
    }

    public UserToGame hideInfo() {
        this.votesFromMurderers = 0;
        this.role_type = "";
        this.actedAtNight = false;
        this.isDeadVisible = false;
        return this;
    }

    public UserToGame deadRoles() {
        if (!this.is_dead || !this.isDeadVisible) {
            this.is_dead = false;
            this.user_id = 0;
            this.isDeadVisible = false;
            this.actedAtNight = false;
            this.votesFromMurderers = 0;
        }
        return this;
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

    public int getVotesFromMurderers() {
        return votesFromMurderers;
    }

    public void setVotesFromMurderers(int votesFromMurderers) {
        this.votesFromMurderers = votesFromMurderers;
    }

    public boolean isIsDeadVisible() {
        return isDeadVisible;
    }

    public void setIsDeadVisible(boolean isDeadVisible) {
        this.isDeadVisible = isDeadVisible;
    }

    public boolean isActedAtNight() {
        return actedAtNight;
    }

    public void setActedAtNight(boolean actedAtNight) {
        this.actedAtNight = actedAtNight;
    }

    @Override
    public String toString() {
        return "UserToGame{" + "user_id=" + user_id + ", game_id=" + game_id + ", role_type=" + role_type + ", is_dead=" + is_dead + ", votesFromMurderers=" + votesFromMurderers + ", isDeadVisible=" + isDeadVisible + ", actedAtNight=" + actedAtNight + '}';
    }

}
