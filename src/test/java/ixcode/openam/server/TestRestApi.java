package ixcode.openam.server;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestRestApi {

    private CloseableHttpClient http;

    @Before
    public void init_http() {
        http = HttpClientBuilder.create()
                .disableContentCompression()
                .disableConnectionState()
                .setKeepAliveStrategy(null)
                .build();
    }

    @After
    public void close_http() throws Exception {
        http.close();
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

        HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/authenticate");

        request.addHeader("X-OpenAM-Username", "adminuser");
        request.addHeader("X-OpenAM-Password", "adminuser");

        StringEntity requestData = new StringEntity("{}");
        requestData.setContentType("application/json");
        request.setEntity(requestData);

        Map responseData = execute_POST(request);

        assertThat((String)responseData.get("tokenId"), is(notNullValue()));
    }


    @Test
    public void fails_to_authenticate_dodgy_creds() throws Exception {

        HttpPost request = new HttpPost("http://loan.example.com:9009/openam/json/authenticate");


        request.addHeader("X-OpenAM-Username", "adminuser");
        request.addHeader("X-OpenAM-Password", "foo");

        StringEntity requestData = new StringEntity("{}");
        requestData.setContentType("application/json");
        request.setEntity(requestData);

        Map responseData = execute_POST(request);

        assertThat((Integer)responseData.get("code"), is(equalTo(401)));
    }

    private Map execute_POST(HttpPost POST) throws IOException {
        System.out.println(POST);

        List<Header> headers = Arrays.asList(POST.getAllHeaders());
        for (Header h : headers) {
            System.out.println(h);
        }
        System.out.println("");

        CloseableHttpResponse response = http.execute(POST);

        try {
            System.out.println(response.getStatusLine());
            headers = Arrays.asList(response.getAllHeaders());
            for (Header h : headers) {
                System.out.println(h);
            }

            HttpEntity entity = response.getEntity();


            if (entity != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
                StringBuffer responseBody = new StringBuffer();
                String inputLine;
                try {
                    while ((inputLine = in.readLine()) != null) {
                        responseBody.append(inputLine);
                    }
                } finally {
                    in.close();
                }
                System.out.println("\n" + responseBody + "\n");
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(responseBody.toString(), Map.class);
            }
        } finally {
            response.close();
        }
        return null;
    }

}
