package eestec.thessaloniki.palermo.security;

import eestec.thessaloniki.palermo.annotations.UserExists;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user.UserService;
import java.util.Arrays;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@UserExists
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class UserExistsInterceptor {
    
    @Inject
    UserService userService;
    
    
    @AroundInvoke
    public Object checkIfUserExists(InvocationContext invocationContext) throws Exception{
        System.out.println("Checking if user exists"); 
        User user;
        User parameter;
        Object[] params=invocationContext.getParameters();
        for (Object param :params){
            if(param.getClass().equals(User.class)){
                parameter=(User)param;
                System.out.println(parameter.getUsername());
                user =userService.findUserByUsername(parameter.getUsername());
                if (user == null){
                    System.out.println("No user with that username: "+parameter.getUsername()+" found in the db");
                    return invocationContext.proceed();
                }else{
                    System.out.println("User with that username: "+parameter.getUsername()+" found in the db");
                    return null;
                }
                
            }
        }
        return invocationContext.proceed();
    }
    
}
