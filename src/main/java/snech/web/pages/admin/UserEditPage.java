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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IHashUtils;
import snech.core.types.User;
import snech.web.base.AdminBasePage;
import snech.web.pages.client.AccountManagementPage;
import snech.web.validators.PasswordValidator;

/**
 *
 * @author vanrado
 */
public class UserEditPage extends AdminBasePage {

    private User user;
    private boolean changePassword = false;
    private String password;
    private String passwordRepeat;
    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IHashUtils hashUtils;

    public UserEditPage(PageParameters params) {
        user = databaseService.getUserLogin(params.get("login").toString());
        if (user == null) {
            user = new User();
        }
        final Form editForm = new Form("edit.form");
        editForm.setOutputMarkupId(true);
        editForm.add(new FeedbackPanel("feedback"));

        TextField firstNameField = new TextField("firstName.textfield", new PropertyModel(user, "firstName"));
        firstNameField.setRequired(true);
        editForm.add(firstNameField);

        TextField lastNameField = new TextField("lastName.textfield", new PropertyModel(user, "lastName"));
        lastNameField.setRequired(true);
        editForm.add(lastNameField);

        TextField emailField = new TextField("login.textfield", new PropertyModel(user, "login"));
        editForm.add(emailField);

        editForm.add(new Link("backpage.button") {

            @Override
            public void onClick() {
                setResponsePage(AccountManagementPage.class);
            }

        });

        editForm.add(new Button("save.button") {

            @Override
            public void onError() {
                super.onError();
                error("Niekde nastala chyba!");
            }

            @Override
            public void onSubmit() {
                if (databaseService.updateUser(user)) {
                    info("Aktualizacia uzivatela prebehla uspesne!");
                } else {
                    error("Nastala chyba pri aktualizacii databazy!");
                }

                if (password != null) {
                    if (password.equals(passwordRepeat)) {
                        String[] newPassword = hashUtils.createNewPassword(password);
                        if (databaseService.updateLoginPassword(user.getLogin(), newPassword[1], newPassword[0])) {
                            info("Aktualizácia hesla prebehla úspešne!");
                        } else {
                            error("Aktualizácia hesla neprebehla úspešne!");
                        }
                    } else {
                        error("Heslá sa nezhodujú!");
                    }
                }
            }
        });

        final PasswordTextField password = new PasswordTextField("password.field", new PropertyModel(this, "password"));
        password.setEnabled(false);
        password.add(new PasswordValidator());
        editForm.add(password);

        final PasswordTextField passwordRepeat = new PasswordTextField("passwordRepeat.field", new PropertyModel(this, "passwordRepeat"));
        passwordRepeat.setEnabled(false);
        editForm.add(passwordRepeat);

        AjaxCheckBox changePasswordCheckbox = new AjaxCheckBox("changePassword.checkbox", new PropertyModel(this, "changePassword")) {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (changePassword) {
                    password.setEnabled(true);
                    passwordRepeat.setEnabled(true);
                } else {
                    password.setEnabled(false);
                    passwordRepeat.setEnabled(false);
                }

                target.add(editForm);
            }
        };

        editForm.add(changePasswordCheckbox);
        add(editForm);
    }

}
