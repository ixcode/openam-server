package ixcode.openam.server.rest.crest.admin;

import ixcode.openam.server.OpenAmClient;
import ixcode.openam.server.OpenAmSession;
import ixcode.openam.server.rest.OpenAmRestApi_TestBase_UserAuthenticated;
import org.junit.After;
import org.junit.Before;

/**
 * http://openam.forgerock.org/openam-documentation/openam-doc-source/doc/webhelp/admin-guide/amadmin-changes.html
 *
 * Not that some code is hardcoded to amadmin
 */
public class OpenAmRestApi_TestBase_AdminAuthenticated extends OpenAmRestApi_TestBase_UserAuthenticated {

    protected OpenAmSession adminSession;

    @Before
    public void authenticate_admin() {
        adminSession = new OpenAmClient(http).authenticate("amadmin", "openamdemonstration");
    }

    @After
    public void logout_admin() {
        adminSession.logout();
    }

}
