package com.macaront.framework.web.servlet.ioc;

import com.macaront.framework.web.annotation.Component;
import com.macaront.framework.web.annotation.Controller;
import com.macaront.framework.web.annotation.Repository;
import com.macaront.framework.web.annotation.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IocContainer {

    private static final Logger logger = LoggerFactory.getLogger(IocContainer.class);

    private static final Map<String, Object> beans = new HashMap<>();

    public static Object getBean(String key) {
        return beans.get(key);
    }

    private static final Set<Class<? extends Annotation>> annotations = new HashSet<>();

    private static final Set<Class<?>> annotatedClasses = new HashSet<>();

    private static final Set<Class<?>> controllerClasses = new HashSet<>();

    public static Set<Class<?>> getControllerClasses() {
        return controllerClasses;
    }

    public static void create(String rootPackage) {

        setIocTargetAnnotation();
        setAnnotatedClasses(rootPackage);
        for (Class<?> annotatedClass: annotatedClasses) {
            iocContainerProcessForBeans(annotatedClass);
            if (annotatedClass.isAnnotationPresent(Controller.class)) {
                controllerClasses.add(annotatedClass);
            }
        }
    }

    /**
     * IOC 대상으로 등록할 Annotation 을 annotations 에 추가함.
     */
    private static void setIocTargetAnnotation() {
        annotations.add(Controller.class);
        annotations.add(Service.class);
        annotations.add(Repository.class);
        annotations.add(Component.class);
    }

    /**
     * Java Reflections 를 통해 annotations 에 등록된 Annotation 이 달린 클래스를 찾아 annotatedClasses 에 추가함.
     */
    private static void setAnnotatedClasses(String rootPackage) {
        Reflections reflections = new Reflections(rootPackage);
        for (Class<? extends Annotation> annotation: annotations) {
            Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(annotation);
            annotatedClasses.addAll(classSet);
        }
    }

    /**
     * clazz -> IOC 대상 클래스
     * 생성자 주입 방식으로 Instance 생성
     */
    private static Object iocContainerProcessForBeans(Class<?> clazz) {
        try {
            if (beans.containsKey(clazz.getName())) {
                return beans.get(clazz.getName());
            }

            Constructor<?>[] constructors = clazz.getDeclaredConstructors(); // 해당 클래스의 생성자들
            for (Constructor<?> constructor: constructors) {
                constructor.setAccessible(true); // 생성자 접근 허용
                Class<?>[] args = constructor.getParameterTypes(); // 생성자가 요구하는 인자 클래스들
                Object[] argsInstance = new Object[args.length]; // 생성자가 요구하는 instances
                int i = 0;
                for (Class<?> arg: args) {
                    argsInstance[i++] = iocContainerProcessForBeans(arg); // Recursive 를 통해 IoC instance 얻기
                }
                beans.putIfAbsent(clazz.getName(), constructor.newInstance(argsInstance)); // instance 생성
            }
        } catch (Exception e) {
            logger.error("iocContainerProcess Error: {}", e.getMessage());
        }
        return beans.get(clazz.getName());
    }

}
