package snech.core;

import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import snech.core.types.User;

/**
 *
 * @author Radovan Račák
 */
public interface IDatabaseService {
    public User getClient(String id, String password);
}
