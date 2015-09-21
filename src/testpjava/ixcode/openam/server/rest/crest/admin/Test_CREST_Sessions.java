package ixcode.openam.server.rest.crest.admin;

import ixcode.platform.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * http://openam.27691.n7.nabble.com/Making-REST-calls-from-a-non-super-user-td139.html
 * https://forgerock.org/topic/restricting-the-crud-permissions-to-the-administrators/
 * https://forgerock.org/topic/issue-with-openam-rest-calls/
 * http://openam.27691.n7.nabble.com/Making-REST-calls-from-a-non-super-user-td139.html
 * https://forgerock.org/topic/restricting-the-crud-permissions-to-the-administrators/
 * https://forgerock.org/topic/issue-with-openam-rest-calls/
 *
 * All session calls can only be consumed by a super user, hence we need an admin session aswell as the user session
 *
 * @throws Exception
 */
public class Test_CREST_Sessions extends OpenAmRestApi_TestBase_AdminAuthenticated {

    @Test
    public void is_active() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=isActive&tokenId=%s", userSession.tokenId()));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

    /**
     * The default is to NOT reset idle time using this api.
     * @throws Exception
     */
    @Test
    public void is_active_resetting_idle_time() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=isActive&tokenId=%s&refresh=true", userSession.tokenId()));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }


    @Test
    public void max_time() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=getMaxTime&tokenId=%s", userSession.tokenId()));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

    @Test
    public void idle_time_remaining() throws Exception {
        HttpPost request = new HttpPost(url("/openam/json/sessions/?_action=getIdle&tokenId=%s", userSession.tokenId()));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

}
