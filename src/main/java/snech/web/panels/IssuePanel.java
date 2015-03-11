package snech.web.panels;

import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.Issue;
import snech.core.types.enums.EIssueStatus;
import snech.web.pages.TicketDetailPage;

/**
 *
 * @author Radovan Račák
 */
public class IssuePanel extends Panel {

    @SpringBean
    private IDatabaseService databaseService;
    
    @SpringBean
    private IFormatUtils formatUtils;

    public IssuePanel(String id) {
        super(id);
        List<Issue> issues = databaseService.getIssues("robert_u");
        add(new ListView<Issue>("issue", issues) {

            @Override
            protected void populateItem(final ListItem<Issue> listItem) {
                final Issue issue = (Issue) listItem.getModelObject();

                listItem.add(new Label("id", issue.getId()));
                listItem.add(new Link("detailsLink") {

                    @Override
                    protected void onInitialize() {
                        super.onInitialize();
                        add(new Label("detailsLinkLabel", issue.getSubject()));
                    }

                    @Override
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.add("id", issue.getId());
                        setResponsePage(TicketDetailPage.class, params);
                    }
                });
                listItem.add(new Label("issueStatus", issue.getStatus().getName()));
                listItem.add(new Link("contactAdminLink") {

                    @Override
                    protected void onInitialize() {
                        super.onInitialize();
                        add(new Label("contactName", issue.getAssignedAdminId()));
                    }

                    @Override
                    public void onClick() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
                listItem.add(new Label("priority", issue.getPriority()));
                
                long estimatedTime = issue.getEstimatedDate().getTime();
                long lastUpdatedDate = issue.getLastUpdatedDate().getTime();
                listItem.add(new Label("estimatedDate", formatUtils.getFormatedDate(estimatedTime)));
                listItem.add(new Label("lastUpdatedDate", formatUtils.getFormatedDate(lastUpdatedDate)));
            }
        });
    }

}
