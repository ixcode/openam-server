package ixcode.openam.server;

import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_Api_Authentication extends HttpTestBase {


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

        HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/authenticate");

        request.addHeader("X-OpenAM-Username", "demo");
        request.addHeader("X-OpenAM-Password", "changeit");

        StringEntity requestData = new StringEntity("{}");
        requestData.setContentType("application/json");
        request.setEntity(requestData);

        Map responseData = http.execute_POST(request);

        assertThat(responseData.get("tokenId"), is(notNullValue()));
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

        Map responseData = http.execute_POST(request);

        assertThat((Integer)responseData.get("code"), is(equalTo(401)));
    }


}
