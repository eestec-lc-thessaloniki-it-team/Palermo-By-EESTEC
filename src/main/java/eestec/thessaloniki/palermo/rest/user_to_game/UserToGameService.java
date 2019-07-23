package eestec.thessaloniki.palermo.rest.user_to_game;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Transactional
public class UserToGameService {
    
    @PersistenceContext
    EntityManager entityManager;
    
    public UserToGame addUserToGame(UserToGame userToGame){
        entityManager.persist(userToGame);
        return userToGame;
    }
    
    public List<Integer> usersInGame(int game_id){
        Query query=entityManager.createQuery("Select utg From UserToGame utg where utg.game_id= :game_id");
        query.setParameter("game_id", game_id);
        List<UserToGame> users_to_game=query.getResultList();
        List<Integer> users_id=new ArrayList<>();
        users_to_game.forEach((utg) -> {
            users_id.add(utg.getUser_id());
        });
        return users_id;
    }
    
    public void deleteUsersFromGame(int game_id){
        Query query=entityManager.createQuery("Delete utg From UserToGame utg where utg.game_id= :game_id");
        query.setParameter("game_id", game_id);
    }
}
