package snech.web.panels;

import java.io.Serializable;
import java.util.List;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.IssueLog;
import snech.core.types.User;
import snech.core.types.enums.EIssueLogType;
import snech.web.pages.TicketDetailPage;

/**
 *
 * @author Radovan Račák
 */
public class OverviewPanel extends Panel {

    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IFormatUtils formatUtils;

    private List<IssueLog> issueLogs;

    public OverviewPanel(String id) {
        super(id);
        setOutputMarkupId(true);
        final User logedUser = CustomAuthenticatedWebSession.get().getUser();
        issueLogs = databaseService.getIssueLogs(logedUser.getLogin());
        final WebMarkupContainer logContainer = new WebMarkupContainer("log.container");
        logContainer.setOutputMarkupId(true);
        logContainer.add(new AbstractAjaxTimerBehavior(Duration.seconds(5)) {
            
            @Override
            protected void onTimer(AjaxRequestTarget target) {
                replaceIssues(databaseService.getIssueLogs(logedUser.getLogin()));
                target.add(logContainer);
            }
        });
        add(logContainer);
        
        ListView<IssueLog> logList = new ListView<IssueLog>("log", issueLogs) {

            @Override
            protected void populateItem(ListItem<IssueLog> item) {
                final IssueLog log = item.getModelObject();
                String logActivity = "";
                final EIssueLogType logType = log.getLogType() != null ? log.getLogType() : EIssueLogType.INE;
                Label activityIcon = new Label("activityIcon", "");
                activityIcon.add(new AttributeAppender("class", new Model() {

                    @Override
                    public Serializable getObject() {
                        String cssClass;

                        if (logType.equals(EIssueLogType.VYTVORENIE)) {
                            cssClass = " glyphicon-plus";
                        } else if (logType.equals(EIssueLogType.ZMAZANIE)) {
                            cssClass = " glyphicon-minus";
                        } else {
                            cssClass = " glyphicon-exclamation-sign";
                        }

                        return cssClass;
                    }

                }));

                item.add(activityIcon);
                item.add(new Label("author", log.getAuthorLogin()));
                item.add(new Label("formatedDate", formatUtils.getFormatedDate(log.getCreatedOn())));
                item.add(new Label("content", log.getDescription()));
                item.add(new Label("activity", logType.getActivity()));
                item.add(new Link("detailsLink") {

                    @Override
                    protected void onInitialize() {
                        super.onInitialize();
                        add(new Label("detailsLinkLabel", "#" + log.getIssueId()));
                    }

                    @Override
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.add("id", log.getIssueId());
                        setResponsePage(TicketDetailPage.class, params);
                    }
                });
            }
        };

        logContainer.add(logList);

    }
    
    private void replaceIssues(List list) {
        issueLogs.clear();
        issueLogs.addAll(list);
    }

}
