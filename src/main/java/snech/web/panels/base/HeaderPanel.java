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
package snech.web.panels.base;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import snech.core.CustomAuthenticatedWebSession;
import snech.web.pages.client.AccountManagementPage;
import snech.web.pages.client.LoginPage;
import snech.web.pages.client.OverviewPage;
import snech.web.pages.client.TicketsListPage;

/**
 *
 * @author Radovan
 */
public class HeaderPanel extends Panel {

    public HeaderPanel(String id) {
        super(id);
        final CustomAuthenticatedWebSession mySession = CustomAuthenticatedWebSession.get();
        add(new Link("signOut.link") {

            @Override
            public void onClick() {
                mySession.invalidate();
                setResponsePage(getApplication().getHomePage());
            }

        });

        add(new Label("logedUser", mySession.getUser() != null ? mySession.getUser().getFirstName() + " " + mySession.getUser().getLastName() : ""));
        add(new Link("overview.link") {
            @Override
            public void onClick() {
                setResponsePage(OverviewPage.class);
            }
        });
        
        add(new Link("tickets.link") {
            @Override
            public void onClick() {
                setResponsePage(TicketsListPage.class);
            }
        });        
        
        add(new Link("accountmanagement.link"){

            @Override
            public void onClick() {
                setResponsePage(AccountManagementPage.class);
            }
            
        });
    }

}
