package eestec.thessaloniki.palermo.rest.user_to_game;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserToGameService {

    @PersistenceContext
    EntityManager entityManager;

    public UserToGame addUserToGame(UserToGame userToGame) {
        entityManager.persist(userToGame);
        return userToGame;
    }

    public List<Integer> usersInGame(int game_id) {
        List<UserToGame> users_to_game = entityManager.createQuery("Select utg From UserToGame utg where utg.game_id= :game_id")
                .setParameter("game_id", game_id)
                .getResultList();
        List<Integer> users_id = new ArrayList<>();
        users_to_game.forEach((utg) -> {
            users_id.add(utg.getUser_id());
        });
        return users_id;
    }

    public void voteUser(int user_id) {
        try {
            UserToGame userToGame = entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.user_id= :user_id", UserToGame.class)
                    .setParameter("user_id", user_id).getSingleResult();
            userToGame.setVotesFromMurderers(userToGame.getVotesFromMurderers() + 1);
            entityManager.merge(userToGame);
        } catch (NoResultException e) {
        }
    }
    
    public List<UserToGame> getAllPlayesOfType(String roleType){
        return entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.role_type= :role_type",UserToGame.class)
                .setParameter("role_type", roleType)
                .getResultList();
    }
    

    public void deleteUsersFromGame(int game_id) {
        entityManager.createQuery("Delete From UserToGame utg where utg.game_id= :game_id")
                .setParameter("game_id", game_id)
                .executeUpdate();
    }

    public UserToGame findByUserId(int user_id){
        return entityManager.find(UserToGame.class, user_id);
    }
    
    public void logOutUserFromGame(int user_id) {
        entityManager.createQuery("DELETE FROM UserToGame utg WHERE utg.user_id = :user_id")
                .setParameter("user_id", user_id)
                .executeUpdate();
    }
}
