package com.krillsson.sysapi.dto.motherboard;

public class Motherboard {


    private ComputerSystem computerSystem;

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


    public ComputerSystem getComputerSystem() {
        return computerSystem;
    }


    public void setComputerSystem(ComputerSystem computerSystem) {
        this.computerSystem = computerSystem;
    }


    public UsbDevice[] getUsbDevices() {
        return usbDevices;
    }


    public void setUsbDevices(UsbDevice[] usbDevices) {
        this.usbDevices = usbDevices;
    }

}
