package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.budget.Budget;

/**
 * Panel containing the list of budgets.
 */
public class BudgetListPanel extends UiPart<Region> {
    private static final String FXML = "ListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(BudgetListPanel.class);

    @FXML
    private StackPane titlePlaceHolder;
    @FXML
    private ListView<Budget> listView;

    public BudgetListPanel(ObservableList<Budget> budgetList) {
        super(FXML);

        titlePlaceHolder.getChildren().add(new Label("Budget List"));
        listView.setItems(budgetList);
        listView.setCellFactory(listView -> new BudgetListViewCell());

    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Budget} using a {@code BudgetCard}.
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
