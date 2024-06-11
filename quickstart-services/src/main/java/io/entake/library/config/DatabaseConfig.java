package io.entake.library.config;

import io.sdsolutions.particle.database.config.JooqDatabaseConfiguration;
import org.jooq.SQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig extends JooqDatabaseConfiguration {

	@Override
	public SQLDialect getSQLDialect() {
		return SQLDialect.H2;
	}

}
