package com.krillsson.sysapi.util;

import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

public class EnvironmentUtils {
    public static void addHttpsForward(ServletContextHandler handler) {
        handler.addFilter(new FilterHolder(new Filter() {

            public void init(FilterConfig filterConfig) throws ServletException {
            }

            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                StringBuffer uri = ((HttpServletRequest) request).getRequestURL();
                if (uri.toString().startsWith("http://")) {
                    String location = "https://" + uri.substring("http://".length());
                    ((HttpServletResponse) response).sendRedirect(location);
                } else {
                    chain.doFilter(request, response);
                }
            }

            public void destroy() {
            }
        }), "/*", EnumSet.of(DispatcherType.REQUEST));
    }

    public static String[] getEndpoints(Environment environment) {
        final String NEWLINE = String.format("%n", new Object[0]);

        String[] arr = environment.jersey().getResourceConfig().getEndpointsInfo()
                .replace("The following paths were found for the configured resources:", "")
                .replace("    ", "")
                .replaceAll(" \\(.+?\\)", "")
                .split(NEWLINE);

        return Arrays.copyOfRange(arr, 2, arr.length - 1);
    }
}
