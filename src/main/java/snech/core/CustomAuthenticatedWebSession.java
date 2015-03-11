package snech.core;

import snech.core.services.IDatabaseService;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.types.User;

/**
 *
 * @author Radovan Račák
 */
public class CustomAuthenticatedWebSession extends AuthenticatedWebSession {

    /**
     * serial uid
     */
    private static final long serialVersionUID = 1L;
    private User user;

    @SpringBean
    private IDatabaseService databaseService;

    public CustomAuthenticatedWebSession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String username, String password) {
        Injector.get().inject(this);
        User users = databaseService.getClient(username, password);

        if (users != null) {
            user = users;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void signOut() {
        super.signOut();
        user = null;
    }

    public User getUser() {
        return user;
    }

    public static CustomAuthenticatedWebSession get() {
        CustomAuthenticatedWebSession session = (CustomAuthenticatedWebSession) AuthenticatedWebSession.get();

        if (session.getSizeInBytes() / 1024 > 256) {
            // TODO Zalogovanie
            //logger.warn("session" + session.getId() + " has size in kB: " + session.getSizeInBytes() / 1024);
        }

        return session;
    }

    @Override
    public Roles getRoles() {
        return null;
    }
}
