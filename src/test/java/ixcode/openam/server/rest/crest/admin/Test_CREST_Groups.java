package ixcode.openam.server.rest.crest.admin;

import ixcode.platform.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * http://openam.27691.n7.nabble.com/Making-REST-calls-from-a-non-super-user-td139.html
 * https://forgerock.org/topic/restricting-the-crud-permissions-to-the-administrators/
 * https://forgerock.org/topic/issue-with-openam-rest-calls/
 * http://openam.27691.n7.nabble.com/Making-REST-calls-from-a-non-super-user-td139.html
 * https://forgerock.org/topic/restricting-the-crud-permissions-to-the-administrators/
 * https://forgerock.org/topic/issue-with-openam-rest-calls/
 *
 * All the group information can only be accessed by an admin
 *
 */
public class Test_CREST_Groups extends OpenAmRestApi_TestBase_AdminAuthenticated {


    /**
     * Need to have a group called 'workers' created - there is an api for this but it would be quite complex and difficult
     * to make it smooth. So you need to create it yourself.
     *
     * @throws Exception
     */
    @Test
    public void get_group_info() throws Exception {

        HttpGet request = new HttpGet(url("/openam/json/groups/workers?_prettyPrint=true"));
        request.addHeader("iPlanetDirectoryPro", adminSession.tokenId());
        request.addHeader("Content-Type", "application/json");

        HttpResponse response = http.execute(request);

        assertThat(response.statusCode(), is(200));

    }
}
