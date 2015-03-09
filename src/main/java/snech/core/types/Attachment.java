package snech.core.types;

import java.io.Serializable;

/**
 *
 * @author Radovan Račák
 */
public class Attachment implements Serializable {

    private String id;
    private String issueId;
    private String messageId;
    private String fileUrl;
    private String fileName;
    private int fileSize;

    public Attachment() {
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getId() {
        return id;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
