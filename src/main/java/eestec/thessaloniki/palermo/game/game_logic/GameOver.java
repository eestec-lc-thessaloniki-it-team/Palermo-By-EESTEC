package eestec.thessaloniki.palermo.game.game_logic;

import eestec.thessaloniki.palermo.game.roles.Role;
import eestec.thessaloniki.palermo.game.roles.Roles;
import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * This method will return if the game is over and which players won return Null
 * if the game is still on
 */
public class GameOver {

    @Inject
    UserToGameService userToGameService;
    @Inject
    GameService gameService;
    @Inject
    Roles roles;

    /**
     * This method will return a list of users that won the game. If it is null
     * then the game is still on
     *
     * @param game_id the game that we need to test if it is over
     * @return if the game is over or not
     */
    public boolean isGameOver(int game_id) {
        try {
            List<UserToGame> users = userToGameService.userToGameList(game_id);
            List<UserToGame> aliveUsers = this.alivePlayers(users);
            Game game = gameService.searchGameByGameID(game_id);
            List<UserToGame> winners = this.winConditions(aliveUsers, game.getState());
            if (winners == null) {
                return false;
            }
            game.setIs_game_over(true);
            gameService.updateGame(game);
            this.persistToDB(winners);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    /**
     *
     * @param alivePlayers all the players that still leave in the game
     * @param state is after the change so we have to look the previous thing
     * @return who of the alive players and won and
     */
    private List<UserToGame> winConditions(List<UserToGame> alivePlayers, String state) {
        if (state.equals("Voting")) {
            return null;
        }

        List<List> lists = this.countEachType(alivePlayers);
        if (lists.get(0).size() == 1 && lists.get(1).size() == 1 && state.equals("Morning")) {
            lists.get(0).addAll(lists.get(1));
            return lists.get(0);
        }
        if (state.equals("Night") && lists.get(0).size() == 1 && lists.get(1).size() > 0) {// this is night and the murderers will kill the good one
            return lists.get(1);
        }
        if (lists.get(1).size() == 0) { // all the murderers are dead
            return lists.get(0);
        }
        if (lists.get(0).size() == 0) { // all the good roles are dead
            return lists.get(1);
        }

        return null;

    }

    private void persistToDB(List<UserToGame> winners) {
        for (UserToGame utg : winners) {
            utg.setHas_won(true);
            userToGameService.update(utg);
        }
    }

    private List<UserToGame> alivePlayers(List<UserToGame> users) {
        List<UserToGame> aliveUsers = new ArrayList<>();
        Iterator<UserToGame> i = users.iterator();
        for (UserToGame utg : users) {
            if (!utg.isIs_dead()) {
                aliveUsers.add(utg);
            }
        }
        return aliveUsers;
    }

    private List<List> countEachType(List<UserToGame> users) {
        List<List> lists = new ArrayList<>();
        lists.add(new ArrayList<UserToGame>());
        lists.add(new ArrayList<UserToGame>());
        lists.add(new ArrayList<UserToGame>());
        for (UserToGame utg : users) {
            for (Role role : roles.getRoles()) {
                if (role.getRoleName().equals(utg.getRole_type())) {
                    if (role.getRoleTeam().equals(Role.RoleTeam.GOOD)) {
                        lists.get(0).add(utg);
                    } else if (role.getRoleTeam().equals(Role.RoleTeam.BAD)) {
                        lists.get(1).add(utg);
                    } else {
                        lists.get(2).add(utg);
                    }
                }
            }
        }
        return lists;
    }

}
