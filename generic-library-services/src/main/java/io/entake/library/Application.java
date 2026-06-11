package io.entake.library;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { "io.entake" })
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {

	private static final Class<Application> APPLICATION_CLASS = Application.class;

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(APPLICATION_CLASS).bannerMode(Mode.OFF).run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(APPLICATION_CLASS).bannerMode(Mode.OFF);
	}

}
