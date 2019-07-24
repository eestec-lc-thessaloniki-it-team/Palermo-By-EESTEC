package eestec.thessaloniki.palermo.rest.user_token;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Base64;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "user_token")
public class UserToken implements Serializable {

    @Id
    private int user_id;
    private String token;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public UserToken() {
    }

    public UserToken(int user_id) {
        this.user_id = user_id;
    }

    
    
    
    @PrePersist
    private void init() {
        this.token=generateNewToken();
    }

    //threadsafe
    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
