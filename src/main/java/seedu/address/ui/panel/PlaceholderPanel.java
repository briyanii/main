package seedu.address.ui.panel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Placeholder Panel for to represent null Panel instances.
 */
public class PlaceholderPanel extends Panel {

    @FXML
    private StackPane panelPlaceholder;

    private static final String PLACEHOLDER_PANEL_MESSAGE = "This panel is empty!";

    public PlaceholderPanel() {
        super(SinglePanelView.FXML);
    }

    @Override
    public void view() {
        panelPlaceholder.getChildren().add(new Label("This is not the Panel you are looking for."));
        getRoot().setVisible(true);
        getRoot().setDisable(false);
    }

    @Override
    public void hide() {
        panelPlaceholder.getChildren().clear();
        getRoot().setVisible(false);
        getRoot().setDisable(true);
    }
}
