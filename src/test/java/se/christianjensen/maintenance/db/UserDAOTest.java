package se.christianjensen.maintenance.db;


import org.junit.Before;
import org.junit.Test;
import se.christianjensen.maintenance.representation.internal.Device;
import se.christianjensen.maintenance.representation.internal.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class UserDAOTest {

    private UserDAO userDAO;
    private User user1;
    private User user2;
    private List<User> users;

    @Before
    public void setUp() throws Exception {
        user1 = new User("User1", "password1");
        user2 = new User("User2", "password2");
        users = Arrays.asList(user1, user2);
        userDAO = new UserDAO() {
            @Override
            protected List<User> setUsers() {
                return users;
            }

            @Override
            protected void persistUserStore() {
                //do nothing
            }
        };
    }

    @Test
    public void getUserByNameShouldReturnCorrectUser() throws Exception {
        User user = userDAO.getByName("User1");
        assertThat(user, equalTo(user1));
    }

    @Test
    public void getUserByNameShouldBeCaseInsensitive() throws Exception {
        User user = userDAO.getByName("uSeR1");
        assertThat(user, equalTo(user1));
    }

    @Test
    public void nonExistentUserShouldReturnNothing() throws Exception {
        assertNull(userDAO.getByName("nada"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateUserThatDoesNotExistShouldFail() throws Exception {
        userDAO.update(new User("", ""));
    }

    @Test
    public void updateUserThatExists() throws Exception {
        User user = new User(user1.getUuid(), "derp", "derp", new Device());
        userDAO.update(user);
        Boolean pass = false;

        for (User u : users) {
            if (Objects.equals(u.getName(), "derp")) {
                pass = true;
            }
        }
        if (pass == false) {
            fail();
        }
    }
}