package ixcode.openam.client.rest;

import ixcode.openam.client.OpenAmClient;
import ixcode.openam.client.OpenAmSession;
import org.junit.After;
import org.junit.Before;

public class OpenAmRestApi_TestBase_UserAuthenticated extends OpenAmRestApi_TestBase {

    protected OpenAmSession userSession;


    @Before
    public void authenticate_user() {
        userSession = new OpenAmClient(http).authenticate("demo", "changeit");
    }

    @After
    public void logout_user() {
        userSession.logout();
    }

}
