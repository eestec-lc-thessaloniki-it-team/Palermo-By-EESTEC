package eestec.thessaloniki.palermo.rest.user_to_game;

import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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

    public List<Integer> usersInGame(int game_id) {
        List<UserToGame> users_to_game = this.userToGameList(game_id);
        List<Integer> users_id = new ArrayList<>();
        users_to_game.forEach((utg) -> {
            users_id.add(utg.getUser_id());
        });
        return users_id;
    }

    public List<UserToGame> userToGameList(int game_id) {
        return entityManager.createQuery("Select utg From UserToGame utg where utg.game_id= :game_id")
                .setParameter("game_id", game_id)
                .getResultList();
    }

    public List<UserToGame> getUsersAndMurderersVotes(int game_id) {
        return entityManager.createQuery("SELECT utg.user_id, utg.votesFromMurderers FROM UserToGame utg WHERE utg.game_id =:game_id",
                UserToGame.class)
                .setParameter("game_id", game_id).getResultList();
    }

    public boolean initializeActedAtNight(int game_id) {
        try {
            for (UserToGame utg : this.userToGameList(game_id)) {
                if (utg.getRole_type().equals("Victim")) { //victims no need to take actions
                    utg.setActedAtNight(true);
                } else {
                    utg.setActedAtNight(false);
                }
                utg.setVotesFromMurderers(0);
                this.update(utg);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<UserToGame> getDeadPlayers(UserToGame userToGame) {
        return entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.is_dead= :dead AND utg.game_id= :game_id", UserToGame.class)
                .setParameter("dead", true).setParameter("game_id", userToGame.getGame_id()).getResultList();
    }

    public List<UserToGame> getRolesDead(UserToGame userToGame) {
        List<UserToGame> users = entityManager.createQuery("SELECT utg FROM UserToGame utg WHERE utg.is_dead= :dead AND utg.game_id= :game_id", UserToGame.class)
                .setParameter("dead", true).setParameter("game_id", userToGame.getGame_id()).getResultList();
        for (UserToGame utg : users) {
            if (!utg.isIsDeadVisible()) {
                users.remove(utg);
            }
        }
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
            List<Integer> users_id = this.usersInGame(gameId);
            if (users_id.size() == 1) {
                gameService.deleteGame(game);
            } else {
                if (game.getLeader_id() == user_id) {

                    game.setLeader_id(users_id.get(1));
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

}
