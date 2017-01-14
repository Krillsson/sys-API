package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import io.dropwizard.auth.Auth;
import oshi.json.software.os.FileSystem;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("filesystems")
@Produces(MediaType.APPLICATION_JSON)
public class FileSystemResource {
    private final FileSystem fileSystem;

    public FileSystemResource(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    public FileSystem getRoot(@Auth UserConfiguration user) {
        return fileSystem;
    }
}
