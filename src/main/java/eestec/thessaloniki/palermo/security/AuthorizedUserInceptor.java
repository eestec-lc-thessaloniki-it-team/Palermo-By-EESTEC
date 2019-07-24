package eestec.thessaloniki.palermo.security;

import eestec.thessaloniki.palermo.annotations.AuthorizedUser;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import eestec.thessaloniki.palermo.rest.user_token.UserTokenService;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

@AuthorizedUser
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class AuthorizedUserInceptor {

    @Inject
    UserTokenService userTokenService;

    @Inject
    UserService userService;

    @AroundInvoke
    public Object authorizedUser(InvocationContext invocationContext) throws Exception {
        if (invocationContext.getMethod().getName().equals("logInUser")) {
            System.out.println("Came here from logIn");
            // you dont want to be already connected
            User user = (User) invocationContext.getParameters()[0];
            user = userService.findUserByUsername(user.getUsername());
            if (userTokenService.isConnected(user.getId())) {
                return Response.status(409, "Already connected").build();
            } else {
                return invocationContext.proceed();
            }
        } else {
            //for the rest that we want to be connected and have the same token
            System.out.println("Want to check if my credentials are correct");
            for (Object param : invocationContext.getParameters()) {
                if (param.getClass().equals(UserToken.class)) { //finding the UserToken in params
                    UserToken userToken=(UserToken)param;
                    if(userTokenService.isValid(userToken)){
                        return invocationContext.proceed();
                    }else{
                        return Response.status(401, "Unauthorized").build();
                    }
                }else{
                    return Response.status(401, "Unauthorized").build();
                }
            }

        }
        return null;
    }

}
