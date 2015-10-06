package ixcode.openam.client.rest.legacy_rest;

import ixcode.openam.client.OpenAmClient;
import ixcode.openam.client.OpenAmSession;
import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_Rest_Legacy_Logout extends HttpTestBase {

    private OpenAmSession session;

    public Test_Rest_Legacy_Logout() {
        super("http://loan.example.com:9009/");
    }

    @Before
    public void authenticate() throws Exception {
        session = new OpenAmClient(http).authenticate("demo", "changeit");
    }


    @Test
    public void can_logout() {
        HttpPost request = new HttpPost(url("/openam/identity/logout?subjectid=%s",  session.tokenId()));

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(equalTo(200)));
    }
}
