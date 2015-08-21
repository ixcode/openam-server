package ixcode.openam.server;

import ixcode.platform.HttpTestBase;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;

public class Test_Api_Tokens extends HttpTestBase {

    private OpenAmTokenId tokenId;

    @Before
    public void authenticate() throws Exception {
        tokenId = new OpenAmClient(http).authenticate("worker", "worker");
    }

    @Test
    public void can_retrieve_token_information() {

    }
}
