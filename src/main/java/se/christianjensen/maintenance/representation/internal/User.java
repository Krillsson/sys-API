package se.christianjensen.maintenance.representation.internal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class User {

    private UUID uuid;
    private String name;
    private String passwordHash;

    public User(String name) {
        this.name = name;
    }

    public User(String name, String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.uuid = UUID.randomUUID();
    }

    @JsonCreator
    public User(@JsonProperty("uuid") UUID uuid, @JsonProperty("name") String name, @JsonProperty("password") String passwordHash) {
        this.uuid = uuid;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    @JsonProperty
    public UUID getUuid() {
        return uuid;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("password")
    public String getPasswordHash() {
        return passwordHash;
    }

    @JsonProperty("password")
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!uuid.equals(user.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid != null ? uuid.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        return result;
    }
}
