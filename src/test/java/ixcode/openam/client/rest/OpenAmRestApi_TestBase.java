package ixcode.openam.client.rest;

import ixcode.platform.HttpTestBase;

public class OpenAmRestApi_TestBase extends HttpTestBase {

    public OpenAmRestApi_TestBase() {
        super("http://loan.example.com:9009/");
    }

}
