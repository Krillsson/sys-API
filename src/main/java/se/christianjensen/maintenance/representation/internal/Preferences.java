package se.christianjensen.maintenance.representation.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Preferences {
    private Date savedTimeStamp;
    private List<User> users = new ArrayList<>();

    public Date getSavedTimeStamp()
    {
        return savedTimeStamp;
    }

    public void setSavedTimeStamp(Date savedTimeStamp) {
        this.savedTimeStamp = savedTimeStamp;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
