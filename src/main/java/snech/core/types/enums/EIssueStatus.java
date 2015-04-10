package snech.core.types.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Radovan Račák
 */
public enum EIssueStatus {

    NOVA("Nová"), PREBIEHA("Prebieha"), VYMAZANA("Vymazaná"), ZAMIETNUTA("Zamietnutá");

    private String name;

    private EIssueStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<String> getStatusesList() {
        ArrayList<String> list = new ArrayList<>();
        EIssueStatus[] array = EIssueStatus.values();

        for (EIssueStatus status : array) {
            list.add(status.getName());
        }

        return list;
    }

    public static EIssueStatus getStatusFromName(String name) {
        EIssueStatus[] array = EIssueStatus.values();

        for (EIssueStatus status : array) {
            if(status.getName().equalsIgnoreCase(name)){
                return status;
            }
        }
        
        return null;
    }
}
