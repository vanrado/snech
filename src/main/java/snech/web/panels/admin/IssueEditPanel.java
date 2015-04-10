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
import java.util.Date;
import java.util.List;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
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
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.Issue;
import snech.core.types.enums.EIssuePriority;
import snech.core.types.enums.EIssueStatus;

/**
 *
 * @author vanrado
 */
public class IssueEditPanel extends Panel {

    private Issue issue;
    private String selectedPriority;
    private String selectedStatus;
    private Integer selectedProgress;
    private String estimatedDate;

    private Label issueId;
    private Label subject;
    private Label assignedAdminName;
    private Label progress;
    private TextField estimatedDateField;
    private DropDownChoice prioritiesDropDown;
    private DropDownChoice statusesDropDown;
    private Form<Void> editForm;
    private DropDownChoice progressDropdown;

    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IFormatUtils formatUtils;

    public IssueEditPanel(String id) {
        super(id);
        setOutputMarkupId(true);
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        editForm = new Form("edit.container") {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                issue.setPriority(EIssuePriority.valueOf(selectedPriority));
                issue.setStatus(EIssueStatus.getStatusFromName(selectedStatus));
                issue.setEstimatedDate(null);
                progress.add(new AttributeModifier("style", "width: " + issue.getProgress() + "%"));
                progress.setDefaultModelObject(new PropertyModel<Integer>(issue, "progress").getObject());

                
                if (estimatedDate != null) {
                    Timestamp date = formatUtils.getTimestampFromString(estimatedDate);
                    issue.setEstimatedDate(date);
                    estimatedDateField.setDefaultModelObject(new PropertyModel(this, "estimatedDate"));
                }

                if (databaseService.updateIssue(issue)) {
                    info("Úspešne aktualizované!");
                }
            }
        };
        editForm.setVisible(false);
        editForm.setOutputMarkupId(true);

        issueId = new Label("issueId", "-");
        issueId.setOutputMarkupId(true);
        editForm.add(issueId);

        subject = new Label("issueSubject", "-");
        editForm.add(subject);

        assignedAdminName = new Label("assignedAdminName", "Nepriradené");
        editForm.add(assignedAdminName);

        List<String> prioritiesList = EIssuePriority.getPrioritiesString();
        prioritiesDropDown = new DropDownChoice("priorities", new PropertyModel<String>(this, "selectedPriority"), prioritiesList);
        prioritiesDropDown.setRequired(true);
        editForm.add(prioritiesDropDown);

        List<String> statusList = EIssueStatus.getStatusesList();
        statusesDropDown = new DropDownChoice("statuses", new PropertyModel<String>(this, "selectedStatus"), statusList);
        statusesDropDown.setRequired(true);
        editForm.add(statusesDropDown);
        editForm.add(new AjaxLink("cancel.button") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                editForm.setVisible(false);
                target.add(editForm);
            }
        });

        List<Integer> progressValues = Arrays.asList(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
        progressDropdown = new DropDownChoice("progressSelect", new PropertyModel<Integer>(this, "selectedProgress"), progressValues);
        editForm.add(progressDropdown);
        progress = new Label("progress", " ");
        editForm.add(progress);

        estimatedDateField = new TextField("estimatedDate", new PropertyModel(this, "estimatedDate"));//new DateTextField("estimatedDate", new PropertyModel<Date>(this, "estimatedDate"), new DateConverter);
        editForm.add(estimatedDateField);
        add(editForm);
    }

    public IssueEditPanel(String id, IModel<?> model) {
        super(id, model);
    }

    @Override
    protected void onModelChanged() {
        IModel<Issue> model = (Model) getDefaultModel();
        if (model != null) {
            issue = model.getObject();

            selectedPriority = issue.getPriority().name();
            prioritiesDropDown.setDefaultModel(new PropertyModel<String>(this, "selectedPriority"));
            selectedStatus = issue.getStatus().getName();
            statusesDropDown.setDefaultModel(new PropertyModel<String>(this, "selectedStatus"));

//            estimatedDate = issue.getEstimatedDate()
            issueId.setDefaultModelObject(issue.getId());
            subject.setDefaultModelObject(issue.getSubject());
            assignedAdminName.setDefaultModelObject(issue.getAssignedAdminId() == 0 ? "Nepriradené" : issue.getAssignedAdminId());
            editForm.setVisible(true);

            progressDropdown.setDefaultModel(new PropertyModel<Integer>(issue, "progress"));
            progress.setDefaultModelObject(new PropertyModel<Integer>(issue, "progress").getObject());
            progress.add(new AttributeModifier("style", "width: " + issue.getProgress() + "%"));
        } else {
            // editForm.setVisible(true);
        }
    }
}
