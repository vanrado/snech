package snech.web.pages.admin;

import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
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
