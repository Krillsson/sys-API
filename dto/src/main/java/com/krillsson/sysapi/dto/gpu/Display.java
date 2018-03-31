package com.krillsson.sysapi.dto.gpu;

public class Display {


    private String edid;

    public Display() {
    }

    public Display(String edid) {
        this.edid = edid;
    }


    public String getEdid() {
        return edid;
    }


    public void setEdid(String edid) {
        this.edid = edid;
    }

}
