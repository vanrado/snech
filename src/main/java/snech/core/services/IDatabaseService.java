package snech.core.services;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import snech.core.types.Issue;
import snech.core.types.Notice;
import snech.core.types.User;
import snech.core.types.enums.EIssueLogType;

/**
 *
 * @author Radovan Račák
 */
public interface IDatabaseService {

    public User getClient(String id, String password);

    public List<Notice> getNotices(boolean allNotices);

    public List<Issue> getIssues(String userId);

    public boolean removeIssue(long id);

    public Issue getIssue(long issueId);

    public long insertIssue(Issue issue);

    public String getAdminFullName(String adminId);

    public boolean insertIssueLog(long issueId, EIssueLogType logType, String author, String description);

    public String testSelect();
}
