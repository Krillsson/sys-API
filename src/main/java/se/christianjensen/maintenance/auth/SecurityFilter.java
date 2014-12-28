package se.christianjensen.maintenance.auth;

import com.sun.jersey.api.core.ExtendedUriInfo;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import io.dropwizard.auth.Auth;

import javax.ws.rs.core.Context;

public class SecurityFilter implements ContainerRequestFilter {
    @Context
    ExtendedUriInfo extendedUriInfo;


    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) {
        boolean methodNeedsAuth = extendedUriInfo.getMatchedMethod().isAnnotationPresent(Auth.class);


        return null;
    }
}
