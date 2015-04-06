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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.services.IDatabaseService;
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
    private String subject;
    private String selectedPriority;
    private String message;
    private Issue issue;
    private final String UNKNOWN = "-";
    private List<Long> attachmentsToDelete;
    private List<Attachment> attachments;

    public TicketEditForm(String id) {
        super(id);
    }

    public TicketEditForm(String id, final PageParameters pageParameters) {
        super(id);
        attachmentsToDelete = new ArrayList<>();
        issue = databaseService.getIssue(Long.parseLong(pageParameters.get("id").toString()));
        attachments = databaseService.getAttachments(issue.getId(), -1);
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

        add(new Button("save.button") {

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
                for (Long attachmentId : attachmentsToDelete) {
                    long id = Long.valueOf(attachmentId);
                    System.out.println(id);
                    if (databaseService.removeAttachment(id)) {
                        info("Uspesne vymazana priloha #" + attachmentId);
                    } else {
                        error("Chyba pri mazani prilohy #" + attachmentId);
                    }

                }
                info("Uspesne aktualizovane, na vymazanie " + attachmentsToDelete.size());
                attachmentsToDelete.clear();
//                List<Attachment> newAttachments = databaseService.getAttachments(issue.getId(), -1);
//                replaceContainer(newAttachments);

            }

            @Override
            public void onAfterSubmit() {
                setResponsePage(TicketEditPage.class, pageParameters);
            }

        });
        
        add(new Link("cancel.button") {

            @Override
            public void onClick() {
                setResponsePage(TicketDetailPage.class, pageParameters);
            }

        });
        
        if (attachments != null) {
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

                                if (attachmentsToDelete.contains(attachment.getId())) {
                                    attachmentsToDelete.remove(attachment.getId());
                                } else {
                                    attachmentsToDelete.add(attachment.getId());
                                }
                                info("dlzka pola na vymazanie priloh: " + attachmentsToDelete.toArray().length);
                                target.add(feedback);
                            }
                        };
                        listItem.add(removeLink);
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(TicketDetailPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } else {
            //Component pridany, ale nieje nutne zobrazovat nejake prilohy
            MarkupContainer attachmentContainer = new MarkupContainer("attachments") {
                @Override
                protected void onInitialize() {
                    super.onInitialize();
                    add(new Link("attLink") {

                        @Override
                        protected void onInitialize() {
                            super.onInitialize();
                            add(new Label("fileName", UNKNOWN));
                        }

                        @Override
                        public void onClick() {

                        }
                    }.setVisible(false));
                }

            };
            attachmentContainer.add(new Link("remove.link") {

                @Override
                public void onClick() {
                }
            }.setVisible(false));
            
            add(attachmentContainer);
        }
    }
}
