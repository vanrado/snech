/*
 * Copyright 2015 vanrado.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package snech.web.forms;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import snech.WicketApplication;
import snech.core.services.IDatabaseService;
import snech.core.services.IHashUtils;
import snech.core.types.Attachment;
import snech.core.types.Issue;
import snech.core.types.enums.EIssuePriority;
import snech.web.pages.TicketDetailPage;
import snech.web.pages.TicketEditPage;

/**
 *
 * @author vanrado
 */
public class TicketEditForm extends Form<Object> {

    @SuppressWarnings("FieldMayBeFinal")
    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IHashUtils hashUtils;

    private String subject;
    private String selectedPriority;
    private String message;
    private Issue issue;
    private final String UNKNOWN = "-";
    private List<Attachment> attachmentsToDelete;
    private List<Attachment> attachments;
    private Collection<FileUpload> uploads;
    private MultiFileUploadField uploadField;

    public TicketEditForm(String id) {
        super(id);
    }

    public TicketEditForm(String id, final PageParameters pageParameters) {
        super(id);
        uploads = new ArrayList<>();
        attachmentsToDelete = new ArrayList<>();
        issue = databaseService.getIssue(Long.parseLong(pageParameters.get("id").toString()));
        attachments = attachments == null ? new ArrayList<Attachment>() : databaseService.getAttachments(issue.getId(), -1);
        subject = issue.getSubject();
        selectedPriority = issue.getPriority().name();
        message = issue.getMessage();

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        add(new Label("issueId", issue.getId()));
        TextField<String> subjectField = new TextField<>("issueSubject", new PropertyModel<String>(this, "subject"));
        add(subjectField);

        List<String> prioritiesList = EIssuePriority.getPrioritiesString();
        final DropDownChoice prioritiesDropDown = new DropDownChoice("priorities", new PropertyModel<String>(this, "selectedPriority"), prioritiesList);
        prioritiesDropDown.setRequired(true);
        add(prioritiesDropDown);

        final TextArea messageTextArea = new TextArea("message", new PropertyModel(this, "message"));
        messageTextArea.setRequired(true);
        add(messageTextArea);

        setMultiPart(true);
        uploadField = new MultiFileUploadField("fileInput", new PropertyModel<Collection<FileUpload>>(this, "uploads"), 5);

        add(uploadField);

        add(new Button("save.button"));

        add(new Link("cancel.button") {

            @Override
            public void onClick() {
                setResponsePage(TicketDetailPage.class, pageParameters);
            }

        });

        ListView<Attachment> attachmentsView;

        add(new ListView<Attachment>("attachments", attachments) {

            @Override
            protected void populateItem(ListItem<Attachment> listItem) {
                try {
                    final Attachment attachment = listItem.getModelObject();
                    File file = new File(attachment.getFileUrl() != null ? URLDecoder.decode(attachment.getFileUrl(), "UTF-8") : UNKNOWN);
                    DownloadLink downloadLink = new DownloadLink("attLink", file, URLDecoder.decode(attachment.getFileName(), "UTF-8"));
                    downloadLink.add(new Label("fileName", attachment.getFileName()));
                    listItem.add(downloadLink);

                    AjaxLink removeLink = new AjaxLink("remove.link") {

                        @Override
                        public void onClick(AjaxRequestTarget target) {

                            if (attachmentsToDelete.contains(attachment)) {
                                attachmentsToDelete.remove(attachment);
                            } else {
                                attachmentsToDelete.add(attachment);
                            }
                            target.add(feedback);
                        }
                    };
                    listItem.add(removeLink);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(TicketDetailPage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    protected void onSubmit() {
        super.onSubmit();
        issue.setSubject(subject);
        issue.setPriority(EIssuePriority.valueOf(selectedPriority));
        issue.setMessage(message);
        databaseService.updateIssue(issue);

        removeAttachments();
        uploadAttachments();

        if (attachments != null) {
            attachments.clear();
            attachments.addAll(databaseService.getAttachments(issue.getId(), -1));
        }
        attachmentsToDelete.clear();
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

    private void uploadAttachments() {
        Folder newFolder;
        try {
            //Vytvorit Folder pre tento upload - current value of seq
            newFolder = new Folder(getUploadFolder(), databaseService.getUploadsCount() + "-" + hashUtils.randomStringGenerator(5));
            newFolder.mkdirs();

            //Doneho zapisem subory
            for (FileUpload fileUpload : uploads) {
                String fileName = fileUpload.getClientFileName();
                File newFile = new File(newFolder, fileName);

                // Skontroluj ci subor uz neexistuje, ak ano vymaz ho a nahrad
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

                Attachment attachment = new Attachment();
                String fileNameEncoded = URLEncoder.encode(fileName, "UTF-8");
                attachment.setFileName(fileNameEncoded);
                //attachment.setFileSize(newFile.get);
                String url = newFile.getAbsolutePath();
                String encodedUrl = URLEncoder.encode(url, "UTF-8");
                attachment.setFileUrl(encodedUrl);
                attachment.setIssueId(issue.getId());
                attachment.setMessageId(null);

                try {
                    boolean success = databaseService.insertAttachment(attachment);
                } catch (Exception ex) {
                    Logger.getLogger(ReportForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception ex) {
            error("Chyba pri uploade suboru!");
        }
    }

    private void removeAttachments() {
        for (Attachment attachment : attachmentsToDelete) {
            if (databaseService.removeAttachment(attachment.getId())) {
                info("Uspesne vymazana priloha #" + attachment.getId());

            } else {
                error("Chyba pri mazani prilohy #" + attachment.getId());
            }

        }
    }

    public Collection<FileUpload> getUploads() {
        return uploads;
    }
}
