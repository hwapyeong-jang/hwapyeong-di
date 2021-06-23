package com.macaront.framework;

import com.macaront.framework.web.servlet.SimpleServletWebServerApplicationContext;

public class MacarontFrameworkApplication {

	private final Class<?> primarySource;
	private SimpleServletWebServerApplicationContext context;

	// primarySource = application
	public MacarontFrameworkApplication(Class<?> primarySource) {
		this.primarySource = primarySource;
	}

	private void run(String[] args) {
		this.context = new SimpleServletWebServerApplicationContext(this.primarySource);
		context.refresh();
	}

	public static void run(Class<?> primarySource, String... args) {
		new MacarontFrameworkApplication(primarySource).run(args);
	}

}
