package io.entake.library.config;

import io.entake.particle.core.config.MasterApplicationConfig;
import io.entake.particle.core.dozer.MapperConverters;
import org.h2.tools.Server;
import org.modelmapper.Converter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
public class ApplicationConfig extends MasterApplicationConfig {

    @Override
    protected List<Converter<?, ?>> getModelMapperConverters() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(MapperConverters.LOCALDATETIME_DATE);
        converters.add(MapperConverters.DATE_LOCALDATETIME);
        converters.add(MapperConverters.BOOLEAN_BYTE);
        converters.add(MapperConverters.TRIM_STRING);
        return converters;
    }

    @ConditionalOnProperty(name="spring.h2.console.enabled", havingValue="true")
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server(Environment environment) throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", environment.getRequiredProperty("h2.port"));
    }

}
