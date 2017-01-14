package com.krillsson.sysapi.domain.motherboard;

import oshi.json.hardware.ComputerSystem;
import oshi.json.hardware.UsbDevice;

public class Motherboard
{
    private final ComputerSystem computerSystem;
    private final UsbDevice[] usbDevices;

    public Motherboard(ComputerSystem computerSystem, UsbDevice[] usbDevices)
    {
        this.computerSystem = computerSystem;
        this.usbDevices = usbDevices;
    }

    public ComputerSystem getMotherboard() {
        return computerSystem;
    }

    public UsbDevice[] getUsbDevices() {
        return usbDevices;
    }
}
