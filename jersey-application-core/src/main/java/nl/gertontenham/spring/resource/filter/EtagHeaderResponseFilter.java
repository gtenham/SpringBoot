package nl.gertontenham.spring.resource.filter;

import nl.gertontenham.spring.resource.cache.EtagCachable;
import nl.gertontenham.spring.resource.cache.EtagServerCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@EtagCachable
public class EtagHeaderResponseFilter implements ContainerResponseFilter {

    private static final Logger log = LoggerFactory.getLogger(EtagHeaderResponseFilter.class);

    private static final String ETAG = "ETag";

    private final EtagServerCache etagServerCache;

    @Inject
    public EtagHeaderResponseFilter(EtagServerCache etagServerCache) {
        this.etagServerCache = etagServerCache;
    }

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        if (responseContext.getStatus() == Response.Status.NOT_MODIFIED.getStatusCode()) {
            return;
        }

        // Determine preferred mediatype from Accept header
        final String mediaType = requestContext.getAcceptableMediaTypes().get(0).toString(); //

        if (StringUtils.equalsIgnoreCase(requestContext.getMethod(),"get")) {
            try {
                // get etag string from cache for resource path and mediatype based key
                String serviceTag = etagServerCache.get(requestContext.getUriInfo().getPath(), mediaType);
                final String etag = StringUtils.wrap(serviceTag, '"');
                log.info("Add ETag resource header {} to {}", etag, requestContext.getUriInfo().getPath());
                responseContext.getHeaders().add(ETAG, etag);
            } catch (Exception e) {
                log.error("ETag Response filter exception", e);
            }
        } else {
            log.info("Succesfull response, increment Etag value to cache");
            etagServerCache.set(requestContext.getUriInfo().getPath(), mediaType);
        }
    }
}
