package nl.gertontenham.spring.config;

import nl.gertontenham.spring.resource.PingResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(PingResource.class);
    }
}
