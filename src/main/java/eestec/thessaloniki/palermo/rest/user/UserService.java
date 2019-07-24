package eestec.thessaloniki.palermo.rest.user;

import eestec.thessaloniki.palermo.rest.user.User;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        try{
            return  entityManager.createQuery("Select u FROM User u WHERE u.username= :username",User.class)
            .setParameter("username", username)
            .getSingleResult();
            
        }catch(NoResultException e){
            return null;
        }

        
    }
    
    public User findUser(User user){
        User inBaseUser=findUserByUsername(user.getUsername());
        if(inBaseUser == null){
            return null;
        }
        if(inBaseUser.getPassword().equals(user.hashPassword())){
            return inBaseUser;
        }
        return null;
    }
    
    public User findUserById(int id){
        query=entityManager.createQuery("SELECT u FROM User u WHERE u.id= :id");
        query.setParameter("id", id);
        return (User) query.getResultList().get(0);
    }
    
    
    public List<User> getUsers(){
        return entityManager.createQuery("SELECT u FROM User u",User.class).getResultList();
        
    }

}
