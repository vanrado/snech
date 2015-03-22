package snech.core.types.enums;

/**
 *
 * @author Radovan Račák
 */
public enum EIssueLogType {
    VYTVORENIE("vytvoril"), ZMAZANIE("zmazal"), INE("iné");
    
    private String activity;
    
    private EIssueLogType(String activity) {
        this.activity = activity;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
