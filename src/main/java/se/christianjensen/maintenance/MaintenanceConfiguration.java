package se.christianjensen.maintenance;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import se.christianjensen.maintenance.representation.internal.PushConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MaintenanceConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }

    @Valid
    @JsonProperty
    private PushConfiguration push;

    public PushConfiguration getPushConfiguration() {
        return push;
    }
}
