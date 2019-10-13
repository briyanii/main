package seedu.address.ui;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Expense;

import java.time.format.DateTimeFormatter;

/**
 * An UI component that displays information of a {@code Budget}.
 */
public class BudgetCard extends UiPart<Region> {

    private static final String FXML = "BudgetCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    @FXML
    private AnchorPane cardPane;
    @FXML
    private Label budgetName;
    @FXML
    private Label budgetStart;
    @FXML
    private Label budgetEnd;
    @FXML
    private Label budgetTotalAmount;
    @FXML
    private Label budgetAllocatedAmount;
    @FXML
    private ProgressBar budgetProgressBar;

    private Budget budget;

    private static final String CURRENCY_SYMBOL = "$";

    public BudgetCard(Budget budget) {
        super(FXML);

        this.budget = budget;

        // budget name
        budgetName.setText(budget.getDescription().fullDescription);

        // budget period
        budgetStart.setText(budget.getStartDate().format(DateTimeFormatter.BASIC_ISO_DATE));
        budgetEnd.setText(budget.getEndDate().format(DateTimeFormatter.BASIC_ISO_DATE));

        // budget total over allocated
        double total = 0.0;
        for (Expense e : budget.getExpenses()) {
            total += e.getPrice().getAsDouble();
        }
        budgetTotalAmount.setText(String.format("%s%f",CURRENCY_SYMBOL,total));
        double allocated = Double.parseDouble(budget.getAmount().toString());
        budgetAllocatedAmount.setText(String.format("%s%f",CURRENCY_SYMBOL,allocated));

        // progress bar
        budgetProgressBar.setProgress(total/allocated);
        // progress bar colour
        if(budget.isExceeded()) {
            budgetProgressBar.setStyle("-progress-bar-colour: -progress-bar-overbudget;");
        } else {
            budgetProgressBar.setStyle("-progress-bar-colour: -progress-bar-inbudget;");
        }

        budget.getExpenses().addListener(new ListChangeListener<Expense>() {
            @Override
            public void onChanged(Change<? extends Expense> change) {
                change.next();
                if (change.wasAdded()) {
                    // update total
                    System.out.println("expense added");
                    double total = 0.0;
                    for (Expense e : budget.getExpenses()) {
                        total += e.getPrice().getAsDouble();
                    }
                    budgetTotalAmount.setText(String.format("%s%f",CURRENCY_SYMBOL,total));
                    budgetProgressBar.setProgress(total/allocated);

                }
                if (change.wasRemoved()) {
                    // update total
                    System.out.println("expense deleted");
                    double total = 0.0;
                    for (Expense e : budget.getExpenses()) {
                        total += e.getPrice().getAsDouble();
                    }
                    budgetTotalAmount.setText(String.format("%s%f",CURRENCY_SYMBOL,total));
                    budgetProgressBar.setProgress(total/allocated);

                }
                if (change.wasUpdated()) {
                    // update total
                    System.out.println("expense updated");
                    double total = 0.0;
                    for (Expense e : budget.getExpenses()) {
                        total += e.getPrice().getAsDouble();
                    }
                    budgetTotalAmount.setText(String.format("%s%f",CURRENCY_SYMBOL,total));
                    budgetProgressBar.setProgress(total/allocated);

                }
                if (change.wasReplaced()) {
                    // update total
                    System.out.println("expense replaced");
                    double total = 0.0;
                    for (Expense e : budget.getExpenses()) {
                        total += e.getPrice().getAsDouble();
                    }
                    budgetTotalAmount.setText(String.format("%s%f",CURRENCY_SYMBOL,total));
                    budgetProgressBar.setProgress(total/allocated);
                }

                if(budget.isExceeded()) {
                    budgetProgressBar.setStyle("-progress-bar-colour: -progress-bar-overbudget;");
                } else {
                    budgetProgressBar.setStyle("-progress-bar-colour: -progress-bar-inbudget;");
                }

            }
        });

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof BudgetCard)) {
            return false;
        }

        // state check
        BudgetCard card = (BudgetCard) other;
        return budgetName.equals(card.budgetName)
                && budgetStart.equals(card.budgetStart)
                && budgetEnd.equals(card.budgetEnd)
                && budget.equals(card.budget);
    }
}
