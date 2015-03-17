package snech.web.forms;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.types.Issue;
import snech.core.types.enums.EIssueLogType;
import snech.core.types.enums.EIssuePriority;

/**
 *
 * @author Radovan Račák
 */
public class ReportForm extends Form {

    private String subject;
    private String selectedPriority;
    private String message;

    @SpringBean
    private IDatabaseService databaseService;

    public ReportForm(String id) {
        super(id);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        final Label success = new Label("success", "Vasa poziadavka sa spracovala uspesne");
        success.setOutputMarkupPlaceholderTag(true);
        success.setVisible(false);
        add(success);

        final WebMarkupContainer reportContainer = new WebMarkupContainer("reportContainer");
        reportContainer.setOutputMarkupId(true);

        final TextField subjectField = new TextField("subject", new PropertyModel(this, "subject"));
        subjectField.setRequired(true);
        subjectField.setOutputMarkupId(true);

        List<String> prioritiesList = EIssuePriority.getPrioritiesString();
        final DropDownChoice prioritiesDropDown = new DropDownChoice("priorities", new PropertyModel<String>(this, "selectedPriority"), prioritiesList) {

            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

            @Override
            protected void onSelectionChanged(Object newSelection) {
                super.onSelectionChanged(newSelection);
            }
        };
        prioritiesDropDown.setRequired(true);
        prioritiesDropDown.setOutputMarkupId(true);

        final TextArea messageTextArea = new TextArea("message", new PropertyModel(this, "message"));
        messageTextArea.setRequired(true);
        messageTextArea.setOutputMarkupId(true);

        AjaxButton confirmButton = (AjaxButton) new AjaxButton("confirm") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Issue issue = new Issue();
                String userLogin = CustomAuthenticatedWebSession.get().getUser().getLogin();
                issue.setUserLogin(userLogin);
                issue.setSubject(subject != null ? subject : "");
                issue.setPriority(EIssuePriority.getPriorityFromString(selectedPriority));
                issue.setMessage(message != null ? message : "");
                long issueId = databaseService.insertIssue(issue);
                if (issueId != -1) {
                    issue.setId(issueId);
                    success.setVisible(true);
                    reportContainer.setVisible(false);
                    if(databaseService.insertIssueLog(issue.getId(), EIssueLogType.VYTVORENIE, userLogin, "")) {
                        System.out.println("Uspesne zalogovanie");
                    } else {
                        System.out.println("Neuspesne zalogovane");
                    }
                    target.add(success);
                    target.add(feedback);
                    target.add(reportContainer);
                    target.add(this);
                    target.appendJavaScript("setTimeout(function(){ window.location.replace(\"" + urlFor(getApplication().getHomePage(), null).toString() + "\"); }, 1500);");
                } else {
                    error("Pri vytvarani nastala chyba! Akciu opakujte alebo sa obratte na technicku podporu!");
                    target.add(feedback);
                }
            }

            protected void onError(AjaxRequestTarget target, Form<?> form) {
                error("Nastala chyba pri spracovani formulara!");
                target.add(feedback);
            }

        };

        reportContainer.add(confirmButton);
        reportContainer.add(messageTextArea);
        reportContainer.add(prioritiesDropDown);
        reportContainer.add(subjectField);
        add(reportContainer);

    }

}
