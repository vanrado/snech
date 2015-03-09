package snech.core.types;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import snech.core.types.enums.EIssuePriority;
import snech.core.types.enums.EIssueStatus;

/**
 *
 * @author Radovan Račák
 */
public class Issue implements Serializable {

    private Long id;
    private String subject;
    private EIssueStatus status;
    private String assignedAdminId;
    private EIssuePriority priority;
    private Timestamp estimatedDate;
    private Timestamp lastUpdatedDate;
    private String message;
    private String replyFromAdmin;
    private ArrayList<Attachment> attachments;

    public Issue() {
        attachments = new ArrayList<>();
    }

    public String getReplyFromAdmin() {
        return replyFromAdmin;
    }

    public String getMessage() {
        return message;
    }

    public String getAssignedAdminId() {
        return assignedAdminId;
    }

    public Timestamp getEstimatedDate() {
        return estimatedDate;
    }

    public Long getId() {
        return id;
    }

    public Timestamp getLastUpdatedDate() {
        return lastUpdatedDate;
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

    public void setAssignedAdminId(String assignedAdminId) {
        this.assignedAdminId = assignedAdminId;
    }

    public void setEstimatedDate(Timestamp estimatedDate) {
        this.estimatedDate = estimatedDate;
    }

    public void setId(long id) {
        this.id = id;
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
}
