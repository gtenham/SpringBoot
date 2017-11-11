package nl.gertontenham.spring.config;

import nl.gertontenham.spring.resource.PingResource;
import nl.gertontenham.spring.resource.filter.EtagEvaluateRequestFilter;
import nl.gertontenham.spring.resource.filter.EtagHeaderResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Filters and interceptors
        register(EtagEvaluateRequestFilter.class);
        register(EtagHeaderResponseFilter.class);

        // Resources
        register(PingResource.class);

        // Forward non-jaxrs requests to Spring Boot servlet
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }
}
