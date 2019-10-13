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
import seedu.address.model.expense.Expense;

/**
 * Panel containing the list of expenses.
 */
public class ExpenseListPanel extends UiPart<Region> {
    private static final String FXML = "ListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ExpenseListPanel.class);

    @FXML
    private StackPane titlePlaceHolder;
    @FXML
    private ListView<Expense> listView;

    public ExpenseListPanel(ObservableList<Expense> expenseList, boolean withTitle) {
        super(FXML);

        titlePlaceHolder.setMinHeight(0);
        if (withTitle) {
            titlePlaceHolder.getChildren().add(new Label("Expense List"));
        }
        listView.setItems(expenseList);
        listView.setCellFactory(listView -> new ExpenseListViewCell());

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
