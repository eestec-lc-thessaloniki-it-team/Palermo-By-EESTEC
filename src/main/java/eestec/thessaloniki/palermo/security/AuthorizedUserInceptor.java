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
            User user = (User) invocationContext.getParameters()[0];
            if (user.getUsername().equals("") || user.getPassword().equals("")) {
                return Response.status(400).build();
            }
            User u = userService.findUser(user);
            if (u == null) {
                return Response.status(410, "Wrong username or password").build();
            }
            return invocationContext.proceed();

        } else {
            //for the rest that we want to be connected and have the same token
            System.out.println("Want to check if my credentials are correct");
            for (Object param : invocationContext.getParameters()) {
                System.out.println(param.getClass());
                if (param.getClass().equals(UserToken.class)) { //finding the UserToken in params
                    UserToken userToken = (UserToken) param;
                    System.out.println(userToken.toString());
                    if (userTokenService.isValid(userToken)) {
                        return invocationContext.proceed();
                    } else {
                        return Response.status(401, "Unauthorized").build();
                    }
                }
            }
            return Response.status(400).build();
        }

    }

}
