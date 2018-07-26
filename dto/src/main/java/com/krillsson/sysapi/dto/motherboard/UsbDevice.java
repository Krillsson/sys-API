package com.krillsson.sysapi.dto.motherboard;

public class UsbDevice {


    private String name;

    private String vendor;

    private String vendorId;

    private String productId;

    private String serialNumber;

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


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getVendor() {
        return vendor;
    }


    public void setVendor(String vendor) {
        this.vendor = vendor;
    }


    public String getVendorId() {
        return vendorId;
    }


    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public String getProductId() {
        return productId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getSerialNumber() {
        return serialNumber;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public UsbDevice[] getConnectedDevices() {
        return connectedDevices;
    }


    public void setConnectedDevices(UsbDevice[] connectedDevices) {
        this.connectedDevices = connectedDevices;
    }

}
