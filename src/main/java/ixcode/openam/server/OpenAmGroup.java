package ixcode.openam.server;

import ixcode.platform.HttpResponse;

public class OpenAmGroup {

    private final String name;

    public OpenAmGroup(String name) {
        this.name = name;
    }

    static OpenAmGroup parseResponse(HttpResponse response) {
        return new OpenAmGroup(response.stringValue("username"));
    }
}
