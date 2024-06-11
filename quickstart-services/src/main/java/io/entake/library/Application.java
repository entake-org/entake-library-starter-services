package io.entake.demo;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		R2dbcAutoConfiguration.class
})
@Configuration
@ComponentScan(basePackages = { Application.PARTICLE_PACKAGES, Application.ENTAKE_PACKAGES })
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {

	public static final String NAME = "Entake Demo Services";
    public static final String PARTICLE_PACKAGES = "io.sdsolutions";
    public static final String ENTAKE_PACKAGES = "io.entake";

	private static final Class<Application> APPLICATION_CLASS = Application.class;

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(APPLICATION_CLASS).bannerMode(Mode.OFF).run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(APPLICATION_CLASS).bannerMode(Mode.OFF);
	}

}
