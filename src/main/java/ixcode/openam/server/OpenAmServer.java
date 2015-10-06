package ixcode.openam.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import static org.eclipse.jetty.servlets.CrossOriginFilter.*;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static ch.qos.logback.classic.Level.INFO;
import static ixcode.platform.LogbackConfiguration.STANDARD_OPS_FORMAT;
import static ixcode.platform.LogbackConfiguration.initialiseConsoleLogging;
import static java.lang.String.format;
import static javax.servlet.DispatcherType.REQUEST;

public class OpenAmServer {

    private static final Logger log = LoggerFactory.getLogger(OpenAmServer.class);
    private static FilterHolder CORSFilterClass;

    public static void main(String[] args) {
        String domainName = (args.length == 2) ? args[1] : "localhost";
        new OpenAmServer("openam-server", 9009, args[0], domainName).start();
    }

    private String serverName;
    private final int httpPort;
    private final String openAmWarFilePath;
    private String domainName;
    private Server server;


    public OpenAmServer(String serverName, int httpPort, String openAmWarFilePath, String domainName) {
        this.serverName = serverName;
        this.httpPort = httpPort;
        this.openAmWarFilePath = openAmWarFilePath;
        this.domainName = domainName;
    }

    public void start() {
        try {
            initialiseConsoleLogging(INFO, STANDARD_OPS_FORMAT);

            File openAmWar = new File(openAmWarFilePath);
            if (!openAmWar.exists()) {
                throw new FileNotFoundException(openAmWar.getAbsolutePath());
            }

            File tempDirectory = new File("/var/openam/" + openAmWar.getName());

            log.info(format("Starting Server [%s] on port %d", serverName, httpPort));

            this.server = new Server(httpPort);

            server.setHandler(rootHandler(server, openAmWar, tempDirectory));


            server.start();

            log.info((format("Open AM War from      : [%s]", openAmWar.getAbsolutePath())));
            log.info((format("Open AM War temp file : [%s]", tempDirectory.getAbsolutePath())));
            log.info(format("Server [%s] started @ http://%s:%d/openam", serverName, domainName, httpPort));

            server.join();
        } catch (Throwable t) {
            throw new RuntimeException(format("Could not start server [%s] (see cause)", serverName), t);
        }
    }

    private static Handler rootHandler(Server server, File warFile, File tempDirectory) {
        HandlerList handlerList = new HandlerList();


        Handler[] handlers = new Handler[]{openAmWebAppHandler(server, warFile, tempDirectory)};

        handlerList.setHandlers(handlers);

        return handlerList;
    }

    /**
     * This is now specific to jetty 9.3
     *
     * @See https://eclipse.org/jetty/documentation/current/embedded-examples.html#embedded-webapp-jsp
     * @See https://eclipse.org/jetty/documentation/current/ref-temporary-directories.html
     */
    private static Handler openAmWebAppHandler(Server server, File warFile, File tempDirectory) {


        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/openam");
        webAppContext.setWar(warFile.getAbsolutePath());
        webAppContext.setPersistTempDirectory(true);
        webAppContext.setTempDirectory(tempDirectory);

        addCrossOriginFilter(webAppContext);

        // Jetty 9.3 part
        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);

        classlist.addBefore(
                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration");

        webAppContext.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$");

        return webAppContext;
    }

    /**
     * TODO - parameterise in config wether to allow cross origin filter
     * TODO - parameterise in config ALLOWED_ORIGINS
     * TODO - Write tests for the filter?
     *
     * See https://forgerock.org/2015/02/openam-with-cors-is-that-a-salad-dessert-or-main-course/
     *
     * optional parameters are:
     * expectedHostname
     * exposeHeadersmaxAge
     */
    private static void addCrossOriginFilter(WebAppContext webAppContext) {
        FilterHolder filterHolder = webAppContext.addFilter(getCORSFilterClass(webAppContext), "/json/*", EnumSet.of(REQUEST));

        filterHolder.setInitParameter("methods", "POST,GET,PUT,DELETE,PATCH,OPTIONS");
        filterHolder.setInitParameter("origins", "*");
        filterHolder.setInitParameter("allowCredentials", "true");
        filterHolder.setInitParameter("headers", "X-OpenAM-Username, X-OpenAM-Password,Content-Type,Accept,Origin,Access-Control-Request-Headers,cache-control, Authorization");



        log.info("Setup CORS Filter ok.");

    }

    /**
     * Need to work out how to ensure that we do this after the WAR file has been extracted
     * @param webAppContext
     * @return
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends Filter> getCORSFilterClass(WebAppContext webAppContext) {

        String corsFilterClassName = "org.forgerock.openam.cors.CORSFilter";
        try {
            File libDir = new File(webAppContext.getTempDirectory(), "webapp/WEB-INF/lib");

            String[] jars = libDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });

            List<URL> libJarUrls = new ArrayList<URL>();
            for (String libJarName : jars) {
                libJarUrls.add(new File(libDir,  libJarName).toURI().toURL());
            }

            log.info("LibJars: " + libJarUrls);
            URLClassLoader loader = new URLClassLoader(libJarUrls.toArray(new URL[0]));


            return (Class<? extends Filter>) loader.loadClass(corsFilterClassName);
        } catch (Throwable t) {
            throw new RuntimeException(format("Could not load class [%s]", corsFilterClassName), t);
        }
    }


}