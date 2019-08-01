/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eestec.thessaloniki.palermo.rest.Resources;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("ingame")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AuthorizedUser
@GameExists
public class InGameResource {
    
    @Inject
    GameService gameService;
    
    @Inject
    UserToGameService userToGameService;
    
    @Path("info")
    @POST
    public Response getUserToGame(UserToken userToken){
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        if(userToGame!= null){
            return Response.ok(userToGame).build();
        }else{
            return Response.status(404,"No user in game found").build();
        }
    }
    
    @Path("nextState")
    @POST
    @Leader
    //check if you can change the state
    public Response changeState(UserToken userToken){ 
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        Game game =gameService.searchGameByGameID(userToGame.getGame_id());
        game.nextState();
        gameService.updateGame(game);
        return Response.ok(game).build();
        
    }
    
    
}
