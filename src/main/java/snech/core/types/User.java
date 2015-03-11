package snech.core.types;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 *
 * @author Radovan Račák
 */
public class User implements Serializable {

    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String occupation;

    public User() {

    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLogin() {
        return login;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
