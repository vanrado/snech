package snech.core.services;

import java.util.List;
import snech.core.types.Attachment;
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

    /*
     Users
     */
    public User getUser(long userId);

    public boolean updateLoginPassword(String login, String newPassword, String newSalt);

    public boolean updateUser(User user);

    public User getUserLogin(String login);

    public String getLoginSalt(String userLogin);
    /*
     /Users
     */

    /*
     Notices
     */
    public List<Notice> getNotices(boolean allNotices);
    /*
     /Notices
     */

    /*
     Issues
     */
    /**
     * @param userId
     * @param deleted ak true vrati aj poziadavky so statusom VYMAZANA
     * @return
     */
    public List<Issue> getIssues(String userId, boolean deleted);

    public boolean updateIssue(Issue issue);

    public boolean removeIssue(long id);

    public Issue getIssue(long issueId);

    /**
     * @param issue predstavuje vkladany ticket.
     * @return vrati issueId vygenerovane pre vlozeny ticket v databaze.
     */
    public long insertIssue(Issue issue);
    /*
     /Issues
     */

    public boolean insertIssueLog(long issueId, EIssueLogType logType, String author, String description);

    public List<IssueLog> getIssueLogs(String userLogin);

    public boolean setIssueStatus(EIssueStatus status, long id, String author);

    /*
     Uploads
     */
    public long getUploadsCount();

    public void incrementUploads();
    /*
     /Uploads
     */

    public boolean insertAttachment(Attachment attachment);

    public List<Attachment> getAttachments(long issueId, long messageId);

    public String testSelect();
}
