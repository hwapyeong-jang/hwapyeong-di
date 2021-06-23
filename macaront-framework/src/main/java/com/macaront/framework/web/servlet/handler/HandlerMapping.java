package com.macaront.framework.web.servlet.handler;

import com.macaront.framework.web.annotation.RequestMapping;
import com.macaront.framework.web.annotation.RequestMethod;
import com.macaront.framework.web.servlet.ioc.IocContainer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HandlerMapping {

    private static final Map<String, Method> controllerMethods = new HashMap<>();

    public static Method getControllerMethod(String key) {
        return controllerMethods.get(key);
    }

    public static void create() {
        handlerMappingProcess();
    }

    private static void handlerMappingProcess() {
        for (Class<?> clazz: IocContainer.getControllerClasses()) {
            Optional<RequestMapping> reqMapping = Optional.ofNullable(clazz.getAnnotation(RequestMapping.class));
            addMethods(clazz.getDeclaredMethods(), reqMapping.map(RequestMapping::value).orElse(""));
        }
    }

    private static void addMethods(Method[] methods, String preUrl) {
        for (Method method: methods) {
            Optional<RequestMapping> reqMapping = Optional.ofNullable(method.getAnnotation(RequestMapping.class));
            RequestMethod[] httpMethods = reqMapping.map(RequestMapping::method)
                    .orElse(new RequestMethod[]{RequestMethod.GET});

            for (RequestMethod httpMethod: httpMethods) {
                String reqUrl = httpMethod.name() + preUrl + reqMapping.map(RequestMapping::value).orElse("");
                controllerMethods.putIfAbsent(reqUrl, method);
            }
        }
    }

}
