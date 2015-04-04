package snech.web.forms;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.wicket.Application;
import java.util.logging.Logger;
import java.util.List;
import java.util.logging.Level;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.WicketApplication;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.types.Issue;
import snech.core.types.enums.EIssuePriority;

/**
 *
 * @author Radovan Račák
 */


public class ReportForm extends Form {

    private String subject;
    private String selectedPriority;
    private String message;
    private List<FileUpload> filesToUpload;
    private Collection<FileUpload> uploads;

    @SpringBean
    private IDatabaseService databaseService;
//    private final FileUploadField fileUploadField;

    public ReportForm(String id) {
        super(id);
        filesToUpload = new ArrayList<>();

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
        final DropDownChoice prioritiesDropDown = new DropDownChoice("priorities", new PropertyModel<String>(this, "selectedPriority"), prioritiesList);

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

        setMultiPart(true);
        reportContainer.add(new MultiFileUploadField("fileInput", new PropertyModel<Collection<FileUpload>>(this, "uploads"), 5));

        reportContainer.add(confirmButton);
        reportContainer.add(messageTextArea);
        reportContainer.add(prioritiesDropDown);
        reportContainer.add(subjectField);
        add(reportContainer);

    }

    @Override
    protected void onSubmit() {
        for (FileUpload fileUpload : uploads) {
            File newFile = new File(getUploadFolder(), fileUpload.getClientFileName());

            // Check new file, delete if it allready existed
            checkFileExists(newFile);

            try {
                newFile.createNewFile();
                fileUpload.writeTo(newFile);
                info("Subor " + fileUpload.getClientFileName() + " ulozeny do: " + newFile.getAbsolutePath());
            } catch (IOException ex) {
                Logger.getLogger(ReportForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ReportForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Collection<FileUpload> getUploads() {
        return uploads;
    }

    private Folder getUploadFolder() {
        return ((WicketApplication) Application.get()).getUploadFolder();
    }

    /**
     * Check whether the file allready exists, and if so, try to delete it.
     *
     * @param file
     */
    private void checkFileExists(File file) {
        if (file.exists()) {

            //Try to delete file
            if (!Files.remove(file)) {
                throw new IllegalStateException("Unable to overwrite " + file.getAbsolutePath());
            }
        }
    }
}
