package se.christianjensen.maintenance.preferences;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.christianjensen.maintenance.auth.UserDAO;

public class UserDAOTest {

    private UserDAO userDAO;


    @Before
    public void setUp() throws Exception {
      userDAO = new UserDAO();
    }

    @Test
    public void createUserShouldCreateFileWithUserInIt() throws Exception {
        userDAO.createUser("Christian", "hej");

    }

    @After
    public void tearDown() throws Exception {
        FileHelper.deleteFile("preferences.json");
    }
}