package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.system.UserInfo;
import com.krillsson.sysapi.sigar.SystemSigar;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource extends Resource{

    private SystemSigar systemSigar;

    public UsersResource(SystemSigar systemSigar) {
        this.systemSigar = systemSigar;
    }

    @Override
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public List<UserInfo> getRoot(@Auth UserConfiguration user) {
        return systemSigar.getUsers();
    }
}
