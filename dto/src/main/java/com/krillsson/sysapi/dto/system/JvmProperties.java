package com.krillsson.sysapi.dto.system;

import java.util.Map;


public class JvmProperties {


    private Map<String, String> properties;

    /**
     * No args constructor for use in serialization
     */
    public JvmProperties() {
    }

    /**
     * @param properties
     */
    public JvmProperties(Map<String, String> properties) {
        super();
        this.properties = properties;
    }


    public Map<String, String> getProperties() {
        return properties;
    }


    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

}
