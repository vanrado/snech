package snech.web.pages.client;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import snech.web.base.MainPage;
import snech.web.forms.client.TicketEditForm;

/**
 *
 * @author Radovan Račák
 */
public class TicketEditPage extends MainPage {

    public TicketEditPage(final PageParameters pageParameters) {
        super(pageParameters);
        add(new TicketEditForm("edit.form", pageParameters));
    }
}
