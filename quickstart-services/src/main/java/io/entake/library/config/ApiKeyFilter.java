package io.entake.library.config;

import io.sdsolutions.particle.exceptions.UnauthorizedException;
import io.sdsolutions.particle.security.filter.AbstractSecurityFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyFilter extends AbstractSecurityFilter {

    private static final String API_KEY_HEADER = "api_key";
    private static final String PUBLIC_ENDPOINT_PATH = "/api/public";

    private final String API_KEY;

    public ApiKeyFilter(Environment environment) {
        this.API_KEY = environment.getRequiredProperty("api.key");
    }

    @Override
    public void doFilterImpl(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (!isPublicEndpoint(request)) {
            String apiKey = request.getHeader(API_KEY_HEADER);

            if (!API_KEY.equals(apiKey)) {
                throw new UnauthorizedException();
            }
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.substring(uri.indexOf('/', 1));

        return uri.startsWith(PUBLIC_ENDPOINT_PATH);
    }
}
