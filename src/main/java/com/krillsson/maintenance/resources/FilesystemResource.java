package com.krillsson.maintenance.resources;

import io.dropwizard.auth.Auth;
import com.krillsson.maintenance.representation.config.UserConfiguration;
import com.krillsson.maintenance.representation.filesystem.FSType;
import com.krillsson.maintenance.representation.filesystem.FileSystem;
import com.krillsson.maintenance.sigar.FilesystemMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("filesystem")
@Produces(MediaType.APPLICATION_JSON)
public class FilesystemResource extends Resource {
    FilesystemMetrics filesystemMetrics;

    public FilesystemResource(FilesystemMetrics filesystemMetrics) {
        this.filesystemMetrics = filesystemMetrics;
    }

    @GET
    @Override
    public List<FileSystem> getRoot(@Auth UserConfiguration user) {
        return filesystemMetrics.getFilesystems();
    }

    @GET
    @Path("type/{fsTypeName}")
    public List<FileSystem> getFsByType(@Auth UserConfiguration user, @PathParam("fsTypeName") String fsTypeName) {
        FSType fsType;
        List<FileSystem> fileSystems;
        try {
            fsType = FSType.valueOf(fsTypeName);
            fileSystems = filesystemMetrics.getFileSystemsWithCategory(fsType);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
        return fileSystems;
    }

    @GET
    @Path("{id}")
    public FileSystem getFsById(@Auth UserConfiguration user, @PathParam("id") String id) {
        FileSystem fileSystem;
        try {
            fileSystem = filesystemMetrics.getFileSystemById(id);
        } catch (IllegalArgumentException e) {
            throw buildWebException(Response.Status.NOT_FOUND, e.getMessage());
        }
        return fileSystem;
    }

}
