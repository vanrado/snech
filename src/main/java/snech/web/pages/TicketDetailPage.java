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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.Issue;
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
    
    private final String UNKNOWN = "Neuvedene";
    
    public TicketDetailPage(PageParameters pageParameters) {
        super(pageParameters);
        Issue issue = databaseService.getIssue(pageParameters.get("id").toString());
        
        add(new Label("issueId", issue.getId() != null ? issue.getId() : UNKNOWN)); 
        add(new Label("issueStatus", issue.getStatus() != null ? issue.getStatus().getName() : UNKNOWN));
        add(new Label("issuePriority", issue.getPriority() != null ? issue.getPriority() : UNKNOWN));
        add(new Label("issueAssigned", databaseService.getAdminFullName(issue.getAssignedAdminId())));
        add(new Label("estimatedDate", issue.getEstimatedDate() != null ? formatUtils.getFormatedDate(issue.getEstimatedDate().getTime()) : UNKNOWN ));
        add(new Label("lastUpdatedDate", issue.getLastUpdatedDate() != null ? formatUtils.getFormatedDate(issue.getLastUpdatedDate().getTime()) : UNKNOWN));
        add(new Link("backpage.link") {

            @Override
            public void onClick() {
                setResponsePage(TicketsListPage.class);
            }

        });
    }

}
