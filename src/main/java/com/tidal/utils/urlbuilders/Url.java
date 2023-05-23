package com.tidal.utils.urlbuilders;



import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Url {

    private Protocol protocol;
    private String host;
    private int port;
    private String basePath;
    private String path;
    private Map<String, String> queryParams;
    private String url;


    public String value() {
        return url;
    }

    /**
     * Builds the url with the values given after performing null check
     * Accidental double slashes will be replaced with a single slash.
     * If BasePath or Path doesn't start with a '/' it will be added
     *
     * @return the parsed url as a string
     */
    private Url buildUrl() {
        String params = parseMap();

        url = host.replace("http://", "").replace("https://", "") +
                (port >= 0 ? ":" + port : "") +
                (basePath != null ? (!basePath.startsWith("/") ? "/" + basePath : basePath) : "") +
                (path != null ? (!path.startsWith("/") ? "/" + path : path) : "") +
                (!params.isEmpty() ? params : "");

        url = url.replace("//", "/");
        url = protocol.getValue() + url;
        return this;
    }

    /**
     * Parses the query parameter values stored in the map
     *
     * @return the parsed map as a single combined string
     */
    private String parseMap() {
        StringBuilder queryParamPart;
        Set<String> keys = queryParams.keySet();

        queryParamPart = keys.size() > 0 ? new StringBuilder("?") : new StringBuilder();

        int counter = 0;
        for (String key : keys) {
            if (counter == 2) {
                break;
            }
            queryParamPart.append(key).append("=").append(queryParams.get(key));

            if (counter == 0 && keys.size() > 1) {
                queryParamPart.append("&");
            }
            counter++;
        }

        return queryParamPart.toString();
    }

    /**
     * The builder class receives the url building blocks and constructs the final string
     */
    public static class Builder {
        private final LinkedHashMap<String, String> queryParams;
        private String basePath;
        private String path;

        // Sets the default value to a negative integer so as the int default value [which is 0] shouldn't add the port 0
        private int port = -1;

        public Builder() {
            queryParams = new LinkedHashMap<>();
        }

        /**
         * Sets the URL port. Can be ignored if port is set to default
         *
         * @param port port number
         * @return Builder class instance
         */
        public Builder port(int port) {
            this.port = port;
            return this;
        }

        /**
         * Basepath of the URL or the first part of the url path
         *
         * @param basePath base path
         * @return Builder class instance
         */
        public Builder basePath(String basePath) {
            this.basePath = basePath;
            return this;
        }

        /**
         * Path of the URL that comes after the base path
         *
         * @param path url path
         * @return Builder class instance
         */
        public Builder path(String path) {
            this.path = path;
            return this;
        }

        /**
         * Query parameters of the URL. Only a max of two can be added
         *
         * @param key   parameter key
         * @param value parameter value
         * @return Builder class instance
         */
        public Builder queryParams(String key, String value) {
            queryParams.put(key, value);
            return this;
        }

        public Url build(Protocol protocol, String host) {
            Url url = new Url();
            url.protocol = protocol;
            url.host = host;
            url.port = this.port;
            url.basePath = this.basePath;
            url.path = this.path;
            url.queryParams = this.queryParams;
            return url.buildUrl();
        }
    }

}






