package eestec.thessaloniki.palermo.rest.Resources.interfaces;

import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.util.List;
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
public interface UserResource {
    
    @Path("newUser")
    @POST   
    public Response createUser(User user);

    @Path("logIn")
    @POST
    public Response logInUser(User user);

    @Path("logOut")
    @POST
    public Response logOutUser(UserToken userToken);
    
    
    @Path("deleteUser")
    @POST
    public Response deleteUser(UserToken userToken);

    @Path("list")
    @GET
    public List<User> getUsers() ;
    
}
