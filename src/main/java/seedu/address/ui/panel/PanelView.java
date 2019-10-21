package seedu.address.ui.panel;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.ui.UiPart;

public class PanelView extends UiPart<Region> implements PanelManager {

    private Map<PanelName, Panel> panelNamePanelHashMap;
    private Panel currentPanel;

    @FXML
    private StackPane panelPlaceholder;

    private static final String FXML = "PanelView.fxml";

    public PanelView() {
        super(FXML);
        panelNamePanelHashMap = new HashMap<>();
        currentPanel = null;
    }


    @Override
    public boolean setPanel(PanelName panelName, Panel panel) {
        panelNamePanelHashMap.put(panelName, panel);
//        panelPlaceholder.getChildren().add(panel.getRoot());
        panelPlaceholder.getChildren().add(panelPlaceholder.getChildren().size(), panel.getRoot());
        return panelPlaceholder.getChildren().contains(panel.getRoot());
    }

    @Override
    public boolean hasPanel(PanelName panelName) {
        return panelNamePanelHashMap.containsKey(panelName) && panelNamePanelHashMap.get(panelName) != null;
    }

    @Override
    public Panel getPanel(PanelName panelName) {
        return panelNamePanelHashMap.get(panelName);
    }

    public void viewPanel(PanelName panelName) {
        if (panelName.equals(PanelName.CURRENT)) {
            if (currentPanel != null) {
                return;
            }
        } else if (!hasPanel(panelName)) {
            return;
        }
        for (Panel p : panelNamePanelHashMap.values()) {
            p.hide();
        }
        panelNamePanelHashMap.get(panelName).view();
        currentPanel = getPanel(panelName);
    }
}
