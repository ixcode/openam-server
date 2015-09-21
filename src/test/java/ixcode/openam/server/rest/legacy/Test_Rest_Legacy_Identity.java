package ixcode.openam.server.rest.legacy;

import ixcode.openam.server.rest.OpenAmRestApi_TestBase_UserAuthenticated;
import ixcode.platform.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_Rest_Legacy_Identity extends OpenAmRestApi_TestBase_UserAuthenticated {

    @Test
    public void get_identity_attributes() throws Exception {
        HttpPost request = new HttpPost(url("/openam/identity/attributes?subjectid=%s", userSession.tokenId()));

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }

}
