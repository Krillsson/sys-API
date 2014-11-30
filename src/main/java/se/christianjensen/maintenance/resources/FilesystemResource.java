package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.representation.filesystem.FileSystem;
import se.christianjensen.maintenance.sigar.FilesystemMetrics;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("filesystem")
@Produces(MediaType.APPLICATION_JSON)
public class FilesystemResource {
    FilesystemMetrics filesystemMetrics;

    public FilesystemResource(FilesystemMetrics filesystemMetrics) {
        this.filesystemMetrics = filesystemMetrics;
    }

    @GET
    public List<FileSystem> filesystem(){
        return filesystemMetrics.filesystems();
    }
}
