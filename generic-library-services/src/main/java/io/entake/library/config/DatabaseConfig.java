package io.entake.library.config;

import io.entake.particle.database.config.JooqDatabaseConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DatabaseConfig extends JooqDatabaseConfiguration {

	@Value(value = "${spring.datasource.url}")
	private String datasourceUrl;

	@Override
	public SQLDialect getSQLDialect() {
		String dbType = getDatabaseType();

		log.info("Database Type: {}", dbType);

		if("postgresql".equalsIgnoreCase(dbType)) {
			return SQLDialect.POSTGRES;
		} else if("mariadb".equalsIgnoreCase(dbType)) {
			return SQLDialect.MARIADB;
		} else if("mysql".equalsIgnoreCase(dbType)) {
			return SQLDialect.MYSQL;
		} else if ("h2".equalsIgnoreCase(dbType)) {
			return SQLDialect.H2;
		}

		log.warn("Unknown database type ({}) falling back to default dialect - system may perform unexpectedly", dbType);
		return SQLDialect.DEFAULT;
	}

	@Bean
	@Override
	public DefaultDSLContext dslContext(DefaultConfiguration jooqConfig) {
		DefaultDSLContext dslContext = super.dslContext(jooqConfig);

		if (!"h2".equalsIgnoreCase(getDatabaseType())) {
			dslContext.settings().withRenderNameCase(RenderNameCase.LOWER);
		}

		return dslContext;
	}

	private String getDatabaseType() {
		String jdbcToken = datasourceUrl.substring(5);
		return jdbcToken.substring(0, jdbcToken.indexOf(":"));
	}

}
