package snech.web.forms.client;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
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
import snech.web.pages.client.TicketDetailPage;

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
    private ArrayList<Long> selectedIds = new ArrayList<>();
    private final int ITEMS_PER_PAGE = 5;

    public IssuesForm(String id) {
        super(id);
        setOutputMarkupId(true);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        final User logedUser = CustomAuthenticatedWebSession.get().getUser();
        issues = databaseService.getIssues(logedUser.getLogin(), false);

        final Label issuesForDelete = new Label("issuesForDelete", "");
        issuesForDelete.setOutputMarkupId(true);
        add(issuesForDelete);

        final Label confirmationQuestion = new Label("confirmationQuestion", "Nieje co zmazat! Neoznacili ste ziadny ticket!");
        confirmationQuestion.setOutputMarkupId(true);
        add(confirmationQuestion);

        Button deleteButton = new Button("deleteConfirmation.button");
        deleteButton.setDefaultFormProcessing(false);
        add(deleteButton);

        ListDataProvider<Issue> issueDataProvider = new ListDataProvider<>(issues);
        DataView<Issue> issueDataView = new DataView<Issue>("issue", issueDataProvider) {

            @Override
            protected void populateItem(Item<Issue> listItem) {
                final Issue issue = listItem.getModelObject();

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
                        if (issue.getSelected() && !selectedIds.contains(issue.getId())) {
                            //checkbox changes to true
                            selectedIds.add(issue.getId());
                        } else {
                            //checkbox changes to false
                            selectedIds.remove(issue.getId());
                        }

                        if (!selectedIds.isEmpty()) {
                            confirmationQuestion.setDefaultModelObject("Naozaj si prajete odstranit poziadavky s id ");
                            issuesForDelete.setDefaultModelObject(selectedIdsToString() + " ?");
                        } else {
                            confirmationQuestion.setDefaultModelObject("Nieje co zmazat! Neoznacili ste ziadny ticket!");
                            issuesForDelete.setDefaultModelObject("");
                        }
                        target.add(confirmationQuestion);
                        target.add(issuesForDelete);
                    }
                };
                listItem.add(checkBox);

                listItem.add(new Label("issueStatus", issue.getStatus().getName())); //issue.getStatus().getName()));
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
                listItem.add(new Label("estimatedDate", formatUtils.getFormatedDate(issue.getEstimatedDate())));
                listItem.add(new Label("createdOnDate", formatUtils.getFormatedDate(issue.getCreatedDate())));
                Label progress = new Label("progress", issue.getProgress() + "%");
                progress.add(new AttributeModifier("style", "width: " + issue.getProgress() + "%"));
                listItem.add(progress);
            }
        };
        final WebMarkupContainer tableContainer = new WebMarkupContainer("table.container");
        tableContainer.setOutputMarkupId(true);
        tableContainer.add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) {

            @Override
            final public void onTimer(AjaxRequestTarget target) {
                List list = databaseService.getIssues(logedUser != null ? logedUser.getLogin() : "", false);
                replaceIssues(list);
                setSelectedValueToIssues();
                target.add(tableContainer);
            }

        });

        tableContainer.add(issueDataView);
        add(tableContainer);
        issueDataView.setItemsPerPage(ITEMS_PER_PAGE);

        PagingNavigator pagination = new PagingNavigator("pagination", issueDataView);
        if (issueDataView.getItemCount() <= ITEMS_PER_PAGE) {
            pagination.setVisible(false);
        }
        add(pagination);

        AjaxButton submitButton = new AjaxButton("submit.button") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (!selectedIds.isEmpty()) {
                    for (Long id : selectedIds) {
                        info("Poziadavka s id #" + id + " bola zmazana!");
                        databaseService.setIssueStatus(EIssueStatus.VYMAZANA, id, logedUser.getLogin());
                    }
                } else {
                    error("Ziadna poziadavka nieje oznacena na vymazanie!");
                }

                List list = databaseService.getIssues(logedUser != null ? logedUser.getLogin() : "", false);
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

    /**
     * Skontroluje oznacene poziadavky a nastavi podla toho aj list s vypisanymi
     * poziadavkami. Takze nam to zabezpeci to, ze ked Ajax refresuje kazdych 5s
     * tabulku s vypisom tak sa nam zachova oznaceny checkbox
     */
    private void setSelectedValueToIssues() {
        for (Issue issue : issues) {
            if (selectedIds.contains(issue.getId())) {
                issue.setSelected(true);
            }
        }
    }

}
