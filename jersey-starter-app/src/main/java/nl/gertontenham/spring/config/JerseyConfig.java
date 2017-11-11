package nl.gertontenham.spring.config;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import nl.gertontenham.spring.resource.PingResource;
import nl.gertontenham.spring.resource.filter.EtagEvaluateRequestFilter;
import nl.gertontenham.spring.resource.filter.EtagHeaderResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JerseyConfig extends ResourceConfig {

    @Value("${spring.jersey.application-path:/}")
    private String apiPath;

    @Value("${info.app.version:1}")
    private String apiVersion;

    @Value("${info.app.artifactId}")
    private String apiId;

    @Value("${info.app.name}")
    private String apiTitle;

    public JerseyConfig() {
        // Filters and interceptors
        register(EtagEvaluateRequestFilter.class);
        register(EtagHeaderResponseFilter.class);

        // Resources
        register(PingResource.class);

        // Forward non-jaxrs requests to Spring Boot servlet
        property(ServletProperties.FILTER_FORWARD_ON_404, true);
    }

    @PostConstruct
    public void init() {
        // Register components where DI is needed
        configureSwagger();
    }

    private void configureSwagger() {
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setConfigId(apiId);
        beanConfig.setVersion(apiVersion);
        beanConfig.setTitle(apiTitle);
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setBasePath(apiPath);
        beanConfig.setResourcePackage("nl.gertontenham.spring.resource");
        beanConfig.setScan(true);

    }
}
