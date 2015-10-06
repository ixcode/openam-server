package ixcode.openam.client.rest.legacy_rest;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @see http://openam.forgerock.org/doc/bootstrap/dev-guide/index.html#deprecated-apis-auth
 */
public class Test_Rest_Legacy_Authenticate extends HttpTestBase {

    public Test_Rest_Legacy_Authenticate() {
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

    @Test
    public void fails_to_authenticate_invalid_user() {
        HttpPost request = new HttpPost(url("/openam/identity/authenticate?username=demo&password=foo"));

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(HttpStatus.SC_UNAUTHORIZED));

    }
}
