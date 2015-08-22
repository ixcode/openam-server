package ixcode.openam.server;

import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *       curl \
 *       --request POST \
 *       --header "iplanetDirectoryPro: AQIC5wM2...U3MTE4NA..*" \
 *       --header "Content-Type: application/json" \
 *      "http://loan.example.com:9009/openam/json/sessions/?_action=logout"
 */
public class Test_Logout extends HttpTestBase {

    private OpenAmTokenId tokenId;

    @Before
    public void authenticate() throws Exception {
        tokenId = new OpenAmClient(http).authenticate("worker", "worker");
    }


    @Test
    public void can_logout() throws Exception {
        HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/sessions/?_action=logout");
        request.addHeader("iplanetDirectoryPro", tokenId.toString());
        request.addHeader("Content-Type", "application/json");

        Map responseData = http.execute_POST(request);

        //assertThat(responseData.get("tokenId"), is(notNullValue()));
    }

    @Test
    public void can_logout_with_legacy_api() throws Exception {
        HttpPost request = new HttpPost("http://loan.example.com:9009/openam/identity/logout?subjectid=" + tokenId.toString());

        Map responseData = http.execute_POST(request);
    }
}
