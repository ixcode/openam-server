package ixcode.platform;

import org.junit.After;
import org.junit.Before;

/**
 * Created by jmdb on 21/08/2015.
 */
public abstract class HttpTestBase {

    protected Http http = new Http();

    @Before
    public void init_http() {
        http.init();
    }

    @After
    public void close_http() throws Exception {
        http.destroy();
    }
}
