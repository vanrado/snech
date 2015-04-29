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
package snech.web.pages.admin;

import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.services.IDatabaseService;
import snech.core.types.User;
import snech.core.types.enums.EUserRole;
import snech.web.base.AdminBasePage;

/**
 *
 * @author vanrado
 */
@AuthorizeInstantiation("ADMIN")
public class UserAdministrationPage extends AdminBasePage {

    @SpringBean
    private IDatabaseService databaseService;
    private List<User> users;
    private String inputSearchString;

    public UserAdministrationPage() {
        users = databaseService.getUsers();
        add(new Link("createUser.link") {

            @Override
            public void onClick() {
                setResponsePage(CreateUserPage.class);
            }
        });
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        final WebMarkupContainer usersContainer = new WebMarkupContainer("users.container");
        usersContainer.setOutputMarkupId(true);
        usersContainer.add(new ListView<User>("userRow", users) {

            @Override
            protected void populateItem(ListItem<User> item) {
                final User user = item.getModelObject();
                item.add(new Label("firstName.label", user.getFirstName()));
                item.add(new Label("lastName.label", user.getLastName()));
                item.add(new Label("login.label", user.getLogin()));
                item.add(new Label("userRole.label", user.getUserRole().name()));
                item.add(new Link("userEdit.link") {

                    @Override
                    public void onClick() {
                        PageParameters params = new PageParameters();
                        params.add("login", user.getLogin());
                        setResponsePage(UserEditPage.class, params);
                    }
                });
            }
        });
        add(usersContainer);

        Form searchForm = new Form("search.form");
        TextField inputSearchField = new TextField("input.field", new PropertyModel(this, "inputSearchString"));
        searchForm.add(inputSearchField);
        searchForm.add(new AjaxSubmitLink("searchsubmit.link") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                users.clear();
                if (inputSearchString != null && !inputSearchString.equals("")) {
                    users.addAll(databaseService.getUsersByStringsearch(inputSearchString));
                } else {
                    users.addAll(databaseService.getUsers());
                }
                target.add(usersContainer);
            }

        });
        add(searchForm);

    }

}
