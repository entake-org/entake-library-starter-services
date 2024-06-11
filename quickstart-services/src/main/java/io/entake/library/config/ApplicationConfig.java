package io.entake.library.config;

import io.sdsolutions.particle.core.config.MasterApplicationConfig;
import io.sdsolutions.particle.core.dozer.MapperConverters;
import org.modelmapper.Converter;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
        converters.add(MapperConverters.LOCALDATETIME_OFFSETDATETIME);
        converters.add(MapperConverters.OFFSETDATETIME_LOCALDATETIME);
        return converters;
    }

}
