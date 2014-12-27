package se.christianjensen.maintenance.auth;


import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.mindrot.jbcrypt.BCrypt;
import se.christianjensen.maintenance.preferences.UserDAO;
import se.christianjensen.maintenance.representation.internal.User;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {

    UserDAO userDao;

    public SimpleAuthenticator(UserDAO userDao) {this.userDao = userDao;}

    @Override
    public com.google.common.base.Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        User user = this.userDao.getUserByName(credentials.getUsername());
        if (user!=null &&
                user.getName().equalsIgnoreCase(credentials.getUsername()) &&
                BCrypt.checkpw(credentials.getPassword(), user.getPasswordHash())) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.absent();
    }
}
