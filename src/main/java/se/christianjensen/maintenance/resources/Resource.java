package se.christianjensen.maintenance.resources;

import io.dropwizard.auth.Auth;
import se.christianjensen.maintenance.representation.config.UserConfiguration;
import se.christianjensen.maintenance.representation.internal.User;
import se.christianjensen.maintenance.sigar.AbstractSigarMetric;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
abstract public class Resource<T extends AbstractSigarMetric> {
    public abstract <T extends Object> T getRoot(@Auth UserConfiguration user);

    protected WebApplicationException buildWebException(Response.Status status, String message) {
        return new WebApplicationException(
                Response.status(status)
                        .entity(new MessageObject(message))
                        .build());
    }

    public static class MessageObject {
        private final String errorMessage;

        public MessageObject(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
