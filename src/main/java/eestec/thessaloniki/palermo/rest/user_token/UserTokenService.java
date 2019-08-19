/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eestec.thessaloniki.palermo.rest.user_token;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserTokenService {

    @PersistenceContext
    EntityManager entityManager;

    public UserToken getToken(int user_id) {
        if (this.isConnected(user_id)) {
            return this.updateToken(user_id);
        } else {
            return this.createUserToken(user_id);
        }
    }

    public boolean isValid(UserToken userToken) {
        UserToken persistedToken = entityManager.find(UserToken.class, userToken.getUser_id());
        return persistedToken != null && persistedToken.getToken().equals(userToken.getToken());
    }

    public void deleteUserToken(UserToken userToken) {
        entityManager.createQuery("DELETE FROM UserToken ut WHERE ut.user_id= :user_id")
                .setParameter("user_id", userToken.getUser_id()).executeUpdate();

    }

    public UserToken findById(int user_id) {
        return entityManager.find(UserToken.class, user_id);
    }

    private UserToken createUserToken(int user_id) {
        UserToken userToken = new UserToken(user_id);
        entityManager.persist(userToken);
        return userToken;
    }

    private boolean isConnected(int user_id) {
        try {
            UserToken userToken = (UserToken) entityManager.createQuery("Select ut FROM UserToken ut WHERE ut.user_id= :user_id")
                    .setParameter("user_id", user_id).getSingleResult();
            return true;
        } catch (NoResultException exception) {
            return false;
        }
    }
    
    private UserToken updateToken(int user_id) {
        UserToken userToken = findById(user_id);
        userToken.updateToken();
        entityManager.merge(userToken);
        return userToken;
    }

}
