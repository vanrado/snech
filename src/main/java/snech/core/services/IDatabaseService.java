package snech.core.services;

import java.util.ArrayList;
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

    public User getUser(long userId);

    public boolean updateLoginPassword(String login, String newPassword, String newSalt);

    public boolean updateUser(User user);

    public User getUserLogin(String login);

    public String getLoginSalt(String userLogin);

    public List<Notice> getNotices(boolean allNotices);

    public List<Issue> getIssues(String userId, boolean deleted);

    public boolean updateIssue(Issue issue, String login);

    public boolean removeIssue(long id);

    public Issue getIssue(long issueId);

    public long insertIssue(Issue issue);

    public boolean insertIssueLog(long issueId, EIssueLogType logType, String author, String description);

    public List<IssueLog> getIssueLogs(String userLogin);

    public boolean setIssueStatus(EIssueStatus status, long id, String author);

    public long getUploadsCount();

    public void incrementUploads();

    public boolean insertAttachment(Attachment attachment);

    public List<Attachment> getAttachments(long issueId);

    public boolean removeAttachment(long attachmentId);
    
    public List<User> getTechnicians();
    
    public boolean assignIssueToTechnician(long issueId, String technicianLogin);
    
    public List<User> getAssignedTechnicians(long issueId);
    
    public List<Issue> getAssignedIssues(String login);
    
    public boolean deleteAssignedTechnicians(long issueId, String technicianLogin);

    public String testSelect();
}
