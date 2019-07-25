package eestec.thessaloniki.palermo.rest.user;

import eestec.thessaloniki.palermo.annotations.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.UserExists;
import eestec.thessaloniki.palermo.rest.user_to_game.UserToGameService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.rest.user_token.UserTokenService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author mavroudo
 */
@Path("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;
    @Inject
    UserTokenService userTokenService;
    @Inject
    UserToGameService userToGameService;

    @Path("newUser")
    @POST
    @UserExists
    public Response createUser(User user) {
        userService.createUser(user);
        UserToken userToken = userTokenService.createUserToken(user.getId());
        return Response.ok(userToken).build();
    }

    @Path("logIn")
    @POST
    @AuthorizedUser
    public Response logInUser(User user) {
        User u = userService.findUser(user);
        if (u != null) {
            UserToken userToken = userTokenService.createUserToken(userService.findUserByUsername(u.getUsername()).getId());
            return Response.ok(userToken).build();
        } else {
            return Response.status(410, "Wrong username or password").build();
        }
    }

    @Path("logOut")
    @POST
    @AuthorizedUser
    public Response logOutUser(UserToken userToken) {
        try {
            userTokenService.deleteUserToken(userToken);
            userToGameService.logOutUserFromGame(userToken.getUser_id());
            return Response.ok().build();
        } catch (Exception anyException) {
            return Response.serverError().build();
        }

    }

    @Path("list")
    @GET
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
