package com.macaront.framework.web.util;

import org.springframework.util.AntPathMatcher;

import java.util.Map;

public class MacarontPathMatcher {
    private final AntPathMatcher antPathMatcher;

    public MacarontPathMatcher() {
        antPathMatcher = new AntPathMatcher();
    }

    public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
        return antPathMatcher.extractUriTemplateVariables(pattern, path);
    }

    public boolean match(String pattern, String path) {
        return antPathMatcher.match(pattern, path);
    }
}
