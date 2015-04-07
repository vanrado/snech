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

import snech.web.base.MainPage;
import snech.web.panels.OverviewPanel;

/**
 *
 * @author Radovan
 */
public class OverviewPage extends MainPage {

    public OverviewPage() {
        add(new OverviewPanel("overview.panel"));
    }
    
}
