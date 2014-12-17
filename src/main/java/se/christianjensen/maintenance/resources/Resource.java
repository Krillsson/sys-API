package se.christianjensen.maintenance.resources;

import se.christianjensen.maintenance.sigar.AbstractSigarMetric;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

abstract public class Resource<T extends AbstractSigarMetric> {

    public abstract <T extends Object> T getRoot();

    protected WebApplicationException buildWebException(Response.Status status, String message) {
        return new WebApplicationException(
                Response.status(status)
                        .entity(new Object() {
                            String errorMessage = message;
                        })
                        .build());
    }
}
