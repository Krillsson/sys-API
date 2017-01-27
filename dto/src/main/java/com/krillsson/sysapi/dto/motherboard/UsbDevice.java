package com.krillsson.sysapi.dto.motherboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "vendor",
        "vendorId",
        "productId",
        "serialNumber",
        "connectedDevices"
})
public class UsbDevice {

    @JsonProperty("name")
    private String name;
    @JsonProperty("vendor")
    private String vendor;
    @JsonProperty("vendorId")
    private String vendorId;
    @JsonProperty("productId")
    private String productId;
    @JsonProperty("serialNumber")
    private String serialNumber;
    @JsonProperty("connectedDevices")
    private UsbDevice[] connectedDevices = null;

    /**
     * No args constructor for use in serialization
     */
    public UsbDevice() {
    }

    /**
     * @param connectedDevices
     * @param vendor
     * @param name
     * @param serialNumber
     * @param vendorId
     * @param productId
     */
    public UsbDevice(String name, String vendor, String vendorId, String productId, String serialNumber, UsbDevice[] connectedDevices) {
        super();
        this.name = name;
        this.vendor = vendor;
        this.vendorId = vendorId;
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.connectedDevices = connectedDevices;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("vendor")
    public String getVendor() {
        return vendor;
    }

    @JsonProperty("vendor")
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    @JsonProperty("vendorId")
    public String getVendorId() {
        return vendorId;
    }

    @JsonProperty("vendorId")
    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    @JsonProperty("productId")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("productId")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("serialNumber")
    public String getSerialNumber() {
        return serialNumber;
    }

    @JsonProperty("serialNumber")
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @JsonProperty("connectedDevices")
    public UsbDevice[] getConnectedDevices() {
        return connectedDevices;
    }

    @JsonProperty("connectedDevices")
    public void setConnectedDevices(UsbDevice[] connectedDevices) {
        this.connectedDevices = connectedDevices;
    }

}
