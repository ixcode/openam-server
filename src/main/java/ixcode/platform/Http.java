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

import static java.lang.System.out;
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
        out.println(request);

        List<Header> headers = Arrays.asList(request.getAllHeaders());
        for (Header h : headers) {
            out.println(h);
        }
        out.println("");

        CloseableHttpResponse response = http.execute(request);

        try {
            out.println(response.getStatusLine());
            headers = Arrays.asList(response.getAllHeaders());
            for (Header h : headers) {
                out.println(h);
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

                out.println("\n" + responseBody + "\n");
                if (responseBody.length() == 0 || !isJson(response) || isZeroContentLength(response)) {
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

    private boolean isZeroContentLength(CloseableHttpResponse response) {
        return 0 == Integer.parseInt(response.getFirstHeader("Content-Length").getValue());
    }

    private boolean isJson(CloseableHttpResponse response) {
        return response.getFirstHeader("Content-Type").getValue().contains("application/json");
    }

    public void destroy() throws Exception {
        http.close();
    }


}
