package ixcode.openam.client;

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


    private final String rootUrl = "http://loan.example.com:9009";
    private final Http http;

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

    boolean logout(OpenAmSession openAmSession) {
        try {
            HttpPost request = postRequest(openAmSession.tokenId(), url("/openam/json/sessions/?_action=logout"));

            HttpResponse response = http.execute(request);

            return response.is(SC_OK);

        } catch (Throwable t) {
            throw new RuntimeException(format("Exception occured whilst logging out token [%s] (See stack trace)", openAmSession.tokenId()), t);
        }
    }

    private String url(String path, Object... params) {
        return format("%s/%s", rootUrl, format(path, params));
    }

    private HttpPost postRequest(String tokenId, String url) {
        HttpPost request = new HttpPost(url);
        request.addHeader("iPlanetDirectoryPro".toLowerCase(), tokenId);
        request.addHeader("Content-Type", "application/json");
        return request;
    }

}
