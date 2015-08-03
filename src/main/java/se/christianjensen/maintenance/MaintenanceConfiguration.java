package se.christianjensen.maintenance;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import se.christianjensen.maintenance.representation.config.UserConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MaintenanceConfiguration extends Configuration {

    @NotNull
    @JsonProperty
    private String sigarLocation;

    @Valid
    @NotNull
    @JsonProperty
    private UserConfiguration user;

    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }

    public String getSigarLocation() {
        return sigarLocation;
    }

    public UserConfiguration getUser() {
        return user;
    }
}
