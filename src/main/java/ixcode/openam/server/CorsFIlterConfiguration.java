package ixcode.openam.server;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;

import static javax.servlet.DispatcherType.REQUEST;

/**
 * TODO - parameterise in config wether to allow cross origin filter
 * TODO - parameterise in config ALLOWED_ORIGINS
 * TODO - Write tests for the filter?
 */
public class CorsFilterConfiguration extends AbstractConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OpenAmServer.class);

    @Override
    public void configure(WebAppContext context) throws Exception {

        Class corsFilterClass = context.getClassLoader().loadClass("org.forgerock.openam.cors.CORSFilter");


        // TODO work out how to limit to just /oauth2 and /json
        FilterHolder filterHolder = context.addFilter(corsFilterClass, "/*", EnumSet.of(REQUEST));

        filterHolder.setInitParameter("methods", "POST,GET,PUT,DELETE,PATCH,OPTIONS");
        filterHolder.setInitParameter("origins", "*");
        filterHolder.setInitParameter("allowCredentials", "true");
        filterHolder.setInitParameter("headers", "X-OpenAM-Username, X-OpenAM-Password,Content-Type,Accept,Origin,Access-Control-Request-Headers,cache-control, Authorization");

        log.info("Configured CORS filter: " + corsFilterClass.getName());
    }
}
