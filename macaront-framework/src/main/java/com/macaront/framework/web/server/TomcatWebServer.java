package com.macaront.framework.web.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class TomcatWebServer {
    private static final Log logger = LogFactory.getLog(TomcatWebServer.class);

    private final Tomcat tomcat;

    public TomcatWebServer(Tomcat tomcat) {
        this.tomcat = tomcat;
        initialize();
    }

    private void initialize() {
        try {
            this.tomcat.start();
            Thread awaitThread = new Thread("container") {
                @Override
                public void run() {
                    TomcatWebServer.this.tomcat.getServer().await();
                }
            };
            awaitThread.setContextClassLoader(getClass().getClassLoader());
            awaitThread.setDaemon(false);
            awaitThread.start();
        } catch (Exception ex) {
            throw new WebServerException("Unable to start embedded Tomcat", ex);
        }
    }

    public void start() {
        logger.info("Tomcat started on port(s): " + tomcat.getConnector().getPort());
    }

    public void stop()  {
        try {
            logger.info("Tomcat stopped on port(s): " + tomcat.getConnector().getPort());
            this.tomcat.stop();
            this.tomcat.destroy();
        } catch (LifecycleException ex) {
            // swallow and continue
        }
    }
}
