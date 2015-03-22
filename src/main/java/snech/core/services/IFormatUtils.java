package snech.core.services;

import java.sql.Timestamp;
import snech.core.types.User;

/**
 *
 * @author Radovan Račák
 */
public interface IFormatUtils {
    public String getFormatedDate(Timestamp date);
    public String getUserFullName(User user);
}
