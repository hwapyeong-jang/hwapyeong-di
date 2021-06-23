package com.macaront.framework.web.servlet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macaront.framework.web.annotation.PathVariable;
import com.macaront.framework.web.annotation.RequestBody;
import com.macaront.framework.web.servlet.ioc.IocContainer;
import com.macaront.framework.web.util.MacarontPathMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HandlerAdapter.class);

    private static final String ID_PATH_VARIABLE = "id";

    private static final String ID_PATH_VARIABLE_PATTERN = "/*/{id}";

    private static final MacarontPathMatcher pathMatcher = new MacarontPathMatcher();

    public static Object invoke(HttpServletRequest req) {
        StringBuilder methodKey = new StringBuilder(req.getMethod());
        List<Object> pathVariableList = new ArrayList<>();

        makeMethodKey(req, methodKey, pathVariableList);

        Method method = HandlerMapping.getControllerMethod(methodKey.toString());
        if (method != null) {
            Class<?> methodClass = method.getDeclaringClass();
            Object methodInstance = IocContainer.getBean(methodClass.getName());
            Annotation[][] annotations = method.getParameterAnnotations();
            AnnotatedType[] annotatedTypes = method.getAnnotatedParameterTypes();

            Object[] parameters = getParameters(annotations, annotatedTypes, pathVariableList, method.getParameterTypes(), req);

            try {
                return method.invoke(methodInstance, parameters);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("HandlerAdapter Invoke Error: {}", e.getMessage());
            }
        }
        return null;
    }

    private static void makeMethodKey(HttpServletRequest req, StringBuilder methodKey, List<Object> pathVariableList) {
        boolean hasPathVariable = pathMatcher.match(ID_PATH_VARIABLE_PATTERN, req.getRequestURI());
        if (hasPathVariable) {
            Map<String, String> map =
                    pathMatcher.extractUriTemplateVariables(ID_PATH_VARIABLE_PATTERN, req.getRequestURI());
            String mappingUrl =
                    req.getRequestURI().replaceAll(map.get(ID_PATH_VARIABLE), "{"+ID_PATH_VARIABLE+"}");
            methodKey.append(mappingUrl);
            pathVariableList.add(map.get(ID_PATH_VARIABLE));
        } else {
            methodKey.append(req.getRequestURI());
        }
    }

    private static Object[] getParameters(
            Annotation[][] annotations,
            AnnotatedType[] annotatedTypes,
            List<Object> pathVariableList,
            Class<?>[] parameterTypes,
            HttpServletRequest req
    ) {
        Object[] parameters = new Object[annotatedTypes.length];
        int i = 0;
        for (AnnotatedType annotatedType: annotatedTypes) {
            Type type = annotatedType.getType();
            if (annotations[i][0].annotationType() == PathVariable.class) {
                if (type == Long.class) {
                    parameters[i] = Long.parseLong(pathVariableList.get(i).toString());
                } else if (type == String.class) {
                    parameters[i] = pathVariableList.get(i).toString();
                }
            } else if (annotations[i][0].annotationType() == RequestBody.class) {
                try {
                    String jsonStr = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object requestBody = objectMapper.readValue(jsonStr, parameterTypes[i]);
                    parameters[i] = requestBody;
                } catch (IOException e) {
                    logger.error("HandlerAdapter ObjectMapper Error: {}", e.getMessage());
                }
            }

            i++;
        }
        return parameters;
    }
}
