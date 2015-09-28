package ixcode.openam.server.rest.crest.user;

import ixcode.openam.server.rest.OpenAmRestApi_TestBase_UserAuthenticated;
import ixcode.platform.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * In order to retrieve the user details from a session you would need to make 2 calls, get user id and then get user details
 * Probably you'd want to cache this.
 */
public class Test_CREST_User extends OpenAmRestApi_TestBase_UserAuthenticated {


    @Test
    public void get_user_id_from_session() throws Exception {

        HttpPost request = new HttpPost(url("/openam/json/users?_action=idFromSession"));
        request.addHeader("iPlanetDirectoryPro", userSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }


    /**
     * This doesn't return the group info as yet, see the legacy_rest user info (Identity) api
     * @See https://forgerock.org/topic/getting-the-users-groupsrole-using-rest-api-in-newer-version-of-apis12/#post-5495
     * @throws Exception
     */
    @Test
    public void get_user_info() throws Exception {

        HttpGet request = new HttpGet(url("/openam/json/users/demo?_prettyPrint=true"));
        request.addHeader("iPlanetDirectoryPro", userSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }


}
