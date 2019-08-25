/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eestec.thessaloniki.palermo.wrappers;

import eestec.thessaloniki.palermo.rest.user_token.UserToken;

/**
 *
 * @author mavroudo
 */
public class WrapperUserTokenVote {
    
    private UserToken userToken;
    private int deadUser_id;

    public WrapperUserTokenVote() {
    }

    public WrapperUserTokenVote(UserToken userToken, int deadUser_id) {
        this.userToken = userToken;
        this.deadUser_id = deadUser_id;
    }
    
    

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }

    public int getDeadUser_id() {
        return deadUser_id;
    }

    public void setDeadUser_id(int deadUser_id) {
        this.deadUser_id = deadUser_id;
    }
    
    
}
