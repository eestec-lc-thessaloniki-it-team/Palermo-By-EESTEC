package eestec.thessaloniki.palermo.security;

import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

@Interceptor
@eestec.thessaloniki.palermo.annotations.interceptors.Leader
@Priority(Interceptor.Priority.APPLICATION)
public class LeaderInterceptor {
    @Inject
    GameService gameService;
    
    @Inject 
    UserToGameService userToGameService;
    
    @AroundInvoke
    public Object checkIfHeIsTheLeader(InvocationContext invocationContext)throws Exception{
        System.out.println("Check if he is the leader, from "+invocationContext.getMethod().getName());
        //There should be a UserToken in the first parameter
        UserToken userToken=(UserToken)invocationContext.getParameters()[0];
        Game game=gameService.searchGameByGameID(userToGameService.findByUserId(userToken.getUser_id()).getGame_id());
        if(game.getLeader_id() == userToken.getUser_id()){
            return invocationContext.proceed();
        }
        return Response.status(401,"You are not the leader of the game").build();
        
        
    }
    
    
}
