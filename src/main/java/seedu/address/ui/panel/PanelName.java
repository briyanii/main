package seedu.address.ui.panel;

public class PanelName {
//    public static PanelName ALIASES_PANEL = new PanelName("AliasPanel");
//    public static PanelName BUDGET_LIST_PANEL = new PanelName("Budgets");
//    public static PanelName BUDGET_PANEL = new PanelName("Budget Panel");
    public static PanelName CURRENT = new PanelName("Current");
//    public static PanelName EVENTS_PANEL = new PanelName("Events");
//    public static PanelName EXPENSE_LIST_PANEL = new PanelName("Expenses");
//    public static PanelName STATISTICS_PANEL = new PanelName("Statistics");

    private final String panelName;

    public PanelName(String panelName) {
        this.panelName = panelName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PanelName)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        PanelName otherPanelName = (PanelName) obj;
        return otherPanelName.panelName.equals(panelName);
    }

    @Override
    public int hashCode() {
        return panelName.hashCode();
    }

    @Override
    public String toString() {
        return "PANEL NAME: " + panelName;
    }
}