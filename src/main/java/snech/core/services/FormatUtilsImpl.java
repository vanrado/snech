package snech.core.services;

import java.sql.Timestamp;
import org.springframework.stereotype.Service;

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
    public String getFormatedDate(long timestampDate) {
        Timestamp timestamp = new Timestamp(timestampDate);
        String timeString = timestamp.toString();
        return timeString;

    }

}
