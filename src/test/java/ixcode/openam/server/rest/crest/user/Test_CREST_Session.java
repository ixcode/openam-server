package ixcode.openam.server.rest.crest.user;

import ixcode.openam.server.rest.OpenAmRestApi_TestBase_UserAuthenticated;
import ixcode.platform.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.junit.*;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Basically the only thing you can do here is validate the token
 */
public class Test_CREST_Session extends OpenAmRestApi_TestBase_UserAuthenticated {


    /**
     * Validating tokens has the effect of resetting the idle time of the session.
     * It is possible to use the session api to determine if a session is active without resetting the idle time.
     * @throws Exception
     */
    @Test
    public void validate_token() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/%s/?_action=validate", userSession.tokenId()));
        request.addHeader("iPlanetDirectoryPro", userSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.booleanValue("valid"), is(true));
    }


}
