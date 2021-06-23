package com.macaront.framework.web;

import java.util.ArrayList;
import java.util.List;

public class StaticLifeCycleEventBus {
    private static List<StaticLifeCycleListener> listeners = new ArrayList<>();

    public static void put(StaticLifeCycleListener staticLifeCycleListener) {
        listeners.add(staticLifeCycleListener);
    }

    public static void send(SimpleLifeCycle simpleLifeCycle) {
        listeners.forEach(listener -> listener.listen(simpleLifeCycle));
    }
}
