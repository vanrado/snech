package snech.core; 


import snech.core.services.IDatabaseService;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.services.IHashUtils;
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

    @SpringBean
    private IHashUtils hashUtils;

    public CustomAuthenticatedWebSession(Request request) {
        super(request);
        Injector.get().inject(this);
    }

    @Override
    public boolean authenticate(String login, String password) {
        Injector.get().inject(this);
        User userLogin = databaseService.getUserLogin(login);

        if (userLogin != null) {
            String inputPassHash = hashUtils.hashPassword(password, userLogin.getSalt());

            if (inputPassHash.equals(userLogin.getPassword())) {
                user = userLogin;
                return true;
            }

        }

        return false;
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
        Roles resultRoles = new Roles();
        
        if(isSignedIn() && user.getLogin().equals("robert_d")){
            resultRoles.add("TECHNIK");
        }
        return resultRoles;
    }
}
