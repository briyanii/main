package seedu.address.ui;

import java.util.logging.Logger;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.expense.Event;

/**
 * Controller for a help page
 */
public class TranspiredEventsWindow extends UiPart<Stage> {

    public static final String MESSAGE =
            "This event should have already happened! Do you want to add it as an expense?\n%s";
    public static final String ERROR_MESSAGE =
            "There was an error adding the specified expense!";
    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "TranspiredEventsWindow.fxml";
    private Event currentEvent;
    private Logic logic;

    @FXML
    private Button yesButton;

    @FXML
    private Button noButton;

    @FXML
    private Label message;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public TranspiredEventsWindow(Stage root, Logic logic) {
        super(FXML, root);
        this.logic = logic;
        root.sizeToScene();
    }

    /**
     * Creates a new HelpWindow.
     */
    public TranspiredEventsWindow(Logic logic) {
        this(new Stage(), logic);
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     * <ul>
     *     <li>
     *         if this method is called on a thread other than the JavaFX Application Thread.
     *     </li>
     *     <li>
     *         if this method is called during animation or layout processing.
     *     </li>
     *     <li>
     *         if this method is called on the primary stage.
     *     </li>
     *     <li>
     *         if {@code dialogStage} is already showing.
     *     </li>
     * </ul>
     */
    public void show(Event event) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                logger.fine("Notifying users of transpired events.");
                currentEvent = event;
                message.setText(
                        String.format(MESSAGE, event.toString()));
                getRoot().show();
                getRoot().centerOnScreen();
            }
        });

    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Adds the corresponding Expense from the transpired Event. Only triggers if the user presses 'Yes'.
     */
    @FXML
    private void addExpenseFromEvent() {
        try {
            logic.addExpenseFromEvent(currentEvent);
            getRoot().close();
        } catch (CommandException | ParseException e) {
            message.setText(ERROR_MESSAGE);
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> getRoot().close());
            delay.play();
        }
    }

    @FXML
    private void ignore() {
        getRoot().close();
    }
}
