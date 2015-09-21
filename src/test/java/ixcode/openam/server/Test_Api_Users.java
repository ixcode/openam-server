package ixcode.openam.server;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * http://openam.27691.n7.nabble.com/Making-REST-calls-from-a-non-super-user-td139.html
 * https://forgerock.org/topic/restricting-the-crud-permissions-to-the-administrators/
 * https://forgerock.org/topic/issue-with-openam-rest-calls/
 * http://openam.27691.n7.nabble.com/Making-REST-calls-from-a-non-super-user-td139.html
 * https://forgerock.org/topic/restricting-the-crud-permissions-to-the-administrators/
 * https://forgerock.org/topic/issue-with-openam-rest-calls/
 * @throws Exception
 */
public class Test_Api_Users extends HttpTestBase {


    private OpenAmSession userSession;
    private OpenAmSession adminSession;

    public Test_Api_Users() {
        super("http://loan.example.com:9009");
    }


    @Before
    public void authenticate() {
        OpenAmClient openAmClient = new OpenAmClient(http);
        userSession = openAmClient.authenticate("demo", "changeit");
        adminSession = openAmClient.authenticate("amadmin", "openamdemonstration");

    }

    @After
    public void logout_user() {
        userSession.logout();
    }

    @After
    public void logout_admin() {
        adminSession.logout();
    }


    @Test
    public void get_user_info() throws Exception {

        HttpGet request = new HttpGet(url("/openam/json/users/demo?_prettyPrint=true"));
        request.addHeader("iPlanetDirectoryPro", userSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

    @Test
    public void get_user_info_legacy() throws Exception {
        HttpPost request = new HttpPost(url("/openam/identity/attributes?subjectid=%s", userSession.tokenId()));

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }


    /**
     * Need to have a group called 'workers' created - there is an api for this but it would be quite complex and difficult
     * to make it smooth. So you need to create it yourself.
     *
     * @throws Exception
     */
    @Test
    public void get_group_info() throws Exception {

        HttpGet request = new HttpGet(url("/openam/json/groups/workers?_prettyPrint=true"));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

    @Test
    public void get_user_id_from_session() throws Exception {

        HttpPost request = new HttpPost(url("/openam/json/users?_action=idFromSession"));
        request.addHeader("iPlanetDirectoryPro", userSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }



}
