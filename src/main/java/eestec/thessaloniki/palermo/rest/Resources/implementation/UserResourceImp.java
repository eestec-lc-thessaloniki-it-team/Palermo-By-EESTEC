package eestec.thessaloniki.palermo.rest.Resources.implementation;

import eestec.thessaloniki.palermo.annotations.interceptors.AuthorizedUser;
import eestec.thessaloniki.palermo.annotations.interceptors.UserExists;
import eestec.thessaloniki.palermo.rest.Resources.interfaces.UserResource;
import eestec.thessaloniki.palermo.rest.connectors.UserConnector;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.core.Response;


public class UserResourceImp implements UserResource {
    @Inject 
    UserConnector userConnector;

    @Override
    @UserExists
    public Response createUser(User user) {
      return this.userConnector.createUser(user);
    }

    @Override
    @AuthorizedUser
    public Response logInUser(User user) {
        return this.userConnector.logInUser(user);
    }

    @Override
    @AuthorizedUser
    public Response logOutUser(UserToken userToken) {
        return this.userConnector.logOutUser(userToken);

    }

    @Override
    @AuthorizedUser
    public Response deleteUser(UserToken userToken) {
        return this.userConnector.deleteUser(userToken);
    }

    @Override
    public List<User> getUsers() {
        return this.userConnector.getUsers();
    }
}
