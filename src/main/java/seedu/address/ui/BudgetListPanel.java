package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.budget.Budget;

import java.util.logging.Logger;

/**
 * Panel containing the list of expenses.
 */
public class BudgetListPanel extends UiPart<Region> {
    private static final String FXML = "BudgetListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(BudgetListPanel.class);

    @FXML
    private ListView<Budget> budgetListView;

    public BudgetListPanel(ObservableList<Budget> budgetList) {
        super(FXML);

        budgetListView.setItems(budgetList);
        budgetListView.setCellFactory(listView -> new BudgetListViewCell());

    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Expense} using a {@code ExpenseCard}.
     */
    class BudgetListViewCell extends ListCell<Budget> {
        @Override
        protected void updateItem(Budget budget, boolean empty) {
            super.updateItem(budget, empty);

            if (empty || budget == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new BudgetCard(budget).getRoot());
            }
        }
    }

}
