package se.christianjensen.maintenance.representation.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private final String name;
    private String passwordHash;

    public User(String name) {
        this.name = name;
    }

    @JsonCreator
    public User(@JsonProperty("name") String name, @JsonProperty("passwordHash") String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
}
