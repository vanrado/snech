package snech.core.types;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import snech.core.types.enums.EIssuePriority;
import snech.core.types.enums.EIssueStatus;

/**
 *
 * @author Radovan Račák
 */
public class Issue implements Serializable {

    private long id;
    private String subject;
    private EIssueStatus status;
    private long assignedAdminId;
    private EIssuePriority priority;
    private Timestamp estimatedDate;
    private Timestamp lastUpdatedDate;
    private Timestamp createdDate;
    private String message;
    private String replyFromAdmin;
    private ArrayList<Attachment> attachments;
    private String userLogin;

    public Issue() {
        attachments = new ArrayList<>();
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getReplyFromAdmin() {
        return replyFromAdmin;
    }

    public String getMessage() {
        return message;
    }

    public long getAssignedAdminId() {
        return assignedAdminId;
    }

    public Timestamp getEstimatedDate() {
        return estimatedDate;
    }

    public long getId() {
        return id;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public EIssuePriority getPriority() {
        return priority;
    }

    public EIssueStatus getStatus() {
        return status;
    }

    public String getSubject() {
        return subject;
    }

    public void setAssignedAdminId(long assignedAdminId) {
        this.assignedAdminId = assignedAdminId;
    }

    public void setEstimatedDate(Timestamp estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void setLastUpdatedDate(Timestamp lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void setPriority(EIssuePriority priority) {
        this.priority = priority;
    }

    public void setStatus(EIssueStatus status) {
        this.status = status;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReplyFromAdmin(String replyFromAdmin) {
        this.replyFromAdmin = replyFromAdmin;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
