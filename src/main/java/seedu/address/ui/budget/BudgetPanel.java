package seedu.address.ui.budget;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.budget.Budget;
import seedu.address.ui.expense.ExpenseListPanel;
import seedu.address.ui.UiPart;
import seedu.address.ui.panel.Panel;
import seedu.address.ui.panel.PanelName;

/**
 * Panel containing the list of expenses.
 */
public class BudgetPanel extends Panel {
    private static final String FXML = "BudgetPanel.fxml";
    public static final PanelName PANEL_NAME = new PanelName("Budget Panel");

    private final Logger logger = LogsCenter.getLogger(BudgetPanel.class);

    @FXML
    private StackPane budgetCardPlaceholder;
    @FXML
    private Button previous;
    @FXML
    private Button next;
    @FXML
    private StackPane expenseListPanelPlaceholder;
    private ExpenseListPanel expenseListPanel;

    private Budget budget;
    private BudgetCard budgetCard;

    public BudgetPanel(Budget budget) {
        super(FXML);
        this.budget = budget;
        expenseListPanel = new ExpenseListPanel(this.budget.getExpenses(), false);
        expenseListPanelPlaceholder.getChildren().add(expenseListPanel.getRoot());
        budgetCardPlaceholder.getChildren().add(new BudgetCard(this.budget).getRoot());
    }

    @Override
    public int hashCode() {
        return PANEL_NAME.hashCode();
    }

    @Override
    public void view() {
        getRoot().setVisible(true);
        getRoot().setDisable(false);
    }

    @Override
    public void hide() {
        getRoot().setVisible(false);
        getRoot().setDisable(true);
    }
}
