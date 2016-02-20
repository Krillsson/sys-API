package com.krillsson.sysapi.resources;

import com.krillsson.sysapi.provider.InfoProvider;
import io.dropwizard.auth.Auth;
import com.krillsson.sysapi.UserConfiguration;
import com.krillsson.sysapi.auth.BasicAuthorizer;
import com.krillsson.sysapi.domain.drive.FileSystemType;
import com.krillsson.sysapi.domain.drive.Drive;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("drives")
@Produces(MediaType.APPLICATION_JSON)
public class DriveResource extends Resource {
    InfoProvider provider;

    public DriveResource(InfoProvider provider) {
        this.provider = provider;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    @Override
    public List<Drive> getRoot(@Auth UserConfiguration user) {
        return provider.drives();
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    @Path("type/{fsTypeName}")
    public List<Drive> getFsByType(@Auth UserConfiguration user, @PathParam("fsTypeName") String fsTypeName) {
        FileSystemType fsType;
        List<Drive> fileSystems;
        try {
            fsType = FileSystemType.valueOf(fsTypeName);
            fileSystems = provider.getFileSystemsWithCategory(fsType);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
        return fileSystems;
    }

    @GET
    @RolesAllowed(BasicAuthorizer.AUTHENTICATED_ROLE)
    @Path("{id}")
    public Drive getFsById(@Auth UserConfiguration user, @PathParam("id") String id) {
        Drive fileSystem;
        try {
            fileSystem = provider.getFileSystemById(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
        return fileSystem;
    }

}
