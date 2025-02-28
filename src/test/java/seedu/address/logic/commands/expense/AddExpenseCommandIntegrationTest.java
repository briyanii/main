package seedu.address.logic.commands.expense;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalExpenses.getTypicalMooLah;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelHistory;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.expense.Expense;

/**
 * Contains integration tests (interaction with the Model) for {@code AddExpenseCommand}.
 */
public class AddExpenseCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalMooLah(), new UserPrefs(), new ModelHistory());
    }

    // No addCommand should not be the same as another addCommand, odds are unlikely because of use of UUID

    @Test
    public void run_duplicateExpense_throwsCommandException() {
        Expense expenseInList = model.getMooLah().getExpenseList().get(0);
        assertCommandFailure(new AddExpenseCommand(expenseInList), model, AddExpenseCommand.MESSAGE_DUPLICATE_EXPENSE);
    }

}
