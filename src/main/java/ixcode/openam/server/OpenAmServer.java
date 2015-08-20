package ixcode.openam.server;

import ixcode.platform.LogbackConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

import static ch.qos.logback.classic.Level.INFO;
import static ixcode.platform.LogbackConfiguration.STANDARD_OPS_FORMAT;
import static ixcode.platform.LogbackConfiguration.initialiseConsoleLogging;
import static java.lang.String.format;

public class OpenAmServer {

    private static final Logger log = LoggerFactory.getLogger(OpenAmServer.class);

    public static void main(String[] args) {
        new OpenAmServer("openam-server", 9009, args[0]).start();
    }

    private String serverName;
    private final int httpPort;
    private final String openAmWarFilePath;
    private Server server;


    public OpenAmServer(String serverName, int httpPort, String openAmWarFilePath) {
        this.serverName = serverName;
        this.httpPort = httpPort;
        this.openAmWarFilePath = openAmWarFilePath;
    }

    public void start() {
        try {
            initialiseConsoleLogging(INFO, STANDARD_OPS_FORMAT);

            File openAmWar = new File(openAmWarFilePath);
            if (!openAmWar.exists()) {
                throw new FileNotFoundException(openAmWar.getAbsolutePath());
            }

            log.info(format("Starting Server [%s] on port %d", serverName, httpPort));

            this.server = new Server(httpPort);

            server.setHandler(rootHandler());


            server.start();

            log.info((format("Open AM War from: [%s]", openAmWar.getAbsolutePath())));
            log.info(format("Server [%s] started @ http://localhost:%d", serverName, httpPort));

            server.join();
        } catch (Throwable t) {
            throw new RuntimeException(format("Could not start server [%s] (see cause)", serverName), t);
        }
    }

    private Handler rootHandler() {
        HandlerList handlerList = new HandlerList();

        Handler[] handlers = new Handler[]{openAmWebAppHandler()};

        handlerList.setHandlers(handlers);

        return handlerList;
    }

    private Handler openAmWebAppHandler() {
        WebAppContext webappHandler = new WebAppContext();
        webappHandler.setContextPath("/openam");
        webappHandler.setWar(openAmWarFilePath);
        return webappHandler;
    }


}