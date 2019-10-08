package seedu.address.ui;

import java.math.BigInteger;
import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.expense.Expense;

/**
 * Panel containing the list of expenses.
 */
public class ExpenseListPanel extends UiPart<Region> {
    private static final String FXML = "ExpenseListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ExpenseListPanel.class);

    @FXML
    private ListView<Expense> expenseListView;
    @FXML
    private Label budgetName;
    @FXML
    private Label budgetPeriod;
    @FXML
    private Label budgetStatusText;
    @FXML
    private ProgressBar budgetStatusBar;
    @FXML
    private Button previous;
    @FXML
    private Button next;


    public ExpenseListPanel(ObservableList<Expense> expenseList) {
        super(FXML);

        //placeholder budget
        //BigInteger budget = BigInteger.valueOf(10000000);

        expenseListView.setItems(expenseList);
        expenseListView.setCellFactory(listView -> new ExpenseListViewCell());

//        BigInteger b = BigInteger.valueOf(0);
//        for (Expense e : expenseListView.getItems()) {
//            b = b.add(BigInteger.valueOf(Long.parseLong(e.getPrice().value)));
//        }
//

        // button should set predicate of model's filtered expense list to different date range
        // as well as update the buttons



//        budgetStatusText.setText(b.toString() + " / " + budget.toString());
//        budgetStatusBar.setProgress(b.divide(budget).doubleValue());

//        if (budgetStatusBar.getProgress() > 1) {
//            budgetStatusBar.setStyle("-fx-accent: red");
//        } else {
//            budgetStatusBar.setStyle("-fx-accent: green");
//        }
//
//        expenseList.addListener(new ListChangeListener<Expense>() {
//            @Override
//            public void onChanged(Change<? extends Expense> change) {
//                BigInteger b = BigInteger.ZERO;
//                for (Expense e : expenseListView.getItems()) {
//                    b = b.add(BigInteger.valueOf(Long.parseLong(e.getPrice().value)));
//                }
//                budgetStatusText.setText(b.toString() + " / " + budget.toString());
//                budgetStatusBar.setProgress(b.divide(budget).doubleValue());
//                if (budgetStatusBar.getProgress() > 1) {
//                    budgetStatusBar.setStyle("-fx-accent: red");
//                } else {
//                    budgetStatusBar.setStyle("-fx-accent: green");
//                }
//
//            }
//        });

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
