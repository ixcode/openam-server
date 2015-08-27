package ixcode.platform;

import org.apache.http.StatusLine;

import java.util.Map;

public class HttpResponse {

    private final StatusLine responseStatusLine;
    private final Map responseData;

    public HttpResponse(StatusLine responseStatusLine, Map responseData) {
        this.responseData = responseData;
        this.responseStatusLine = responseStatusLine;
    }

    public <T> T responseValue(String key) {
        return (T)responseData.get(key);
    }

    public int statusCode() {
        return responseStatusLine.getStatusCode();
    }

    public String stringValue(String key) {
        return responseValue(key);
    }
}
