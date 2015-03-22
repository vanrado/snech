package snech.core.services;

import java.sql.Timestamp;
import org.springframework.stereotype.Service;
import snech.core.types.User;

/**
 *
 * @author Radovan Račák
 */
@Service
public class FormatUtilsImpl implements IFormatUtils {

    /**
     * Vrati formatovany datum
     *
     * @param timestampDate timestamp format datumu - t.j cas v ms od 1.1.1970
     * @return
     */
    @Override
    public String getFormatedDate(Timestamp date) {
        String timeString = "-";
        
        if(date != null){
            System.out.println(date.getTime());
            Timestamp timestamp = new Timestamp(date.getTime());
            timeString = timestamp.toString();
        }
        
        return timeString;

    }

    @Override
    public String getUserFullName(User user) {
        String fullname = "-";
        
        if(user != null){
            fullname = user.getFirstName() + " " + user.getLastName();
        }
        
        return fullname;
    }
}
