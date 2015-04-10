package snech.core.services;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            Timestamp timestamp = new Timestamp(date.getTime());
            timeString = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
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
    
    public Timestamp getTimestampFromString(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        Timestamp timestamp = null;
        
        try {
            Date parsedDate = dateFormat.parse(date);
            timestamp = new Timestamp(parsedDate.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(FormatUtilsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return timestamp;        
    }
}
