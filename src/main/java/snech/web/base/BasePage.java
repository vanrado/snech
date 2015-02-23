package snech.web.base;

import org.apache.wicket.markup.html.WebPage;

import java.io.Serializable;
import snech.web.panels.base.FooterPanel;

/**
 * Created by Radovan on 23.11.2014.
 */
public class BasePage extends WebPage implements Serializable {
    public BasePage(){
        add(new FooterPanel("footer.panel"));
    }
}
