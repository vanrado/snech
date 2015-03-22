package snech.web.pages;

import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.services.IDatabaseService;
import snech.core.types.Issue;
import snech.core.types.enums.EIssuePriority;
import snech.web.base.MainPage;

/**
 *
 * @author Radovan Račák
 */
public class TicketEditPage extends MainPage {

    @SuppressWarnings("FieldMayBeFinal")
    @SpringBean
    private IDatabaseService databaseService;
    private String subject;
    private String selectedPriority;
    private String message;
    private Issue issue;

    public TicketEditPage(final PageParameters pageParameters) {
        super(pageParameters);
        issue = databaseService.getIssue(Long.parseLong(pageParameters.get("id").toString()));
        subject = issue.getSubject();
        selectedPriority = issue.getPriority().name();
        message = issue.getMessage();
        Form editForm = new Form("edit.form");

        editForm.add(new FeedbackPanel("feedback"));
        editForm.add(new Label("issueId", issue.getId()));
        TextField<String> subjectField = new TextField<>("issueSubject", new PropertyModel<String>(this, "subject"));
        editForm.add(subjectField);

        List<String> prioritiesList = EIssuePriority.getPrioritiesString();
        final DropDownChoice prioritiesDropDown = new DropDownChoice("priorities", new PropertyModel<String>(this, "selectedPriority"), prioritiesList);
        prioritiesDropDown.setRequired(true);
        editForm.add(prioritiesDropDown);

        final TextArea messageTextArea = new TextArea("message", new PropertyModel(this, "message"));
        messageTextArea.setRequired(true);
        editForm.add(messageTextArea);

        
        editForm.add(new Button("save.button") {

            @Override
            public void onError() {
                super.onError();
                error("Error on submit");
            }

            
            @Override
            public void onSubmit() {
                super.onSubmit();
                issue.setSubject(subject);
                issue.setPriority(EIssuePriority.valueOf(selectedPriority));
                issue.setMessage(message);
                databaseService.updateIssue(issue);
                info("Uspesne aktualizovane");
            }

        });
        editForm.add(new Link("cancel.button") {

            @Override
            public void onClick() {
                setResponsePage(TicketDetailPage.class, pageParameters);
            }

        });
        
        add(editForm);
    }
}
