package snech;

import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import snech.web.pages.LoginPage;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    
    /**
     * Constructor
     */
	public WicketApplication()
	{
	}
	
	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class getHomePage()
	{
		return LoginPage.class;
	}
        
        @Override
        protected void init()
        {
            getRequestCycleSettings().setResponseRequestEncoding("UTF-8"); 
            getMarkupSettings().setDefaultMarkupEncoding("UTF-8"); 
            getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        }
}
