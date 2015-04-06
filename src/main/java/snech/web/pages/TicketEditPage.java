package snech.web.pages;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.services.IDatabaseService;
import snech.core.types.Attachment;
import snech.core.types.Issue;
import snech.core.types.enums.EIssuePriority;
import snech.web.base.MainPage;
import snech.web.forms.TicketEditForm;

/**
 *
 * @author Radovan Račák
 */
public class TicketEditPage extends MainPage {

    public TicketEditPage(final PageParameters pageParameters) {
        super(pageParameters);
        add(new TicketEditForm("edit.form", pageParameters));
    }

//    private void replaceContainer(List list) {
//        attachmentsToDelete.clear();
//        attachmentsToDelete.addAll(list);
//    }
}
