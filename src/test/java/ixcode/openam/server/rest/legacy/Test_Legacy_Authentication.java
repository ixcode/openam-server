package ixcode.openam.server.rest.legacy;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class Test_Legacy_Authentication extends HttpTestBase {

    public Test_Legacy_Authentication() {
        super("http://loan.example.com:9009");
    }

    /**
     * curl \
     * --request POST \
     * --data "username=demo&password=changeit" \
     * https://loans.example.com:9009/openam/identity/authenticate
     */
    @Test
    public void can_authenticate_valid_user() {
        HttpPost request = new HttpPost(url("/openam/identity/authenticate?username=demo&password=changeit"));

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        assertThat("Should have responded with a token id", response.stringValue("token.id"), is(notNullValue()));
    }
}
