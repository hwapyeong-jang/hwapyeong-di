package com.macaront.framework.web.servlet.mv;

import java.util.LinkedHashMap;

public class ModelMap extends LinkedHashMap<String, Object> {

    public void addAttribute(String attributeName, Object attributeValue) {
        put(attributeName, attributeValue);
    }

}
