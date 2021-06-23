package com.macaront.framework.web;

@FunctionalInterface
public interface StaticLifeCycleListener {
    void listen(SimpleLifeCycle lifeCycle);
}
