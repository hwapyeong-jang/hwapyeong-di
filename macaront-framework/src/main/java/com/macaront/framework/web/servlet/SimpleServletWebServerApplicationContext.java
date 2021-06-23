package com.macaront.framework.web.servlet;

import com.macaront.framework.web.SimpleLifeCycle;
import com.macaront.framework.web.StaticLifeCycleEventBus;
import com.macaront.framework.web.server.TomcatWebServer;
import com.macaront.framework.web.server.TomcatWebServerFactory;
import com.macaront.framework.web.server.filter.CharacterEncodingFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * Web Application Context
 */
public class SimpleServletWebServerApplicationContext {

    public static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";
    public final String rootPackage;

    public SimpleServletWebServerApplicationContext(Class<?> primarySource) {
        this.rootPackage = primarySource.getPackage().getName();
    }

    private TomcatWebServer webServer;

    public void refresh() {
        createWebServer();
        registerShutdownHandler();
        start();
        StaticLifeCycleEventBus.send(SimpleLifeCycle.AFTER_START_EVENT);
    }

    private void createWebServer() {
        TomcatWebServerFactory tomcatWebServerFactory = new TomcatWebServerFactory();
        this.webServer = tomcatWebServerFactory.getWebServer(this::selfInitialize);
    }

    /**
     * 이것을 설명해주자면, 클라이언트로부터 어떠한 요청이 오면 Tomcat(톰캣)과 같은 서블릿컨테이너가 요청을 받는데,
     * 이때 제일 앞에서 서버로 들어오는 모든 요청을 처리하는 *프론트 컨트롤러를 정의하였고,
     * 이를 Dispatcher-Servlet이라고 합니다. 그래서 공통처리 작업을 Dispatcher 서블릿이 처리한 후,
     * 적절한 세부 컨트롤러로 작업을 위임해줍니다.
     *
     * 물론 Dispatcher-Servlet이 처리하는 url 패턴을 지정해주어야 하는데
     * 일반적으로는 /*.do와 같은 /로 시작하며 .do로 끝나는 url 패턴에 대해서 처리하라고 지정해줍니다.
     *
     */
    private void selfInitialize(ServletContext servletContext) {
        ServletRegistration.Dynamic servletRegistration = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(rootPackage));
        servletRegistration.setLoadOnStartup(1);
        servletRegistration.addMapping("/");

        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("encodingFilter", new CharacterEncodingFilter());
        filterRegistration.addMappingForUrlPatterns(null, false, "/*");
    }

    private void registerShutdownHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> webServer.stop()));
    }

    private void start() {
        this.webServer.start();
    }
}
