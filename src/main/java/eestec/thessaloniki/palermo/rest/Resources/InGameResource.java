package eestec.thessaloniki.palermo.rest.Resources;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.GameExists;
import eestec.thessaloniki.palermo.annotations.interceptors.Leader;
import eestec.thessaloniki.palermo.game.states.ChangeStates;
import eestec.thessaloniki.palermo.game.states.NightState;
import eestec.thessaloniki.palermo.rest.game.Game;
import eestec.thessaloniki.palermo.rest.game.GameService;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGame;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.wrappers.WrapperUserTokenListIds;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
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
    
    @Inject  GameService gameService;
    @Inject UserToGameService userToGameService;
    @Inject  NightState nightService;    
    @Inject ChangeStates changeStates;
    
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
        changeStates.changeStateTo(game.getState(), userToGame);
        return Response.ok(gameService.searchGameByGameID(userToGame.getGame_id())).build();
        
    }
    
    @Path("deadPlayers")
    @POST
    public Response getDeadPlayers(UserToken userToken){
        UserToGame utg=userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getDeadPeople(utg);
    }
    
    @Path("deadRoles")
    @POST
    public Response getDeadRoles(UserToken userToken){
        UserToGame utg=userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getDeadRoles(utg);
    }
    
    @Path("allRoles")
    @POST
    public Response getAllRoles(UserToken userToken){
        UserToGame utg=userToGameService.findByUserId(userToken.getUser_id());
        return changeStates.getAllRoles(utg);
    }
    
    /**
     * This will inform users for the state of the game
     * @param userToken the users that asks for it  
     * @return a json that will contain the state of the game, if the game has ended and if thats true it will also contains
     * a list of players that won with their roles
     */
    @Path("state")
    @POST
    public Response getState(UserToken userToken){
        UserToGame userToGame = userToGameService.findByUserId(userToken.getUser_id());
        Game game =gameService.searchGameByGameID(userToGame.getGame_id());
        JsonObjectBuilder jsonObjectBuilder= Json.createObjectBuilder()
                 .add("state", game.getState());
        jsonObjectBuilder.add("is_game_over", game.isIs_game_over());
        if(game.isIs_game_over()){
            jsonObjectBuilder.add("won", userToGameService.getWhoWon(game.getId()));
        }
        return Response.ok(jsonObjectBuilder.build()).build(); 
    }
    
    @Path("roleInfo")
    @POST
    public Response getRoleJson(UserToken userToken){
        return nightService.getRoleJson(userToken);
    }
    
    
    @Path("act")
    @POST
    public Response act(WrapperUserTokenListIds wrapper ){
        return nightService.act(wrapper.getUserToken(), wrapper.getIds());
    }
    
    // the info that some roles might need to return
    @Path("nightInfo")
    @POST
    public Response nightInfo(UserToken userToken){
        return nightService.info(userToken);
        
    }

    
    
}
