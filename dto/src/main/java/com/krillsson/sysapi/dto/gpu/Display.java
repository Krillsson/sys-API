package com.krillsson.sysapi.dto.gpu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "edid"
})
public class Display {

    @JsonProperty("edid")
    private String edid;

    public Display() {
    }

    public Display(String edid) {
        this.edid = edid;
    }

    @JsonProperty("edid")
    public String getEdid() {
        return edid;
    }

    @JsonProperty("edid")
    public void setEdid(String edid) {
        this.edid = edid;
    }

}
