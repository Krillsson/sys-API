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
    public User(@JsonProperty("name") String name, @JsonProperty("password") String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    @JsonProperty
    public String getName() {
        return name;
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

        if (!name.equalsIgnoreCase(user.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
