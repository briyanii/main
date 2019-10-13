package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Expense;

import java.util.logging.Logger;

/**
 * Panel containing the list of expenses.
 */
public class BudgetPanel extends UiPart<Region> {
    private static final String FXML = "BudgetPanel.fxml";
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
        expenseListPanel = new ExpenseListPanel(this.budget.getExpenses());
        expenseListPanelPlaceholder.getChildren().add(expenseListPanel.getRoot());
        budgetCardPlaceholder.getChildren().add(new BudgetCard(this.budget).getRoot());
    }

}
