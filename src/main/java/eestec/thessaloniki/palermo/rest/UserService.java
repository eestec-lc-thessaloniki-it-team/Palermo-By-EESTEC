package eestec.thessaloniki.palermo.rest;

import eestec.thessaloniki.palermo.rest.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class UserService {

    @PersistenceContext
    EntityManager entityManager;

    public User createUser(User user) {
        entityManager.persist(user);
        return user;
    }

    public User findUserByUsername(String username) {
        return entityManager.find(User.class, username);
    }
    
    public List<User> getUsers(){
        return entityManager.createQuery("SELECT * FROM user",User.class).getResultList();
    }

}
