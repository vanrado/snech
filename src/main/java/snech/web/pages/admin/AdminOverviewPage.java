package snech.web.pages.admin;

import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.Issue;
import snech.core.types.User;
import snech.web.base.AdminBasePage;
import snech.web.panels.admin.IssueEditPanel;

/**
 *
 * @author vanrado
 */
public class AdminOverviewPage extends AdminBasePage {

    private List<Issue> issues;

    private final int ITEMS_PER_PAGE = 5;

    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IFormatUtils formatUtils;

    private Issue selectedIssue;
    private IssueEditPanel editPanel;

    public AdminOverviewPage() {
        final User logedUser = CustomAuthenticatedWebSession.get().getUser();
        issues = databaseService.getIssues("admin", false);

        ListDataProvider<Issue> issueDataProvider = new ListDataProvider<>(issues);
        DataView<Issue> issueDataView = new DataView<Issue>("issue", issueDataProvider) {

            @Override
            protected void populateItem(Item<Issue> listItem) {
                final Issue issue = listItem.getModelObject();
                listItem.add(new AjaxLink("edit.link") {

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        selectedIssue = issue;
                        editPanel.setDefaultModel(new Model<Issue>(selectedIssue));
                        target.appendJavaScript("$(document).ready(function () { $(function () { $(\"#datepicker\").datepicker(); }); });");
                        target.appendJavaScript("$(document).ready(function () { $(function () { $('#datetimepicker2').datetimepicker({ locale: 'sk' }); }); });");
                        target.add(editPanel);
                    }
                });
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
                        //setResponsePage(TicketDetailPage.class, params);
                    }
                });
                listItem.add(new Label("issueStatus", issue.getStatus().getName()));
                listItem.add(new Label("priority", issue.getPriority()));
                listItem.add(new Label("estimatedDate", formatUtils.getFormatedDate(issue.getEstimatedDate())));
                listItem.add(new Label("createdOnDate", formatUtils.getFormatedDate(issue.getCreatedDate())));
                Label progress = new Label("progress", issue.getProgress() + "%");
                progress.add(new AttributeModifier("style", "width: " + issue.getProgress() + "%"));
                listItem.add(progress);
            }

        };
        issueDataView.setItemsPerPage(ITEMS_PER_PAGE);
        add(issueDataView);

        PagingNavigator pagination = new PagingNavigator("pagination", issueDataView);
        if (issueDataView.getItemCount() <= ITEMS_PER_PAGE) {
            pagination.setVisible(false);
        }
        add(pagination);

        editPanel = new IssueEditPanel("edit.panel");
        editPanel.setOutputMarkupId(true);
        add(editPanel);
    }

    public Issue getSelectedIssue() {
        return selectedIssue;
    }
}
