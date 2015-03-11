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
import snech.web.pages.AccountManagementPage;
import snech.web.pages.LoginPage;
import snech.web.pages.OverviewPage;
import snech.web.pages.TicketsListPage;

/**
 *
 * @author Radovan
 */
public class HeaderPanel extends Panel {

    public HeaderPanel(String id) {
        super(id);
        CustomAuthenticatedWebSession MySession = CustomAuthenticatedWebSession.get();
        add(new Link("signOut.link") {

            @Override
            public void onClick() {
                CustomAuthenticatedWebSession.get().invalidate();
                setResponsePage(getApplication().getHomePage());
            }

        });

        add(new Label("logedUser", MySession.getUser() != null ? MySession.getUser().getFirstName() + " " + MySession.getUser().getLastName() : ""));
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
