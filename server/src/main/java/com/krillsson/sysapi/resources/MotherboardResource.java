package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.domain.motherboard.Motherboard;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.ComputerSystem;
import oshi.json.hardware.UsbDevice;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("motherboard")
@Produces(MediaType.APPLICATION_JSON)
public class MotherboardResource {
    private final ComputerSystem computerSystem;
    private final UsbDevice[] usbDevices;

    public MotherboardResource(ComputerSystem computerSystem, UsbDevice[] usbDevices) {
        this.computerSystem = computerSystem;
        this.usbDevices = usbDevices;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public Motherboard getRoot(@Auth UserConfiguration user) {
        return new Motherboard(computerSystem, usbDevices);
    }
}
