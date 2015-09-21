package ixcode.openam.server.rest;

import ixcode.openam.server.OpenAmClient;
import ixcode.openam.server.OpenAmSession;
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
