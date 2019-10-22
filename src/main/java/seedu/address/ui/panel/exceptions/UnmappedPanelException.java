package seedu.address.ui.panel.exceptions;

import static java.util.Objects.requireNonNull;

import seedu.address.ui.panel.PanelName;

public class UnmappedPanelException extends Exception {

    public UnmappedPanelException(PanelName panelName) {
        super(formatMessage(panelName));
    }

    private static String formatMessage(PanelName panelName) {
        requireNonNull(panelName);
        return String.format("%s is not assigned to any panel.", panelName.toString());
    }
}
