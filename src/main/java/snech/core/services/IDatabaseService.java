package snech.core.services;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import snech.core.types.Issue;
import snech.core.types.Notice;
import snech.core.types.User;

/**
 *
 * @author Radovan Račák
 */
public interface IDatabaseService {
    public User getClient(String id, String password);
    public List<Notice> getNotices(boolean allNotices);
    public List<Issue> getIssues(String userId);
    public Issue getIssue(long issueId);
    public boolean insertIssue(Issue issue);
    public String getAdminFullName(String adminId);
    public String testSelect();
}
