package snech.core.types;

import java.io.Serializable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import snech.core.types.enums.EUserRole;

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
    private String salt;
    private String password;
    private EUserRole userRole;

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

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public EUserRole getUserRole() {
        return userRole;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public void setUserRole(EUserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
