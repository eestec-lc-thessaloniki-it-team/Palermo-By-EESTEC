package eestec.thessaloniki.palermo.rest.vote;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;
import javax.transaction.Transactional;

@Transactional
public class VoteService {

    @PersistenceContext
    EntityManager entityManager;

    public Vote insert(Vote vote) {
        entityManager.persist(vote);
        return vote;
    }

    public List<Vote> getCurrentVotes(int game_id) {
            return entityManager.createQuery("SELECT v FROM Vote v WHERE v.game_id = :game_id ORDER BY v.votes DESC", Vote.class)
                    .setParameter("game_id", game_id).getResultList();
    }

    public boolean isVotingOver(int game_id) {
        if (this.getCurrentVotes(game_id).size()==0 ) {
            return true;
        }
        return false;
    }

    public void deleteVotes(int game_id) {
        try {
            entityManager.createQuery("DELETE FROM Vote v WHERE v.game_id= :game_id ")
                    .setParameter("game_id", game_id).executeUpdate();
        } catch (TransactionRequiredException e) {
            return;
        }
    }

    public void voted(int user_Id) {
        Vote vote = entityManager.find(Vote.class, user_Id);
        vote.setVotes(vote.getVotes() + 1);
        entityManager.merge(vote);
    }

}
