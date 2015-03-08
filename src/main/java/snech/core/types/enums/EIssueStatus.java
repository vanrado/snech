package snech.core.types.enums;

/**
 *
 * @author Radovan Račák
 */
public enum EIssueStatus {

    NOVA("Nová"), PREBIEHA("Prebieha");

    String name;

    private EIssueStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
