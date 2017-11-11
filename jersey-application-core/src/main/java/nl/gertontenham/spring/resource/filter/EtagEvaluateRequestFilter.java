package nl.gertontenham.spring.resource.filter;

import nl.gertontenham.spring.resource.cache.EtagCachable;
import nl.gertontenham.spring.resource.cache.EtagServerCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@EtagCachable
public class EtagEvaluateRequestFilter implements ContainerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(EtagEvaluateRequestFilter.class);

    private final EtagServerCache etagServerCache;

    @Inject
    public EtagEvaluateRequestFilter(EtagServerCache etagServerCache) {
        this.etagServerCache = etagServerCache;
    }

    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Determine preferred mediatype from Accept header
        final String mediaType = requestContext.getAcceptableMediaTypes().get(0).toString(); //
        if (StringUtils.equalsIgnoreCase(requestContext.getMethod(),"get")) {
            try {
                // get etag string from cache for resource path and mediatype based key
                String serviceTag = etagServerCache.get(requestContext.getUriInfo().getPath(), mediaType);
                EntityTag etag = new EntityTag(serviceTag);
                Response.ResponseBuilder builder = requestContext.getRequest().evaluatePreconditions(etag);
                if (builder != null) {
                    log.info("Resource {} not modified!", requestContext.getUriInfo().getPath());
                    requestContext.abortWith(builder.build());
                }
            } catch (Exception e) {
                log.error("ETag Request filter exception", e);
            }
        }

    }
}
