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
    private ListView<Expense> expenseListView;

    private Budget budget;
    private BudgetCard budgetCard;

    public BudgetPanel(Budget budget) {
        super(FXML);
        this.budget = budget;

        expenseListView.setItems(this.budget.getExpenses());
        expenseListView.setCellFactory(listView -> new BudgetPanel.ExpenseListViewCell());
        budgetCardPlaceholder.getChildren().add(new BudgetCard(this.budget).getRoot());

    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Expense} using a {@code ExpenseCard}.
     */
    class ExpenseListViewCell extends ListCell<Expense> {
        @Override
        protected void updateItem(Expense expense, boolean empty) {
            super.updateItem(expense, empty);

            if (empty || expense == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ExpenseCard(expense, getIndex() + 1).getRoot());
            }
        }
    }

}
