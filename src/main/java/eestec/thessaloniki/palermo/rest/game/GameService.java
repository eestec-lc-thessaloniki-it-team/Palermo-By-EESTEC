package eestec.thessaloniki.palermo.rest.game;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class GameService {

    @PersistenceContext
    EntityManager entityManager;

    public Game createGame(Game game) {
        entityManager.persist(game);
        return game;
    }

    public Game searchGameByRandomId(String random_id) {
        try {
            return entityManager.createQuery("Select g From Game g where g.random_id= :random_id", Game.class)
                    .setParameter("random_id", random_id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void deleteGame(Game game) {
        entityManager.createQuery("DELETE FROM Game g WHERE g.id= :id")
                .setParameter("id", game.getId())
                .executeUpdate();
    }

    public Game searchGameByGameID(int game_id) {
        try {
            return entityManager.createQuery("SELECT g FROM Game g WHERE g.id = :id", Game.class)
                    .setParameter("id", game_id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Game updateGame(Game game){
        entityManager.merge(game);
        return game;
    }

}
