package ixcode.openam.server;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public Map execute_POST(HttpPost POST) throws IOException {
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

    public void destroy() throws Exception {
        http.close();
    }

}
