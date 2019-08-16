package eestec.thessaloniki.palermo.rest.user;

import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserTest {
    @Inject
    User user;
    
    @Deployment
    public static JavaArchive createDeployment(){
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(User.class).addAsManifestResource(EmptyAsset.INSTANCE,"beans.xml");
    }

    @Test
    public void testSomeMethod() {
        System.out.println(user.toString());
        assertTrue(true);
    }
    
}
