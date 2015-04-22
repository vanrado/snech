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
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
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

    public UserAdministrationPage() {
        users = databaseService.getUsers();
        add(new Link("createUser.link") {

            @Override
            public void onClick() {
                setResponsePage(CreateUserPage.class);
            }
        });
        add(new ListView<User>("userRow", users) {

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
    }

}
