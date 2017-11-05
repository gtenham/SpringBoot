package nl.gertontenham.spring.config;

import nl.gertontenham.spring.resource.PingResource;
import nl.gertontenham.spring.resource.filter.EtagEvaluateRequestFilter;
import nl.gertontenham.spring.resource.filter.EtagHeaderResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        // Filters and interceptors
        register(EtagEvaluateRequestFilter.class);
        register(EtagHeaderResponseFilter.class);

        // Resources
        register(PingResource.class);
    }
}
