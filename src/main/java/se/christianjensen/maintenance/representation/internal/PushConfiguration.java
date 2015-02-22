package se.christianjensen.maintenance.representation.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class PushConfiguration {
    @JsonProperty
    @NotEmpty
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

}
