package eestec.thessaloniki.palermo.rest.game;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int leader_id;
    private boolean started;
    private String random_id;
    private Timestamp created_date;
    private String state;
    private boolean is_game_over;

    public Game(int leader_id) {
        this.leader_id = leader_id;
    }

    public Game() {
    }

    @PrePersist
    private void init() {
        this.created_date = Timestamp.valueOf(LocalDateTime.now());
        this.is_game_over = false;
        started = false;
        state = "Night";
    }

    public void generateRandomId() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789";
        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        setRandom_id(sb.toString());

    }

    public Timestamp getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Timestamp created_date) {
        this.created_date = created_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeader_id() {
        return leader_id;
    }

    public void setLeader_id(int leader_id) {
        this.leader_id = leader_id;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public String getRandom_id() {
        return random_id;
    }

    public void setRandom_id(String random_id) {
        this.random_id = random_id;
    }

    public void nextState() {
        if (this.state.equals("Night")) {
            this.state = "Morning";
        } else if (this.state.equals("Morning")) {
            this.state = "Voting";
        } else {
            this.state = "Night";
        }
    }

    public boolean isIs_game_over() {
        return is_game_over;
    }

    public void setIs_game_over(boolean is_game_over) {
        this.is_game_over = is_game_over;
    }

    @Override
    public String toString() {
        return "Game{" + "id=" + id + ", leader_id=" + leader_id + ", started=" + started + ", random_id=" + random_id + ", created_date=" + created_date + ", state=" + state + ", is_game_over=" + is_game_over + '}';
    }

    public String getState() {
        return state;
    }

}
