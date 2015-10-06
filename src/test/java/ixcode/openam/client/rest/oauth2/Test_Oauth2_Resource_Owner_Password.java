package ixcode.openam.client.rest.oauth2;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static ixcode.platform.Http.addBasicAuthzHeader;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @See https://tools.ietf.org/html/rfc6749#section-1.3.3
 * <p/>
 * Matches the case where the client application passes the credentials directly.
 * <p/>
 * We need to be sure that everything is under our control in this case.
 * <p/>
 * There is no redirection here.
 * <p/>
 * It's equivalent to using the rest endpoints except that the client must be registered.
 * <p/>
 * It would not be very useful in a situation where the client was running in the browser as then the client credentials would
 * need to be stored there.
 */
public class Test_Oauth2_Resource_Owner_Password extends HttpTestBase {

    public Test_Oauth2_Resource_Owner_Password() {
        super("http://loan.example.com:9009");
    }

    /**
     * curl \
     * --request POST \
     * --user "myClientID:password" \
     * --data "grant_type=password&username=demo&password=changeit&scope=cn%20mail" \
     * https://loan.example.com:9009/openam/oauth2/access_token
     * <p/>
     * You will need to have create an oauth client called testClient with changeme password
     * <p/>
     * First create the config in the home screen "oauth / openid connect" then
     * <p/>
     * http://loan.example.com:9009/openam/agentconfig/Agents
     * <p/>
     * You also need to add the scopes in the agent config
     *
     * https://backstage.forgerock.com/#!/docs/openam/10.1.0/dev-guide/chap-oauth2-scopes
     */
    @Test
    public void can_login_and_retrieve_token_info() {


        HttpPost request = new HttpPost(url("/openam/oauth2/access_token?grant_type=password&username=demo&password=changeit&scope=cn+mail"));
        addBasicAuthzHeader(request, "testClient", "changeme");
        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(HttpStatus.SC_OK));

        String token = response.stringValue("access_token");
        HttpGet tokenRequest = new HttpGet(url("/openam/oauth2/tokeninfo?access_token=%s", token));

        HttpResponse tokenResponse = http.execute(tokenRequest);

        assertThat(tokenResponse.statusCode(), is(HttpStatus.SC_OK));


    }

}
