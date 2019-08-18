package eestec.thessaloniki.palermo.rest.user;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserServiceTest {
    
    @Inject
    UserService userService;
    
    private User u;
    
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(UserService.class.getPackage())
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    
    private User objectUser() {
        User user = new User();
        user.setUsername("panos");
        user.setPassword("1234");
        return user;
    }
    
    @Test
    public void testSomeMethod() {
        assertTrue(true);
    }
    
    @Before
    public void createUser() {
        System.out.println("Testing create user");
        u = userService.createUser(objectUser());
        assertEquals("panos", u.getUsername());
        assertNotEquals(0, u.getId());
        assertNotEquals("1234", u.getPassword());
        Assume.assumeNotNull(u);
    }
    
    @Test
    public void addSameUsername() {
        try {
            userService.createUser(objectUser());
            assertTrue(false);
        } catch (PersistenceException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void findUserById() {
        User aUser = userService.findUserById(u.getId());
        assertEquals(u, aUser);
    }
    
    @Test
    public void findUsername() {
        User aUser = userService.findUserByUsername(u.getUsername());
        assertEquals(u, aUser);
    }
    
    @Test
    public void findUser() {
        User aUser = new User();
        aUser.setPassword("1234");
        aUser.setUsername("panos");
        assertEquals(u, userService.findUser(aUser));
    }
    
    @Test
    public void getUsers() {
        List<User> usesrs = userService.getUsers();
        assertTrue(usesrs.size() > 0);
    }
    
    @After
    public void deleteUser() {
        userService.removeUser(u.getId());
        assertTrue(true);
    }
    
}
