package eestec.thessaloniki.palermo.security;

import eestec.thessaloniki.palermo.annotations.UserExists;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

@UserExists
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class UserExistsInterceptor {
    
    @Inject
    UserService userService;
    
    
    @AroundInvoke
    public Object checkIfUserExists(InvocationContext invocationContext) throws Exception{
        User user;
        User parameter;
        Object[] params=invocationContext.getParameters();
        for (Object param :params){
            if(param.getClass().equals(User.class)){
                parameter=(User)param;
                if(parameter.getUsername().equals("") || parameter.getPassword().equals("")){
                    return Response.status(400).build();
                }
                user =userService.findUserByUsername(parameter.getUsername());
                if (user == null){
                    return invocationContext.proceed();
                }else{
                    System.out.println("User with that username: "+parameter.getUsername()+" found in the db");
                    return Response.status(406, "Username already exists in the db").build();
                }
                
            }
        }
        return invocationContext.proceed();
    }
    
}
