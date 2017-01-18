/*
 * Sys-Api (https://github.com/Krillsson/sys-api)
 *
 * Copyright 2017 Christian Jensen / Krillsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Maintainers:
 * contact[at]christian-jensen[dot]se
 */
package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.config.UserConfiguration;
import com.krillsson.sysapi.core.domain.motherboard.Motherboard;
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
