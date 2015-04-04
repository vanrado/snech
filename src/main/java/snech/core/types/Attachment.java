package snech.core.types;

import java.io.Serializable;

/**
 *
 * @author Radovan Račák
 */
public class Attachment implements Serializable {

    private long id;
    private long issueId;
    private Long messageId;
    private String fileUrl;
    private String fileName;
    private long fileSize;

    public Attachment() {
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public long getId() {
        return id;
    }

    public long getIssueId() {
        return issueId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
