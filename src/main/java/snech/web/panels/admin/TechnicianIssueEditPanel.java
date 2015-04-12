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
package snech.web.panels.admin;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.Issue;
import snech.core.types.enums.EIssuePriority;
import snech.core.types.enums.EIssueStatus;

/**
 *
 * @author vanrado
 */
public class TechnicianIssueEditPanel extends Panel {

    @SpringBean
    private IFormatUtils formatUtils;

    @SpringBean
    private IDatabaseService databaseService;

    private Form editForm;
    private Issue issue;
    private DropDownChoice prioritiesDropDown;
    private DropDownChoice statusesDropDown;
    private DropDownChoice progressDropdown;
    private TextField estimatedDateField;
    private Label issueIdLabel;
    private Label subject;
    private Label progress;
    private String selectedStatus;
    private String selectedPriority;
    private String estimatedDate;
    private Integer selectedProgress;

    public TechnicianIssueEditPanel(String id) {
        super(id);
        setOutputMarkupId(true);

        FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        editForm = new Form("edit.container") {

            @Override
            protected void onSubmit() {
                issue.setPriority(EIssuePriority.valueOf(selectedPriority));
                issue.setStatus(EIssueStatus.getStatusFromName(selectedStatus));

                if (estimatedDate != null) {
                    Timestamp date = formatUtils.getTimestampFromString(estimatedDate);
                    issue.setEstimatedDate(date);
                }

                if (databaseService.updateIssue(issue, CustomAuthenticatedWebSession.get().getUser().getLogin())) {
                    info("Úspešne aktualizované!");
                }

                estimatedDateField.setDefaultModelObject(estimatedDate);
                progress.add(new AttributeModifier("style", "width: " + issue.getProgress() + "%"));
                progress.setDefaultModelObject(new PropertyModel<Integer>(issue, "progress").getObject());
            }

        };
        editForm.setOutputMarkupId(true);

        issueIdLabel = new Label("issueId", "");
        editForm.add(issueIdLabel);

        subject = new Label("issueSubject", "-");
        editForm.add(subject);

        List<String> statusList = EIssueStatus.getStatusesList();
        statusesDropDown = new DropDownChoice("statuses", new PropertyModel<String>(this, "selectedStatus"), statusList);
        statusesDropDown.setRequired(true);
        editForm.add(statusesDropDown);

        List<String> prioritiesList = EIssuePriority.getPrioritiesString();
        prioritiesDropDown = new DropDownChoice("priorities", new PropertyModel<String>(this, "selectedPriority"), prioritiesList);
        prioritiesDropDown.setRequired(true);
        editForm.add(prioritiesDropDown);

        List<Integer> progressValues = Arrays.asList(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
        progressDropdown = new DropDownChoice("progressSelect", new PropertyModel<Integer>(this, "selectedProgress"), progressValues);
        editForm.add(progressDropdown);
        progress = new Label("progress", " ");
        editForm.add(progress);

        estimatedDate = "";
        estimatedDateField = new TextField("estimatedDate", new PropertyModel(this, "estimatedDate"));
        editForm.add(estimatedDateField);

        editForm.add(new AjaxLink("cancel.button") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                editForm.setVisible(false);
                target.add(editForm);
            }
        });
        editForm.setVisible(false);
        add(editForm);
    }

    @Override
    protected void onModelChanged() {
        IModel<Issue> model = (Model) getDefaultModel();
        if (model != null) {
            issue = model.getObject();
            editForm.setVisible(true);

            selectedPriority = issue.getPriority().name();
            prioritiesDropDown.setDefaultModel(new PropertyModel<String>(this, "selectedPriority"));
            selectedStatus = issue.getStatus().getName();
            statusesDropDown.setDefaultModel(new PropertyModel<String>(this, "selectedStatus"));

            estimatedDate = formatUtils.getFormatedDate(issue.getEstimatedDate());
            estimatedDateField.setDefaultModel(new PropertyModel(this, "estimatedDate"));

            issueIdLabel.setDefaultModelObject(issue.getId());
            subject.setDefaultModelObject(issue.getSubject());

            progressDropdown.setDefaultModel(new PropertyModel<Integer>(issue, "progress"));
            progress.setDefaultModelObject(new PropertyModel<Integer>(issue, "progress").getObject());
            progress.add(new AttributeModifier("style", "width: " + issue.getProgress() + "%"));
        }
    }
}
