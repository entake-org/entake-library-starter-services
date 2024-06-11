package io.entake.library.config;

import io.sdsolutions.particle.security.config.CorsConfigurationProperties;
import io.sdsolutions.particle.security.config.impl.PassThroughSecurityConfiguration;
import io.sdsolutions.particle.security.services.SecurityService;
import io.sdsolutions.particle.security.services.impl.SecurityServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig extends PassThroughSecurityConfiguration {

    public SecurityConfig(
            UserDetailsService userDetailsService,
            CorsConfigurationProperties corsConfigurationProperties
    ) {
        super(userDetailsService, corsConfigurationProperties);
    }

    @Bean
    public SecurityService securityService() {
        return new SecurityServiceImpl();
    }

}

