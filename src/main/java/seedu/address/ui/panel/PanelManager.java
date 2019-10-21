package seedu.address.ui.panel;

public interface PanelManager {

    boolean setPanel(PanelName panelName, Panel panel);

    boolean hasPanel(PanelName panelName);

    Panel getPanel(PanelName panelName);
}
