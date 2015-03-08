package snech.web.panels;

import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import snech.core.IDatabaseService;
import snech.core.types.Notice;

/**
 *
 * @author Radovan Račák
 */
public class NoticePanel extends Panel {

    @SpringBean
    private IDatabaseService databaseService;

    public NoticePanel(String id) {
        super(id);
        List<Notice> notices = databaseService.getNotices(true);

        add(new ListView<Notice>("notices", notices) {

            @Override
            protected void populateItem(ListItem<Notice> notice) {
                notice.add(new Label("heading", new PropertyModel(notice.getModel(), "heading")));
                notice.add(new Label("formatedDate", new PropertyModel(notice.getModel(), "formatedDate")));
                notice.add(new Label("author", new PropertyModel(notice.getModel(), "author")));
                notice.add(new Label("content", new PropertyModel(notice.getModel(), "content")));
            }

        });
    }

}
