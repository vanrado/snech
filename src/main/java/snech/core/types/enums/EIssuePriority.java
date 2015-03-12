package snech.core.types.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Radovan Račák
 */
public enum EIssuePriority {
    A, B, C, D, E;

    private EIssuePriority() {
    }
    
    public static List<String> getPrioritiesString(){
        List<String> list = new ArrayList<String>();
        EIssuePriority[] priorities = EIssuePriority.values();
        
        for(EIssuePriority priority : priorities){
            list.add(priority.name());
        }
        
        return list;
    }
    
}
