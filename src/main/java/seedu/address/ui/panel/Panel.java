package seedu.address.ui.panel;

import javafx.scene.layout.Region;
import seedu.address.ui.UiPart;

/**
 * Represents a viewable panel in the GUI.
 */
public abstract class Panel extends UiPart<Region> {

    public Panel(String FXML) {
        super(FXML);
    }

    public abstract int hashCode();

    public abstract void view();

    public abstract void hide();
}
