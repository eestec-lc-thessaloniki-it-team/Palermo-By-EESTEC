package eestec.thessaloniki.palermo.rest.user;

import eestec.thessaloniki.palermo.rest.user.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Transactional
public class UserService {

    @PersistenceContext
    EntityManager entityManager;
    
    private Query query;

    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    public User findUserByUsername(String username) {
        query= entityManager.createQuery("Select u FROM User u WHERE u.username= :username");
        query.setParameter("username", username);
        List<User> resultList=query.getResultList();
        return resultList.get(0);
        
    }
    
    public User findUser(User user){
        User inBaseUser=findUserByUsername(user.getUsername());
        if(inBaseUser.getPassword().equals(user.hashPassword())){
            return inBaseUser;
        }
        return null;
    }
    
    public List<User> getUsers(){
        return entityManager.createQuery("SELECT u FROM User u",User.class).getResultList();
        
    }

}
