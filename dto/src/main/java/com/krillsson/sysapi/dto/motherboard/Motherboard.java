package com.krillsson.sysapi.dto.motherboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "computerSystem",
        "usbDevices"
})
public class Motherboard {

    @JsonProperty("computerSystem")
    private ComputerSystem computerSystem;
    @JsonProperty("usbDevices")
    private UsbDevice[] usbDevices = null;

    /**
     * No args constructor for use in serialization
     */
    public Motherboard() {
    }

    /**
     * @param computerSystem
     * @param usbDevices
     */
    public Motherboard(ComputerSystem computerSystem, UsbDevice[] usbDevices) {
        super();
        this.computerSystem = computerSystem;
        this.usbDevices = usbDevices;
    }

    @JsonProperty("computerSystem")
    public ComputerSystem getComputerSystem() {
        return computerSystem;
    }

    @JsonProperty("computerSystem")
    public void setComputerSystem(ComputerSystem computerSystem) {
        this.computerSystem = computerSystem;
    }

    @JsonProperty("usbDevices")
    public UsbDevice[] getUsbDevices() {
        return usbDevices;
    }

    @JsonProperty("usbDevices")
    public void setUsbDevices(UsbDevice[] usbDevices) {
        this.usbDevices = usbDevices;
    }

}
