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
package snech.web.pages;

import org.apache.wicket.markup.html.link.Link;
import snech.web.base.BasePage;

/**
 *
 * @author Radovan
 */
public class ResetVerificationPage extends BasePage{
    public ResetVerificationPage(){
        
        add(new Link("changepass.link"){

            @Override
            public void onClick() {
                setResponsePage(ChangePasswordPage.class);
            }
        
        });
        
        add(new Link("resetpassword.link"){

            @Override
            public void onClick() {
                setResponsePage(ResetPasswordPage.class);
            }
        
        });
    }
}
