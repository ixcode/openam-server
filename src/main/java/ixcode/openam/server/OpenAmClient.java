package ixcode.openam.server;

import ixcode.platform.Http;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.util.Map;

import static java.lang.String.format;

/**
 * See https://backstage.forgerock.com/#!/docs/openam/12.0.0/dev-guide
 */
public class OpenAmClient {


    private Http http;

    public OpenAmClient(Http http) {
        this.http = http;
    }

    public OpenAmTokenId authenticate(String username, String password) {
        try {
            HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/authenticate");

            request.addHeader("X-OpenAM-Username", username);
            request.addHeader("X-OpenAM-Password", password);

            StringEntity requestData = new StringEntity("{}");
            requestData.setContentType("application/json");
            request.setEntity(requestData);

            Map responseData = http.execute_POST(request);

            return new OpenAmTokenId((String)responseData.get("tokenId"));

        } catch (Throwable t) {
            throw new RuntimeException(format("Could not authenticate user [%s] (See stack trace)", username), t);
        }
    }
}
