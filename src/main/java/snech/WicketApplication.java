package snech;

import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.file.Folder;
import snech.core.CustomAuthenticatedWebSession;
import snech.web.pages.AccountManagementPage;
import snech.web.pages.ChangeDetailsPage;
import snech.web.pages.ChangePasswordPage;
import snech.web.pages.LoginPage;
import snech.web.pages.OverviewPage;
import snech.web.pages.TicketDetailPage;
import snech.web.pages.TicketsListPage;

/**
 * Application object for your web application. If you want to run this
 * application without deploying, run the Start class.
 *
 * @see wicket.myproject.Start#main(String[])
 */
public class WicketApplication extends AuthenticatedWebApplication {

    private Folder uploadFolder = null;

    /**
     * Constructor
     */
    public WicketApplication() {
    }

    /**
     * @see wicket.Application#getHomePage()
     */
    public Class getHomePage() {
        return TicketsListPage.class;
    }

    @Override
    protected void init() {
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        mountPages();

        //Upload
        uploadFolder = new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
        uploadFolder.mkdirs();
        getApplicationSettings().setUploadProgressUpdatesEnabled(true);
    }

    @Override
    protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
        return CustomAuthenticatedWebSession.class;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return LoginPage.class;
    }

    private void mountPages() {
        mountPage("/prihlasenie", LoginPage.class);
        mountPage("/domov", TicketsListPage.class);
        mountPage("/prehlad", OverviewPage.class);
        mountPage("/administracia", AccountManagementPage.class);
        mountPage("/zmena-profilu", ChangeDetailsPage.class);
        mountPage("/zmena-hesla", ChangePasswordPage.class);
        mountPage("/detail-poziadavky", TicketDetailPage.class);
    }

    public Folder getUploadFolder() {
        return uploadFolder;
    }
}
