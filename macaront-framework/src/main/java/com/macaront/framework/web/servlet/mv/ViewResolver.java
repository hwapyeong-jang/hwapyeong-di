package com.macaront.framework.web.servlet.mv;

public class ViewResolver {
    public static final String prefix = "macaront-app/src/main/resources/static/";

    public static String getView(String viewName){
        return prefix + viewName;
    }
}
