package ixcode.openam.server;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_Api_Tokens extends HttpTestBase {


    private OpenAmSession session;

    public Test_Api_Tokens() {
        super("http://loan.example.com:9009");
    }


    @Before
    public void authenticate() {
        session = new OpenAmClient(http).authenticate("demo", "changeit");
    }

    @After
    public void logout() {
        session.logout();
    }

    @Test
    public void validate_token() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/%s/?_action=validate", session.tokenId()));
        request.addHeader("iPlanetDirectoryPro".toLowerCase(), session.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.booleanValue("valid"), is(true));
    }

    @Test
    public void confirm_session_is_active() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=isActive&tokenId=%s", session.tokenId()));
        request.addHeader("iPlanetDirectoryPro".toLowerCase(), session.tokenId());
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

        HttpGet request = new HttpGet(url("/openam/json/users/demo"));
        request.addHeader("iPlanetDirectoryPro".toLowerCase(), session.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

    @Test
    public void get_user_id_from_session() throws Exception {

        HttpPost request = new HttpPost(url("/openam/json/users?_action=idFromSession"));
        request.addHeader("iPlanetDirectoryPro".toLowerCase(), session.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }


}
