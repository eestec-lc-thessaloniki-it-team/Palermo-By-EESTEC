
package eestec.thessaloniki.palermo.wrappers;

import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;


public class WrapperUsersMurdererVotes {

    private int user_id;
    private int murderer_votes;
    private String username;
    
    
    public WrapperUsersMurdererVotes(UserToGame userToGame,String username){
        this.user_id=userToGame.getUser_id();
        this.murderer_votes=userToGame.getVotesFromMurderers();
        this.username=username;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMurderer_votes() {
        return murderer_votes;
    }

    public void setMurderer_votes(int murderer_votes) {
        this.murderer_votes = murderer_votes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "WrapperUsersMurdererVotes{" + "user_id=" + user_id + ", murderer_votes=" + murderer_votes + ", username=" + username + '}';
    }
    
    
    
}
