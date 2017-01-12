package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.json.hardware.UsbDevice;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("usbdevices")
@Produces(MediaType.APPLICATION_JSON)
public class UsbDevicesResource {
    private final UsbDevice[] devices;

    public UsbDevicesResource(UsbDevice[] devices) {
        this.devices = devices;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public UsbDevice[] getRoot(@Auth UserConfiguration user) {
        return devices;
    }
}
