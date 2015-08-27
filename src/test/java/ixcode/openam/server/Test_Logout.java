package ixcode.openam.server;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *       curl \
 *       --request POST \
 *       --header "iplanetDirectoryPro: AQIC5wM2...U3MTE4NA..*" \
 *       --header "Content-Type: application/json" \
 *      "http://loan.example.com:9009/openam/json/sessions/?_action=logout"
 */
public class Test_Logout extends HttpTestBase {

    private OpenAmSession session;

    public Test_Logout() {
        super("http://loan.example.com:9009/");
    }

    @Before
    public void authenticate() throws Exception {
        session = new OpenAmClient(http).authenticate("demo", "changeit");
    }


    /**
     * When OpenAM searches for the header its looking for it in lower case
     * See bug at http://bugster.forgerock.org/jira/browse/OPENAM-6167
     *
     * Doesn't seem to be affected by version of jetty
     * @throws Exception
     */
    @Test
    public void can_logout() throws Exception {
        HttpPost request = new HttpPost(url("openam/json/sessions/?_action=logout"));
        request.addHeader("iPlanetDirectoryPro".toLowerCase(), session.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.stringValue("result"), is(equalTo("Successfully logged out")));
    }

    @Test
    public void can_logout_with_legacy_api() throws Exception {
        HttpPost request = new HttpPost("http://loan.example.com:9009/openam/identity/logout?subjectid=" + session.tokenId());

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(equalTo(200)));
    }
}
