package snech.core.types;

import java.io.Serializable;

/**
 *
 * @author Radovan Račák
 */
public class User implements Serializable{
    private String id;
    private String password;
    private String fullName;
    
    public User() {
    
    }

    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
