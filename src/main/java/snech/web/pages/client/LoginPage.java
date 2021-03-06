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

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import snech.web.base.BasePage;
import snech.web.forms.LoginForm;

/**
 *
 * @author Radovan
 */
public class LoginPage extends BasePage{
    
    private static final long serialVersionUID = 1L;
    
    public LoginPage(){
        add(new FeedbackPanel("feedback"));
        add(new LoginForm("login.form"));
    }
    
}
