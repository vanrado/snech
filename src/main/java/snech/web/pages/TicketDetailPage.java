/*
 * Copyright 2014 Radovan.
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
package snech.web.pages;

import java.io.File;
import java.util.List;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.Attachment;
import snech.core.types.Issue;
import snech.core.types.User;
import snech.core.types.enums.EIssueStatus;
import snech.web.base.MainPage;

/**
 *
 * @author Radovan
 */
public class TicketDetailPage extends MainPage {

    private String id;

    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IFormatUtils formatUtils;

    private final String UNKNOWN = "-";

    public TicketDetailPage(PageParameters pageParameters) {
        super(pageParameters);
        final Issue issue = databaseService.getIssue(Long.parseLong(pageParameters.get("id").toString()));
        User assignedAdmin = databaseService.getUser(issue.getAssignedAdminId());

        add(new Label("issueId", issue.getId() != 0 ? issue.getId() : UNKNOWN));
        add(new Label("issueSubject", issue.getSubject()));
        add(new Label("issueStatus", issue.getStatus() != null ? issue.getStatus().getName() : UNKNOWN));
        add(new Label("issuePriority", issue.getPriority() != null ? issue.getPriority() : UNKNOWN));
        add(new Label("issueAssigned", formatUtils.getUserFullName(assignedAdmin)));
        add(new Label("estimatedDate", formatUtils.getFormatedDate(issue.getEstimatedDate())));
        add(new Label("lastUpdatedDate", formatUtils.getFormatedDate(issue.getLastUpdatedDate())));
        add(new Label("createdOnDate", formatUtils.getFormatedDate(issue.getCreatedDate())));
        add(new MultiLineLabel("message", issue.getMessage() != null ? issue.getMessage() : UNKNOWN));
        add(new MultiLineLabel("replyFromAdmin", issue.getReplyFromAdmin() != null ? issue.getReplyFromAdmin() : UNKNOWN));
        List<Attachment> attachments = issue.getAttachments();
        if (!attachments.isEmpty()) {
            add(new ListView<Attachment>("attachments", attachments) {

                @Override
                protected void populateItem(ListItem<Attachment> listItem) {
                    final Attachment attachment = listItem.getModelObject();
                    File file = new File(attachment.getFileUrl() != null ? attachment.getFileUrl() : UNKNOWN);
                    add(new DownloadLink("attLink", file, attachment.getFileName() != null ? attachment.getFileName() : UNKNOWN));
                }
            });
        } else {
            //Component pridany, ale nieje nutne zobrazovat nejake prilohy
            MarkupContainer linkContainer = new MarkupContainer("attachments") {
                @Override
                protected void onInitialize() {
                    super.onInitialize();
                    add(new Link("attLink") {

                        @Override
                        public void onClick() {

                        }
                    });
                }

            };
            add(linkContainer);
        }

        add(new Link("backpage.link") {

            @Override
            public void onClick() {
                setResponsePage(TicketsListPage.class);
            }

        });

        add(new Link("editTicket.link") {

            @Override
            public void onClick() {
                PageParameters params = new PageParameters();
                params.add("id", issue.getId());
                setResponsePage(TicketEditPage.class, params);
            }

        });

        add(new Link("submit.button") {

            @Override
            public void onClick() {
                if (issue.getStatus().equals(EIssueStatus.NOVA)) {
                    final User logedUser = CustomAuthenticatedWebSession.get().getUser();
                    databaseService.setIssueStatus(EIssueStatus.VYMAZANA, issue.getId(), logedUser.getLogin());
                    setResponsePage(TicketsListPage.class);
                } else {
                    info("Poziadavku #" + issue.getId() + " nieje mozne vymazat! Uz nema status Nova!");
                }
            }

        });
        add(new Label("issuesForDelete", "#" + issue.getId()));
    }

}
