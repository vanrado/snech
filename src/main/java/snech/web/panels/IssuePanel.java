package snech.web.panels;

import org.apache.wicket.markup.html.panel.Panel;
import snech.web.forms.IssuesForm;

/**
 *
 * @author Radovan Račák
 */
public class IssuePanel extends Panel {

    public IssuePanel(String id) {
        super(id);
        add(new IssuesForm("issues.form"));
    }

}
