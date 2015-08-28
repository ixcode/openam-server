package ixcode.openam.server;

import ixcode.platform.Http;
import ixcode.platform.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import static java.lang.String.format;
import static org.apache.http.HttpStatus.SC_OK;

/**
 * See https://backstage.forgerock.com/#!/docs/openam/12.0.0/dev-guide
 */
public class OpenAmClient {


    private Http http;

    public OpenAmClient(Http http) {
        this.http = http;
    }

    public OpenAmSession authenticate(String username, String password) {
        try {
            HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/authenticate");

            request.addHeader("X-OpenAM-Username", username);
            request.addHeader("X-OpenAM-Password", password);

            StringEntity requestData = new StringEntity("{}");
            requestData.setContentType("application/json");
            request.setEntity(requestData);

            HttpResponse response = http.execute(request);

            return new OpenAmSession(this, new OpenAmTokenId(response.stringValue("tokenId")));

        } catch (Throwable t) {
            throw new RuntimeException(format("Could not authenticate user [%s] (See stack trace)", username), t);
        }
    }

    boolean logout(OpenAmTokenId tokenId) {
        try {
            HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/sessions/?_action=logout");
            request.addHeader("iPlanetDirectoryPro", tokenId.toString());
            request.addHeader("Content-Type", "application/json");

            HttpResponse response = http.execute(request);

            return response.is(SC_OK);

        } catch (Throwable t) {
            throw new RuntimeException(format("Exception occured whilst logging out token [%s] (See stack trace)", tokenId), t);
        }
    }
}
