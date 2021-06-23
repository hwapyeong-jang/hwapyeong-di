package com.macaront.framework.web.server;

import com.macaront.framework.web.servlet.ServletContextInitializer;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class TomcatWebServerFactory {
    public TomcatWebServer getWebServer(ServletContextInitializer servletContextInitializer) {
        Tomcat tomcat = new Tomcat();
        prepareContext(servletContextInitializer, tomcat);
        return new TomcatWebServer(tomcat);
    }

    private void prepareContext(ServletContextInitializer servletContextInitializer, Tomcat tomcat) {
        StandardContext context = new StandardContext();
        File docBase = createTempDir("tomcat-docbase", tomcat.getConnector().getPort());
        context.setDocBase(docBase.getAbsolutePath());
        context.setDisplayName("application");
        context.setName("");
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.getHost().addChild(context);
        context.addServletContainerInitializer(new TomcatStarter(servletContextInitializer), Collections.emptySet());
    }

    /**
     * Return the absolute temp dir for given web server.
     * @param prefix server name
     * @return The temp dir for given server.
     */
    private File createTempDir(String prefix, int port) {
        try {
            File tempDir = File.createTempFile(prefix + ".", "." + port);
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir;
        }
        catch (IOException ex) {
            throw new WebServerException(
                    "Unable to create tempDir. java.io.tmpdir is set to "
                            + System.getProperty("java.io.tmpdir"),
                    ex);
        }
    }
}
