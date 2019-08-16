
package eestec.thessaloniki.palermo.rest.user;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
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
    public static WebArchive createDeployment(){
        return ShrinkWrap.create(WebArchive.class,"test.war")
                .addPackage(UserService.class.getPackage())
                .addAsResource("persistence.xml","META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE,"beans.xml");
    }
    
    private User objectUser(){
        User user=new User();
        user.setUsername("panos");
        user.setPassword("1234");
        return user;
    }

    @Test
    public void testSomeMethod() {
        assertTrue(true);
    }
    
    @Before
    public void createUser(){
        System.out.println("Testing create user");
         u=userService.createUser(objectUser());
        assertEquals("panos", u.getUsername());
        assertNotEquals(0, u.getId());
        assertNotEquals("1234", u.getPassword());
        Assume.assumeNotNull(u);
    }
    
    @Test
    public void nothing(){
        System.out.println("Find user by Id");
        User aUser=userService.findUserById(u.getId());
        assertEquals(u, aUser);
    }
    
    @After
    public void deleteUser(){
        System.out.println("Deleting user");
        userService.removeUser(u.getId());
        assertTrue(true);
    }
    
}
