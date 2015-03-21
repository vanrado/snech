package snech.web.panels;

import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.CustomAuthenticatedWebSession;
import snech.core.services.IDatabaseService;
import snech.core.services.IFormatUtils;
import snech.core.types.IssueLog;
import snech.core.types.User;

/**
 *
 * @author Radovan Račák
 */
public class OverviewPanel extends Panel {

    @SpringBean
    private IDatabaseService databaseService;

    @SpringBean
    private IFormatUtils formatUtils;

    private List<IssueLog> issueLogs;

    public OverviewPanel(String id) {
        super(id);
        final User logedUser = CustomAuthenticatedWebSession.get().getUser();
        issueLogs = databaseService.getIssueLogs(logedUser.getLogin());
        System.out.println(issueLogs.toArray().toString());
        ListView<IssueLog> logList = new ListView<IssueLog>("log", issueLogs) {

            @Override
            protected void populateItem(ListItem<IssueLog> item) {
                IssueLog log = item.getModelObject();
                item.add(new Label("author", log.getAuthorLogin()));
                item.add(new Label("formatedDate", formatUtils.getFormatedDate(log.getCreatedOn().getTime())));
                // TODO v contente sprava zalezi od 
                item.add(new Label("content", log.getLogType().name() + " " + log.getDescription()));
            }
        };
        
        add(logList);

    }

}
