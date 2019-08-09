package eestec.thessaloniki.palermo.game.game_logic;

import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;

/**
 *
 *  Here will be set all the logic from what took place at night, who died
 */
public class WhoDied {
    
    @Inject
    UserToGameService userToGameService;
    
    
    
    public void kill(UserToGame leader){
        this.whoDiedFromKillers(leader);
    }
    
    /**
     * This method will decide who will die from the users depending on murderers vote. The person with more votes
     * or one of the persons with more votes , picked randomly
     * @param leader the userToGame record from the leader of the game
     * @return the new persisted userToGame from the user who died
     */
    private UserToGame whoDiedFromKillers(UserToGame leader){
        System.out.println("Murderes are killing now");
        if(userToGameService==null){
            return null;
        }
        List<UserToGame> users=userToGameService.getUsersAndMurderersVotes(leader.getGame_id());
        System.out.println(users.toString());
        Collections.sort(users,new Comparator<UserToGame>() {
            @Override
            public int compare(UserToGame o2, UserToGame o1) {
                return Integer.compare(o1.getVotesFromMurderers(), o2.getVotesFromMurderers());
            }
        });
        System.out.println(users.toString());
        if(users.get(0).getVotesFromMurderers()==0){
            return users.get(0);
        }
       users.get(0).setIs_dead(true);
       users.get(0).setIsDeadVisible(false);
       return userToGameService.update(users.get(0));
    }
    
    //Here will be added a new function that will take care of all the the other roles at night that can kill or save
    
}
