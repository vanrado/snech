package snech.core.types;

import java.io.Serializable;
import java.sql.Timestamp;
import snech.core.types.enums.EIssueLogType;

/**
 *
 * @author Radovan Račák
 */
public class IssueLog implements Serializable{

    private long logId;
    private long issueId;
    private EIssueLogType logType;
    private String authorLogin;
    private String description;
    private Timestamp createdOn;

    public IssueLog() {
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public String getDescription() {
        return description;
    }

    public long getIssueId() {
        return issueId;
    }

    public long getLogId() {
        return logId;
    }

    public EIssueLogType getLogType() {
        return logType;
    }

    public void setAuthorLogin(String authorLogin) {
        this.authorLogin = authorLogin;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public void setLogType(EIssueLogType logType) {
        this.logType = logType;
    }

}
