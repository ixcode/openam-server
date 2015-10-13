package ixcode.openam.client.rest.legacy_rest;

import ixcode.openam.client.rest.OpenAmRestApi_TestBase_UserAuthenticated;
import ixcode.platform.HttpResponse;
import ixcode.platform.UserDetailsResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_Rest_Legacy_Identity extends OpenAmRestApi_TestBase_UserAuthenticated {

    @Test
    public void get_identity_attributes() throws Exception {
        HttpPost request = new HttpPost(url("/openam/identity/attributes?subjectid=%s", userSession.tokenId()));

        HttpResponse response = http.execute(request, new UserDetailsResponseHandler());

        assertThat(response.statusCode(), is(200));

        System.out.println(response.stringValue("userdetails.role"));

    }

}
