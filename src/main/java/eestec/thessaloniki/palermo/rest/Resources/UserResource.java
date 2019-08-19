package eestec.thessaloniki.palermo.rest.Resources;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.UserExists;
import eestec.thessaloniki.palermo.game.game_logic.GiveRoles;
import eestec.thessaloniki.palermo.game.roles.Roles;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
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
    
  
    @Inject
    Roles roles;
    
    @Inject
    GiveRoles giveRoles;

    @Path("newUser")
    @POST
    @UserExists
    public Response createUser(User user) {
        userService.createUser(user);
        UserToken userToken = userTokenService.getToken(user.getId());
        return Response.ok(userToken).build();
    }

    @Path("logIn")
    @POST
    @AuthorizedUser
    public Response logInUser(User user) {
        User u = userService.findUser(user);
        return Response.ok(userTokenService.getToken(u.getId())).build();

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
            anyException.printStackTrace();
            return Response.serverError().build();
        }

    }
    
    
    @Path("deleteUser")
    @POST
    @AuthorizedUser
    public Response deleteUser(UserToken userToken){
        try{
            userTokenService.deleteUserToken(userToken);
            userToGameService.logOutUserFromGame(userToken.getUser_id());
            userService.removeUser(userToken.getUser_id());
            return Response.ok().build();
        }catch(Exception exception){
            System.out.println("Problem with deleting user");
            exception.printStackTrace();
            return Response.serverError().build();
        }
    }

    @Path("list")
    @GET
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
