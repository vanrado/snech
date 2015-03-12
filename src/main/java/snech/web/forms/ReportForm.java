package snech.web.forms;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;
import snech.core.types.enums.EIssuePriority;

/**
 *
 * @author Radovan Račák
 */
public class ReportForm extends Form {

    private String subject;
    private String selectedPriority;
    private String message;

    public ReportForm(String id) {
        super(id);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        final WebMarkupContainer reportContainer = new WebMarkupContainer("reportContainer");
        reportContainer.setOutputMarkupId(true);

        final TextField subjectField = new TextField("subject", new PropertyModel(this, "subject"));
        subjectField.setRequired(true);
        subjectField.setOutputMarkupId(true);

        List<String> prioritiesList = EIssuePriority.getPrioritiesString();
        final DropDownChoice prioritiesDropDown = new DropDownChoice("priorities", new PropertyModel(this, "selectedPriority"), prioritiesList);
        prioritiesDropDown.setRequired(true);
        prioritiesDropDown.setOutputMarkupId(true);

        final TextArea messageTextArea = new TextArea("message", new PropertyModel(this, "message"));
        messageTextArea.setRequired(true);
        messageTextArea.setOutputMarkupId(true);

        AjaxButton confirmButton = (AjaxButton) new AjaxButton("confirm") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                info("Vasa poziadavka sa spracovala uspesne");
                reportContainer.setVisible(false);
                target.add(feedback);
                target.add(reportContainer);
                target.add(this);
            }

            protected void onError(AjaxRequestTarget target, Form<?> form) {
                error("Nastala chyba pri spracovani formulara!");
                target.add(feedback);
            }

        }.setOutputMarkupId(true);

        reportContainer.add(confirmButton);
        reportContainer.add(messageTextArea);
        reportContainer.add(prioritiesDropDown);
        reportContainer.add(subjectField);
        add(reportContainer);
    }

}
