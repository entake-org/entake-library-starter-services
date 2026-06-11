package io.entake.library.config;

import io.entake.particle.security.config.CorsConfigurationProperties;
import io.entake.particle.security.config.impl.PassThroughSecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig extends PassThroughSecurityConfiguration {

    public SecurityConfig(
            Environment environment,
            CorsConfigurationProperties corsConfigurationProperties
    ) {
        super(environment, corsConfigurationProperties);
    }

}

