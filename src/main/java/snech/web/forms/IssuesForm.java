package snech.web.forms;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.Issue;
import snech.core.types.User;
import snech.core.types.enums.EIssueStatus;
import snech.web.pages.TicketDetailPage;

/**
 *
 * @author Radovan Račák
 */
public class IssuesForm extends Form {

    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IFormatUtils formatUtils;

    private List<Issue> issues;
    private ArrayList<Long> selectedIds = new ArrayList<Long>();

    public IssuesForm(String id) {
        super(id);
        setOutputMarkupId(true);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        final User logedUser = CustomAuthenticatedWebSession.get().getUser();
        issues = databaseService.getIssues(logedUser != null ? logedUser.getLogin() : "");

        final Label issuesForDelete = new Label("issuesForDelete", "");
        issuesForDelete.setOutputMarkupId(true);
        add(issuesForDelete);

        Button deleteButton = new Button("deleteConfirmation.button");
        deleteButton.setDefaultFormProcessing(false);
        add(deleteButton);

        final ListView<Issue> issueListView = new ListView<Issue>("issue", issues) {

            @Override
            protected void populateItem(ListItem<Issue> listItem) {
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

                AjaxCheckBox checkBox = new AjaxCheckBox("check", new PropertyModel<Boolean>(listItem.getModelObject(), "selected")) {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        if (issue.getSelected()) {
                            //checkbox changes to true
                            selectedIds.add(issue.getId());
                        } else {
                            //checkbox changes to false
                            selectedIds.remove(issue.getId());
                        }

                        issuesForDelete.setDefaultModelObject(selectedIdsToString());
                        target.add(issuesForDelete);
                    }
                };
                listItem.add(checkBox);

                listItem.add(new Label("issueStatus", EIssueStatus.NOVA.getName())); //issue.getStatus().getName()));
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

                long estimatedTime = 4000;//issue.getEstimatedDate().getTime();
                long lastUpdatedDate = 4000;//issue.getLastUpdatedDate().getTime();
                listItem.add(new Label("estimatedDate", formatUtils.getFormatedDate(estimatedTime)));
                listItem.add(new Label("lastUpdatedDate", formatUtils.getFormatedDate(lastUpdatedDate)));
            }
        };
        final WebMarkupContainer tableContainer = new WebMarkupContainer("table.container");
        tableContainer.setOutputMarkupId(true);
        tableContainer.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
        add(tableContainer);

        tableContainer.add(issueListView);

        AjaxButton submitButton = new AjaxButton("submit.button") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (!selectedIds.isEmpty()) {
                    for (Long id : selectedIds) {
                        info("Poziadavka s id #" + id + " bola zmazana!");
                        databaseService.removeIssue(id);
                    }
                } else {
                    error("Ziadna poziadavka nieje oznacena na vymazanie!");
                }

                List list = databaseService.getIssues(logedUser != null ? logedUser.getLogin() : "");
                replaceIssues(list);
                selectedIds.clear();
                target.add(issuesForDelete.setDefaultModelObject(""));
                target.add(feedback);
                target.add(tableContainer);
            }

        };
        submitButton.setDefaultFormProcessing(false);
        add(submitButton);
    }

    /**
     *
     * @return vrati String vo formate #id1 #id2 #id3
     */
    private String selectedIdsToString() {
        String str = "";

        for (Long id : selectedIds) {
            str += "#" + id + " ";
        }

        return str;
    }

    /**
     *
     * @param list
     */
    private void replaceIssues(List list) {
        issues.clear();
        issues.addAll(list);
    }

}
