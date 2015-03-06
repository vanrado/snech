package snech.core;

import org.springframework.stereotype.Service;
import snech.core.types.User;

/**
 *
 * @author Radovan Račák
 */
@Service
public class DatabaseServiceImpl implements IDatabaseService {

    @Override
    public User getClient(String id, String password) {
        User user = new User();
        user.setId("jack");
        user.setPassword("1234");

        if (user.getId().equals(id) && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

}
