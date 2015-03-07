package snech.core.types;

import java.io.Serializable;

/**
 *
 * @author Radovan Račák
 */
public class Notice implements Serializable {

    private String heading;
    private String formatedDate;
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

    public String getFormatedDate() {
        return formatedDate;
    }

    public String getHeading() {
        return heading;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

}
