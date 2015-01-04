package se.christianjensen.maintenance.auth;


import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import se.christianjensen.maintenance.db.UserDAO;
import se.christianjensen.maintenance.representation.internal.User;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {

    private UserDAO userDao;
    private Logger LOGGER = org.slf4j.LoggerFactory.getLogger(SimpleAuthenticator.class.getSimpleName());

    public SimpleAuthenticator(UserDAO userDao) {this.userDao = userDao;}

    @Override
    public com.google.common.base.Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        User user = this.userDao.getByName(credentials.getUsername());
        if (user!=null &&
                user.getName().equalsIgnoreCase(credentials.getUsername()) &&
                BCrypt.checkpw(credentials.getPassword(), user.getPasswordHash())) {
            return Optional.of(new User(credentials.getUsername()));
        }
        LOGGER.info("Unauthorized access: " + credentials.toString());
        return Optional.absent();
    }
}
