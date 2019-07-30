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
    private Timestamp started_date;

    public Game(int leader_id) {
        this.leader_id = leader_id;
    }

    public Game() {
    }

    @PrePersist
    private void init() {
        this.started_date = Timestamp.valueOf(LocalDateTime.now());
        started=false;
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

    public Timestamp getStarted_date() {
        return started_date;
    }

    public void setStarted_date(Timestamp started_date) {
        this.started_date = started_date;
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

    @Override
    public String toString() {
        return "Game{" + "id=" + id + ", leader_id=" + leader_id + ", started=" + started + ", random_id=" + random_id + ", started_date=" + started_date + '}';
    }

    
}
