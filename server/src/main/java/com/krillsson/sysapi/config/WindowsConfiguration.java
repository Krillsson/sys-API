package com.krillsson.sysapi.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class WindowsConfiguration {

    @NotNull
    @JsonProperty
    private boolean enableOhmJniWrapper;

    public WindowsConfiguration(boolean enableOhmJniWrapper) {
        this.enableOhmJniWrapper = enableOhmJniWrapper;
    }

    public WindowsConfiguration() {
    }

    public boolean enableOhmJniWrapper() {
        return enableOhmJniWrapper;
    }

}
