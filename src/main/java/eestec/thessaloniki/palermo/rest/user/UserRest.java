/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eestec.thessaloniki.palermo.rest.user;

import eestec.thessaloniki.palermo.annotations.UserExists;
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
public class UserRest {
    
    @Inject
    UserService userService;
    @Inject
    UserTokenService userTokenService;
    
    @Path("newUser")
    @POST
    @UserExists
    public Response createUser(User user){
        userService.createUser(user);
        UserToken userToken=userTokenService.createUserToken(user.getId());
        return Response.ok  (userToken).build();
    }
    
    @Path("logIn")
    @GET
    public Response logInUser(User user){
        User u=userService.findUser(user);
        if (u != null){
           return Response.ok(u).build();
        }else{
            return Response.status(410, "Wrong username or password").build();
        }
    }
    
//    @Path("{username}")
//    @GET
//    public User getUser(@PathParam("username") String username){
//        return userService.findUserByUsername(username);
//    }
//    
    @Path("list")
    @GET
    public List<User> getUsers(){
        return userService.getUsers();
    }
}
