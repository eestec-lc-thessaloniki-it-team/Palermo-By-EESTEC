package eestec.thessaloniki.palermo.rest.vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    
    public void deleteVotes(int game_id){
        try{
        entityManager.createQuery("DELETE FROM Vote v WHERE v.game_id= :game_id ")
                .setParameter("game_id", game_id).executeUpdate();
        }catch(TransactionRequiredException e){
            return;
        }
    }
    
    public void voted(int user_Id){
        Vote vote=entityManager.find(Vote.class,user_Id);
        vote.setVotes(vote.getVotes()+1);
        entityManager.merge(vote);
    }
    
    
    
    
    /**
     * This will return the user id with more votes or one random if there more
     * than 2 in the first place
     *
     * @param gameId game id that we need results
     * @return the player that will die
     */
    public int getWhoDied(int gameId) {
        List<Vote> votes = this.getCurrentVotes(gameId);
        List<Integer> users_id = new ArrayList<>();
        for (Vote v : votes) {
            users_id.add(v.getVotes());
        }
        return this.getDead(users_id);
    }
    

    private int getDead(List<Integer> users_id) {
        Map<Integer, Integer> map = new HashMap<>();

        for (Integer t : users_id) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<Integer, Integer> max = null;

        for (Entry<Integer, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }

        return max.getKey();

    }

}
