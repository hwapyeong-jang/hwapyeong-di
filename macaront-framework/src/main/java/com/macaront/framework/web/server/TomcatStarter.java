package com.macaront.framework.web.server;

import com.macaront.framework.web.servlet.ServletContextInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class TomcatStarter implements ServletContainerInitializer {
    private final ServletContextInitializer initializers;

    public TomcatStarter(ServletContextInitializer initializers) {
        this.initializers = initializers;
    }

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        initializers.onStartup(ctx);
    }
}
