package com.krillsson.sysapi.oshi;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.system.JvmProperties;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("system")
@Produces(MediaType.APPLICATION_JSON)
public class SystemResource {
    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public JvmProperties getRoot(@Auth UserConfiguration user) {
        Properties p = java.lang.System.getProperties();
        return new JvmProperties(
                p.getProperty("java.home"),
                p.getProperty("java.class.path"),
                p.getProperty("java.vendor"),
                p.getProperty("java.vendor.url"),
                p.getProperty("java.version"),
                p.getProperty("os.arch"),
                p.getProperty("os.name"),
                p.getProperty("os.version"),
                p.getProperty("user.dir"),
                p.getProperty("user.home"),
                p.getProperty("user.name"));
    }
}
