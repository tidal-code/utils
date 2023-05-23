package com.tidal.utils.urlbuilders;

/**
 * Gets an enum value of URL schema/protocol
 */
public enum Protocol {
    SECURE("https://"), NORMAL("http://");

    private final String value;

    Protocol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
