package eestec.thessaloniki.palermo.security;

import eestec.thessaloniki.palermo.annotations.GameExists;
import eestec.thessaloniki.palermo.rest.game.GameResource;
import eestec.thessaloniki.palermo.rest.game.GameService;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

@Interceptor
@GameExists
@Priority(Interceptor.Priority.APPLICATION)
public class GameExistsInterceptor {

    @Inject
    GameService gameService;

    @AroundInvoke
    public Object checkIfGameExists(InvocationContext invocationContext) throws Exception {
        System.out.println("Check if random id exists");
        if (invocationContext.getMethod().getDeclaringClass().equals(GameResource.class) ) {
            if( invocationContext.getMethod().getName().equals("createGame")){ // create game has no string in the parameters
                return invocationContext.proceed();
            }
            //methods from thsi class has as first parameter the random id from the game, check if this exists
            String random_id = invocationContext.getParameters()[0].toString();
            if (gameService.searchGameByRandomId(random_id) == null) { //couldn't find this game
                return Response.status(404, "No game found").build();
            }

        }

        return invocationContext.proceed();
    }

}
