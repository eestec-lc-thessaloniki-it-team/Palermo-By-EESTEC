package eestec.thessaloniki.palermo.game_logic.states;

import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * This will be a class for doing the necessary things while changing between
 * the states
 */
public class ChangeStates {

    @Inject
    UserToGameService userToGameService;

    /**
     * Conditions for every state
     *
     * @param state the new state
     */
    public boolean changeStateTo(String state, UserToGame leader) {
        switch (state) {
            case "Night":
                return this.changeToNight(leader);
            default:
                return false;
        }

    }

    private boolean changeToNight(UserToGame leader) {
        return userToGameService.initializeActedAtNight(leader.getGame_id());
    }
    
    public boolean killAtNight(UserToGame leader){
        return true; //to do one that will choose who died from killers
    }
    
    public boolean hasGameEnded(){
        return false; // to do one that will return if the game has endeed
    }

}
