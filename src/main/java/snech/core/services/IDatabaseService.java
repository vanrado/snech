package snech.core.services;

import java.util.List;
import snech.core.types.Issue;
import snech.core.types.IssueLog;
import snech.core.types.Notice;
import snech.core.types.User;
import snech.core.types.enums.EIssueLogType;
import snech.core.types.enums.EIssueStatus;

/**
 *
 * @author Radovan Račák
 */
public interface IDatabaseService {

    public User getClient(String id, String password);

    public List<Notice> getNotices(boolean allNotices);

    /**
     * 
     * @param userId
     * @param deleted ak true vrati aj poziadavky so statusom VYMAZANA
     * @return 
     */
    public List<Issue> getIssues(String userId, boolean deleted);

    public boolean updateIssue(Issue issue);
    public boolean removeIssue(long id);

    public Issue getIssue(long issueId);

    /**
     * 
     * @param issue predstavuje vkladany ticket.
     * @return vrati issueId vygenerovane pre vlozeny ticket v databaze.
     */
    public long insertIssue(Issue issue);

    public User getUser(long userId);
    
    public boolean updateUser(User user);

    public boolean insertIssueLog(long issueId, EIssueLogType logType, String author, String description);

    public List<IssueLog> getIssueLogs(String userLogin);
    
    public boolean setIssueStatus(EIssueStatus status, long id, String author);
    
    public String testSelect();
}
