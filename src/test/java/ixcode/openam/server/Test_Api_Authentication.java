package ixcode.openam.server;

import ixcode.platform.HttpResponse;
import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_Api_Authentication extends HttpTestBase {


    public Test_Api_Authentication() {
        super("http://loan.example.com:9009");
    }

    /**
     * <pre>
     *     curl \
     *     --request POST \
     *     --header "X-OpenAM-Username: demo" \
     *     --header "X-OpenAM-Password: changeit" \
     *     --header "Content-Type: application/json" \
     *     --data "{}" \
     *     https://loans.example.com:9009/openam/json/authenticate
     * </pre>
     *
     * @throws Exception
     */
    @Test
    public void can_authenticate_valid_user() throws Exception {

        HttpPost request = new HttpPost(url("/openam/json/authenticate"));

        request.addHeader("X-OpenAM-Username", "demo");
        request.addHeader("X-OpenAM-Password", "changeit");

        StringEntity requestData = new StringEntity("{}");
        requestData.setContentType("application/json");
        request.setEntity(requestData);

        HttpResponse response = http.execute(request);

        assertThat(response.stringValue("tokenId"), is(notNullValue()));
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void can_authenticate_valid_user_with_legacy_api() throws Exception {

    }


    @Test
    public void fails_to_authenticate_dodgy_creds() throws Exception {

        HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/authenticate");


        request.addHeader("X-OpenAM-Username", "demo");
        request.addHeader("X-OpenAM-Password", "foo");

        StringEntity requestData = new StringEntity("{}");
        requestData.setContentType("application/json");
        request.setEntity(requestData);

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(equalTo(401)));
    }


}
