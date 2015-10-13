package ixcode.platform;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetailsResponseHandler implements ResponseHandler {
    public static Map<String, String[]> parseUserDetails(String userDetails) {
        String[] items = userDetails.split("userdetails.");
        Map<String, String[]> data = new HashMap<String, String[]>();
        AttributeBuilder attributeBuilder = null;
        for (String item : items) {
            if (item.length() == 0) {
                continue;
            }
            String[] tuple = item.split("=");
            if ("attribute.name".equals(tuple[0])) {
                if (attributeBuilder != null) {
                    attributeBuilder.addTo(data);
                }
                attributeBuilder = new AttributeBuilder();
                attributeBuilder.name(tuple[1]);
            } else if ("attribute.value".equals(tuple[0])) {
                attributeBuilder.value(tuple[1]);
            } else {
                data.put(tuple[0], new String[]{tuple[1]});
            }
        }

       return data;
    }

    static String debugArray(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (String item : arr) {
            sb.append(item).append(", ");
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 2);
    }

    @Override
    public Map processResponseBody(CloseableHttpResponse response, StringBuffer responseBody) throws IOException {
        System.out.println("response: " + responseBody);
        return new HashMap();
    }

    public static class AttributeBuilder {

        private static String[] EMPTY_ARR = new String[0];
        private List<String> values = new ArrayList<String>();
        private String name;

        public void name(String name) {
            this.name = name;
        }

        public void addTo(Map<String, String[]> data) {
            data.put(name, values.toArray(EMPTY_ARR));
        }

        public void value(String value) {
            this.values.add(value);
        }
    }
}
