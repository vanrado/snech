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
package snech.web.base;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import snech.core.CustomAuthenticatedWebSession;
import snech.web.panels.base.HeaderPanel;

/**
 *
 * @author Radovan
 */
public class MainPage extends BasePage{
    public MainPage(){
        add(new HeaderPanel("header.panel"));
    }

    @Override
    protected void onConfigure() {
        AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();
        if(!CustomAuthenticatedWebSession.get().isSignedIn()){
            app.restartResponseAtSignInPage();
        }
    }
    
    
}
