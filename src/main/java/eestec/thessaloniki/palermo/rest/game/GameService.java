package eestec.thessaloniki.palermo.rest.game;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
        Query query = entityManager.createQuery("Select g From Game g where g.random_id= :random_id");
        query.setParameter("random_id", random_id);
        List<Game> games = query.getResultList();
        return games.get(0);
    }

}
