package eestec.thessaloniki.palermo.rest.Resources;

import eestec.thessaloniki.palermo.GeneralTestMethods;
import eestec.thessaloniki.palermo.rest.user.User;
import eestec.thessaloniki.palermo.rest.user_token.UserToken;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.ws.rs.core.Response;
import org.jboss.arquillian.junit.InSequence;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

@RunWith(Arquillian.class)
public class UserResourceTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "palermo.war")
                .addPackages(true, "eestec.thessaloniki.palermo")
                .addPackage(GeneralTestMethods.class.getPackage())
                .addAsResource("roles.txt")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    private URL base;
    private WebTarget webTarget;
    private Client client;

    private static UserToken userToken;

    @Before
    public void init() {
        client = ClientBuilder.newBuilder().build();

    }

    @After
    public void clean() {
        client.close();
    }

    @Test
    @RunAsClient
    @InSequence(0)
    public void testCreateUser() throws MalformedURLException {
        webTarget = client.target(URI.create(new URL(base, "api/v1/user/newUser").toExternalForm()));
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(new User("testing", "1234")));
        userToken=response.readEntity(UserToken.class);
        Assert.assertEquals(200, response.getStatus());
    }
    
    @Test
    @RunAsClient
    @InSequence(1)
    public void testLogOut() throws MalformedURLException {
        webTarget = client.target(URI.create(new URL(base, "api/v1/user/logOut").toExternalForm()));
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(userToken));
        Assert.assertEquals(200, response.getStatus());
    }


    @Test
    @RunAsClient
    @InSequence(3)
    public void logInUser() throws MalformedURLException {
        webTarget = client.target(URI.create(new URL(base, "api/v1/user/logIn").toExternalForm()));
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(new User("testing", "1234")));
        userToken = response.readEntity(UserToken.class);
        Assert.assertEquals(200, response.getStatus());
    }

    @Test
    @RunAsClient
    @InSequence(4)
    public void deleteUser() throws MalformedURLException {
        webTarget = client.target(URI.create(new URL(base, "api/v1/user/deleteUser").toExternalForm()));
        Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(userToken));
        Assert.assertEquals(200, response.getStatus());
    }

}
