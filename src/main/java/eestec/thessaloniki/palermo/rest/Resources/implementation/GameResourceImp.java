package eestec.thessaloniki.palermo.rest.Resources.implementation;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.rest.Resources.interfaces.GameResource;
import eestec.thessaloniki.palermo.rest.connectors.GameConnector;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;


@AuthorizedUser
@GameExists
public class GameResourceImp implements GameResource{
    
    @Inject 
    GameConnector gameConnector;

    @Override
    public Response createGame(UserToken userToken) {
       return gameConnector.createGame(userToken);
    }

    @Override
    public Response joinGame(@PathParam("random_id") String random_id, UserToken userToken) {
        return gameConnector.joinGame(random_id, userToken);
    }

    @Override
    @Leader
    public Response endGame(UserToken userToken) {
        return gameConnector.endGame(userToken);
    }

    @Override
    public Response getUsersInGame(UserToken userToken) {
        return gameConnector.getUsersInGame(userToken);

    }

    @Override
    public Response getGameInfo(UserToken userToken) {
        return gameConnector.gameInfo(userToken);
    }

    @Override
    @Leader
    public Response startGame(UserToken userToken) {
        return gameConnector.startGame(userToken);

    }

}
