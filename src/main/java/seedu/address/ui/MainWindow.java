package seedu.address.ui;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMESTAMP;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    // Ui parts which the user can switch between
    private ExpenseListPanel expenseListPanel;
    private BudgetListPanel budgetListPanel;
    private BudgetPanel budgetPanel;

    // Ui parts which are always displayed
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane panelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private ButtonBar windowSelectionBar;

    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        expenseListPanel = new ExpenseListPanel(logic.getFilteredExpenseList());
        budgetListPanel = new BudgetListPanel(logic.getFilteredBudgetList());
        budgetPanel = null;
        panelPlaceholder.getChildren().add(expenseListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBox.importSyntaxStyleSheet(getRoot().getScene());

        // add supported commands (not all yet)
        commandBox.enableSyntaxHightlightingForCommand("add",
                List.of(PREFIX_PRICE, PREFIX_DESCRIPTION),
                "add d/<description_here> p/ <price here>");
        commandBox.enableSyntaxHightlightingForCommand("alias",
                Collections.emptyList(),
                "alias <alias_name> <input>");
        commandBox.enableSyntaxHightlightingForCommand("budget",
                List.of(PREFIX_DESCRIPTION, PREFIX_PRICE, PREFIX_START_DATE, PREFIX_PERIOD),
                "budget d/ <description> p/ <amount> sd/ <start_date> pr/ <period>");
        commandBox.enableSyntaxHightlightingForCommand("event",
                List.of(PREFIX_DESCRIPTION, PREFIX_PRICE, PREFIX_CATEGORY, PREFIX_TIMESTAMP),
                "event d/ <description> p/ <amount> date/ <date>");
        commandBox.enableSyntaxHightlightingForCommand("stats",
                List.of(PREFIX_DESCRIPTION, PREFIX_START_DATE, PREFIX_END_DATE),
                "stats sd/ <start_date> ed/ <end_date>");
        commandBox.enableSyntaxHightlightingForCommand("undo",
                Collections.emptyList(),
                "undo");
        commandBox.enableSyntaxHightlightingForCommand("redo",
                Collections.emptyList(),
                "redo");

        commandBox.enableSyntaxHighlighting();

        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Changes the panel displayed in the application's panelPlaceholder to the Budget Panel if
     * the model has a primary budget, returning true if there is a primary budget to display, otherwise
     * return's false.
     *
     * @return true if there is a primary budget to display, false otherwise.
     */
    @FXML
    private boolean viewBudgetPanel() {
        if (logic.getPrimaryBudget() == null) {
            return false;
        }
        budgetPanel = new BudgetPanel(logic.getPrimaryBudget());
        panelPlaceholder.getChildren().set(0, budgetPanel.getRoot());
        return true;
    }

    /**
     * Changes the panel displayed in the application's panelPlaceholder to the Expense List Panel.
     */
    @FXML
    private void viewExpenseListPanel() {
        panelPlaceholder.getChildren().set(0, expenseListPanel.getRoot());
    }

    /**
     * Changes the panel displayed in the application's panelPlaceholder to the Budget List Panel.
     */
    @FXML
    private void viewBudgetListPanel() {
        panelPlaceholder.getChildren().set(0, budgetListPanel.getRoot());
    }

    public ExpenseListPanel getExpenseListPanel() {
        return expenseListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }

    /**
     * Displays Reminders of the user's upcoming Events.
     */
    public void displayReminders() {
        // logger.info("Result: " + commandResult.getFeedbackToUser());
        resultDisplay.setFeedbackToUser(logic.displayReminders());
    }

}
