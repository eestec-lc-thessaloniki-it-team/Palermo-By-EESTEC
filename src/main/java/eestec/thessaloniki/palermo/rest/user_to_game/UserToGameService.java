package eestec.thessaloniki.palermo.rest.user_to_game;

import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserToGameService {

    @PersistenceContext
    EntityManager entityManager;

    @Inject
    GameService gameService;

    public UserToGame addUserToGame(UserToGame userToGame) {
        entityManager.persist(userToGame);
        return userToGame;
    }

    public List<UserToGame> userToGameList(int game_id) {
        return entityManager.createQuery("Select utg From UserToGame utg where utg.game_id= :game_id")
                .setParameter("game_id", game_id)
                .getResultList();
    }

    public List<UserToGame> getUsersAndMurderersVotes(int game_id) {
        return entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.game_id =:game_id",
                UserToGame.class)
                .setParameter("game_id", game_id).getResultList();
    }



    public List<UserToGame> getDeadPlayers(UserToGame userToGame) {
        return entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.is_dead= :dead AND utg.game_id= :game_id", UserToGame.class)
                .setParameter("dead", true).setParameter("game_id", userToGame.getGame_id()).getResultList();
    }

    public List<UserToGame> getRolesDead(UserToGame userToGame) {
        List<UserToGame> users = this.getDeadPlayers(userToGame);
        Iterator<UserToGame> i = users.iterator();
        while (i.hasNext()) {
            UserToGame utg = i.next();
            if (!utg.isIsDeadVisible()) {
                i.remove();
            }
        }
        System.out.println("all dead people" + users.toString());
        return users;
    }

    public List<UserToGame> getRoles(UserToGame userToGame) {
        return entityManager.createQuery("SELECT utg WHERE utg.is_dead= :dead AND utg.game_id= :game_id", UserToGame.class)
                .setParameter("dead", true).setParameter("game_id", userToGame.getGame_id()).getResultList();
    }

    public List<UserToGame> getAllPlayesOfType(String roleType, int game_id) {
        return entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.role_type= :role_type AND utg.game_id= :game_id", UserToGame.class)
                .setParameter("role_type", roleType).setParameter("game_id", game_id)
                .getResultList();
    }

    public void deleteUsersFromGame(int game_id) {
        entityManager.createQuery("Delete From UserToGame utg where utg.game_id= :game_id")
                .setParameter("game_id", game_id)
                .executeUpdate();
    }

    public UserToGame findByUserId(int user_id) {
        return entityManager.find(UserToGame.class, user_id);
    }

    public void logOutUserFromGame(int user_id) {
        try {
            int gameId = this.findByUserId(user_id).getGame_id();
            Game game = gameService.searchGameByGameID(gameId);
            List<UserToGame> users_id = this.userToGameList(gameId);
            if (users_id.size() == 1) {
                gameService.deleteGame(game);
            } else {
                if (game.getLeader_id() == user_id) {

                    game.setLeader_id(users_id.get(1).getUser_id());
                    gameService.updateGame(game);
                }
            }
        } catch (NullPointerException e) {
        } finally {
            entityManager.createQuery("DELETE FROM UserToGame utg WHERE utg.user_id = :user_id")
                    .setParameter("user_id", user_id)
                    .executeUpdate();
        }
    }

    public UserToGame update(UserToGame userToGame) {
        entityManager.merge(userToGame);
        return userToGame;
    }

    public JsonArray getWhoWon(int game_id) {
        JsonArrayBuilder jsonWonBuilder = Json.createArrayBuilder();
        for (UserToGame utg : this.userToGameList(game_id)) {
            if (utg.isHas_won()) {
                jsonWonBuilder.add(Json.createObjectBuilder().add("user", utg.getUser_id()).add("role", utg.getRole_type()));
            }
        }
        return jsonWonBuilder.build();
    }

    /**
     * It will return the userToGame record of the next payer who is voting
     *
     * @param game_id
     * @return either the userToGame record if that exists, or null if the
     * voting is over
     */
    public UserToGame getWhoIsVoting(int game_id) {
        try {
            return entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.game_id = :game_id AND utg.is_voting = :is_voting", UserToGame.class)
                    .setParameter("game_id", game_id).setParameter("is_voting", true).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean isVotingOver(int game_id) {
        if(this.getWhoIsVoting(game_id)==null){
            return true;
        }else{
            return false;
        }
    }
}
