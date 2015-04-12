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
package snech.web.pages;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.types.enums.EUserRole;
import snech.web.base.BasePage;
import snech.web.pages.admin.AdminOverviewPage;
import snech.web.pages.admin.MyTasksPage;
import snech.web.pages.client.TicketDetailPage;
import snech.web.pages.client.TicketsListPage;
import snech.web.panels.admin.AdminHeaderPanel;

/**
 *
 * @author vanrado
 */
public class AppSelectionPage extends BasePage {

    public AppSelectionPage() {
        final CustomAuthenticatedWebSession mySession = CustomAuthenticatedWebSession.get();
        add(new Link("signOut.link") {

            @Override
            public void onClick() {
                mySession.invalidate();
                setResponsePage(getApplication().getHomePage());
            }

        });

        add(new Label("logedUser", mySession.getUser() != null ? mySession.getUser().getFirstName() + " " + mySession.getUser().getLastName() : ""));
        add(new Link("clientApp.link") {

            @Override
            public void onClick() {
                setResponsePage(TicketsListPage.class);
            }
        });

        add(new Link("adminApp.link") {

            @Override
            public void onClick() {
                setResponsePage(MyTasksPage.class);
            }
        });
    }

    @Override
    protected void onConfigure() {
        AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();
        if (!CustomAuthenticatedWebSession.get().isSignedIn()) {
            app.restartResponseAtSignInPage();
        }

        EUserRole userRole = CustomAuthenticatedWebSession.get().getUser().getUserRole();
        if (userRole.equals(EUserRole.UZIVATEL)) {
            setResponsePage(TicketsListPage.class);
        }
    }
}
