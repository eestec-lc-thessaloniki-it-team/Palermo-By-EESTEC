package eestec.thessaloniki.palermo.wrappers;

import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.util.List;

public class WrapperUserTokenListIds {


    private UserToken userToken;
    private List<Integer> ids;

    public UserToken getUserToken() {
        return userToken;
    }

    public void setUserToken(UserToken userToken) {
        this.userToken = userToken;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "WrapperUserTokenListIds{" + "userToken=" + userToken + ", ids=" + ids + '}';
    }
    
    
}
