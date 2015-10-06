package ixcode.openam.client.rest.crest.admin;

import ixcode.openam.client.OpenAmClient;
import ixcode.openam.client.OpenAmSession;
import ixcode.openam.client.rest.OpenAmRestApi_TestBase_UserAuthenticated;
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
