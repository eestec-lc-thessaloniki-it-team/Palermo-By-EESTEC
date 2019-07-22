package eestec.thessaloniki.palermo.rest.game;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("game")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GameRest {

    @Inject
    GameService gameService;

    @Path("newGame")
    @POST
    public Response createGame(Game game) {
        while (true) {
            try {
                game.generateRandomId();
                gameService.createGame(game);
                break;
            } catch (ConstraintViolationException exception) {
                exception.printStackTrace();
            }
        }
        return Response.ok(game).build();
    }

    @Path("searchGame/{random_id}")
    @GET
    public Response joinGame(@PathParam("random_id") String random_id) {
        Game game = gameService.searchGameByRandomId(random_id);
        return Response.ok(game).build();
    }

}
