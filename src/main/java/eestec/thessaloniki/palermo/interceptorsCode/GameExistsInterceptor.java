package eestec.thessaloniki.palermo.interceptorsCode;

import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.rest.Resources.implementation.GameResourceImp;
import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenListIds;
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

    @Inject
    UserToGameService userToGameService;

    @AroundInvoke
    public Object checkIfGameExists(InvocationContext invocationContext) throws Exception {
        System.out.println("Check if Game exists: " + invocationContext.getMethod().getName());

        if (invocationContext.getMethod().getName().equals("act")) { // we will check how we will modifie it to make act work
            return this.testAct(invocationContext);
        } else if (invocationContext.getMethod().getDeclaringClass().equals(GameResourceImp.class)) {
            return this.checkInGameResource(invocationContext);
        } else {
            UserToken userToken = (UserToken) invocationContext.getParameters()[0];
            return this.testFromUserToken(userToken, invocationContext);
        }
    }

    private Object checkInGameResource(InvocationContext invocationContext) throws Exception {
        if (invocationContext.getMethod().getName().equals("createGame")) { // create game has no string in the parameters
            return invocationContext.proceed();
        } else if (invocationContext.getMethod().getName().equals("joinGame")) {
            String random_id = invocationContext.getParameters()[0].toString();
            if (gameService.searchGameByRandomId(random_id) == null) { //couldn't find this game
                return Response.status(404, "No game found").build();
            } else {
                return invocationContext.proceed();
            }
        }
        UserToken userToken = (UserToken) invocationContext.getParameters()[0];
        return testFromUserToken(userToken, invocationContext);

    }

    private Object testFromUserToken(UserToken userToken, InvocationContext invocationContext) {
        try {
            Game game = gameService.searchGameByGameID(userToGameService.findByUserId(userToken.getUser_id()).getGame_id());
            return invocationContext.proceed();
        } catch (NullPointerException e) { // couldnt find a record in userToGame table so it will throw a null pointer exception
            return Response.status(400, "You are not logged in in a game").build();
        } catch (Exception ex) {
            return Response.status(500, "Error will checking your request").build();
        }
    }
    
    private Object testAct(InvocationContext invocationContext){
        WrapperUserTokenListIds wrapper=(WrapperUserTokenListIds)invocationContext.getParameters()[0];
        try{
        return this.testFromUserToken(wrapper.getUserToken(), invocationContext);
        }catch(NullPointerException e){
            e.printStackTrace();
            return Response.status(400);
        }
    }

}
