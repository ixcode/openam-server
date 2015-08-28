package ixcode.openam.server;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.*;

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
public class Test_Api_Tokens extends HttpTestBase {


    private OpenAmSession session;
    private OpenAmSession adminSession;

    public Test_Api_Tokens() {
        super("http://loan.example.com:9009");
    }


    @Before
    public void authenticate() {
        session = new OpenAmClient(http).authenticate("demo", "changeit");
        adminSession = new OpenAmClient(http).authenticate("amadmin", "openamdemonstration");
    }

    @After
    public void logout_user() {
        session.logout();
    }

    @After
    public void logout_admin() {
        adminSession.logout();
    }

    @Test
    public void validate_token() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/%s/?_action=validate", session.tokenId()));
        request.addHeader("iPlanetDirectoryPro", session.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.booleanValue("valid"), is(true));
    }

    @Test
    public void confirm_session_is_active() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=isActive&tokenId=%s", session.tokenId()));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }


    @Test
    public void max_time() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=getMaxTime&tokenId=%s", session.tokenId()));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

    @Test
    public void idle_time_remaining() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=getIdle&tokenId=%s", session.tokenId()));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }


    /**
     *
     *
     * @throws Exception
     */
    @Test
    public void get_user_info() throws Exception {

        HttpGet request = new HttpGet(url("/openam/json/users/demo?_prettyPrint=true"));
        request.addHeader("iPlanetDirectoryPro", session.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

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
        request.addHeader("iPlanetDirectoryPro", session.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }



}
