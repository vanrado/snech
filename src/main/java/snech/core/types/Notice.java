package snech.core.types;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Radovan Račák
 */
public class Notice implements Serializable {

    private String subject;
    private Timestamp formatedDate;
    private String author;
    private String content;

    public Notice() {
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getFormatedDate() {
        return formatedDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFormatedDate(Timestamp formatedDate) {
        this.formatedDate = formatedDate;
    }

    public void setSubject(String heading) {
        this.subject = heading;
    }

}
