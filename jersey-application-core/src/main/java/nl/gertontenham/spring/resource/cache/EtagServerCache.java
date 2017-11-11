package nl.gertontenham.spring.resource.cache;

/**
 *
 */
public interface EtagServerCache {

    /**
     *
     * @param uri
     * @param mediaType
     */
    void set(String uri, String mediaType);

    /**
     *
     * @param uri
     * @param mediaType
     * @return
     */
    String get(String uri, String mediaType);

}
