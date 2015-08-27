package ixcode.platform;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * Created by jmdb on 21/08/2015.
 */
public class Http {
    private CloseableHttpClient http;

    public Http() {
    }


    public void init() {
        http = HttpClientBuilder.create()
                .disableContentCompression()
                .disableConnectionState()
                .setKeepAliveStrategy(null)
                .build();

    }

    public HttpResponse execute(HttpUriRequest request) throws IOException {
        System.out.println(request);

        List<Header> headers = Arrays.asList(request.getAllHeaders());
        for (Header h : headers) {
            System.out.println(h);
        }
        System.out.println("");

        CloseableHttpResponse response = http.execute(request);

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
                if (responseBody.length() == 0) {
                    return new HttpResponse(response.getStatusLine(), emptyMap());
                }
                ObjectMapper mapper = new ObjectMapper();
                Map responseMap =  mapper.readValue(responseBody.toString(), Map.class);

                return new HttpResponse(response.getStatusLine(), responseMap);
            }
        } finally {
            response.close();
        }
        return null;
    }

    public void destroy() throws Exception {
        http.close();
    }


}
