package ixcode.openam.client.rest.crest;

import ixcode.openam.client.rest.OpenAmRestApi_TestBase;
import ixcode.platform.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class Test_CREST_ServerInfo extends OpenAmRestApi_TestBase {

    @Test
    public void gets_server_info() {
        HttpGet request = new HttpGet(url("openam/json/serverinfo/*"));

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));
    }
}
