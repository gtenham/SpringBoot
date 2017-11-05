package nl.gertontenham.spring.resource.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleEtagServerCache implements EtagServerCache {

    private static final Logger log = LoggerFactory.getLogger(SimpleEtagServerCache.class);

    private final Map<String, String> cache = new ConcurrentHashMap<String, String>();

    public void set(String uri, String mediaType) {
        final String newUUID = UUID.randomUUID().toString();
        try {
            final String hashedKey = generateKey(uri, mediaType);
            cache.put(hashedKey, newUUID);
        } catch (NoSuchAlgorithmException e) {
            log.error("Generating MD5 hash exception", e);
        }
    }

    public String get(String uri, String mediaType) {
        String entry = null;
        try {
            final String hashedKey = generateKey(uri, mediaType);
            entry = cache.get(hashedKey);
            if (entry == null) {
                set(uri, mediaType);
                entry = cache.get(hashedKey);
            }

        } catch (NoSuchAlgorithmException e) {
            log.error("Generating MD5 hash exception", e);
        }

        return entry;
    }

    private String generateKey(String uri, String mediaType) throws NoSuchAlgorithmException {
        final String key = uri + "#" + mediaType;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(key.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }
}
