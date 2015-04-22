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
package snech.web.pages.client;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.types.User;
import snech.web.base.MemberBasePage;

/**
 *
 * @author Radovan
 */
public class AccountManagementPage extends MemberBasePage {
    
    public AccountManagementPage(){
        User user = CustomAuthenticatedWebSession.get().getUser();
        add(new Label("firstName.label", user.getFirstName()));
        add(new Label("lastName.label", user.getLastName()));
        add(new Label("email.label", user.getEmail()));
        add(new Label("occupation.label", user.getOccupation()));
        
        add(new Link("changedetails.link"){

            @Override
            public void onClick() {
                setResponsePage(ChangeDetailsPage.class);
            }
        
        });
        
        add(new Link("changepass.link"){

            @Override
            public void onClick() {
                setResponsePage(ChangePasswordPage.class);
            }
            
        });
    }
}
