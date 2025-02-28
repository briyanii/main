package seedu.address.logic.commands.expense;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CHICKEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_FOOD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TRANSPORT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_TRANSPORT;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showExpenseAtIndex;
import static seedu.address.testutil.TestUtil.makeModelStack;
import static seedu.address.testutil.TypicalExpenses.getTypicalMooLah;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.expense.EditExpenseCommand.EditExpenseDescriptor;
import seedu.address.logic.commands.general.ClearCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelHistory;
import seedu.address.model.ModelManager;
import seedu.address.model.MooLah;
import seedu.address.model.UserPrefs;
import seedu.address.model.expense.Expense;
import seedu.address.testutil.EditExpenseDescriptorBuilder;
import seedu.address.testutil.ExpenseBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditExpenseCommandTest {

    private Model model = new ModelManager(getTypicalMooLah(), new UserPrefs(), new ModelHistory());

    @Test
    public void run_allFieldsSpecifiedUnfilteredList_success() {
        Expense editedExpense = new ExpenseBuilder().build();
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        EditExpenseCommand editExpenseCommand = new EditExpenseCommand(INDEX_FIRST_EXPENSE, descriptor);

        String expectedMessage = String.format(EditExpenseCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
                new UserPrefs(), new ModelHistory());
        expectedModel.setExpense(model.getFilteredExpenseList().get(0), editedExpense);
        expectedModel.setModelHistory(new ModelHistory("", makeModelStack(model), makeModelStack()));

        assertCommandSuccess(editExpenseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void run_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastExpense = Index.fromOneBased(model.getFilteredExpenseList().size());
        Expense lastExpense = model.getFilteredExpenseList().get(indexLastExpense.getZeroBased());

        ExpenseBuilder expenseInList = new ExpenseBuilder(lastExpense);
        Expense editedExpense = expenseInList
                .withDescription(VALID_DESCRIPTION_TRANSPORT)
                .withPrice(VALID_PRICE_TRANSPORT)
                .withCategory(VALID_CATEGORY_FOOD).build();

        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_TRANSPORT)
                .withPrice(VALID_PRICE_TRANSPORT)
                .withCategory(VALID_CATEGORY_FOOD).build();
        EditExpenseCommand editExpenseCommand = new EditExpenseCommand(indexLastExpense, descriptor);

        String expectedMessage = String.format(EditExpenseCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
                new UserPrefs(), new ModelHistory());
        expectedModel.setExpense(lastExpense, editedExpense);
        expectedModel.setModelHistory(new ModelHistory("", makeModelStack(model), makeModelStack()));

        assertCommandSuccess(editExpenseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void run_noFieldSpecifiedUnfilteredList_success() {
        EditExpenseCommand editExpenseCommand =
                new EditExpenseCommand(INDEX_FIRST_EXPENSE, new EditExpenseDescriptor());
        Expense editedExpense = model.getFilteredExpenseList().get(INDEX_FIRST_EXPENSE.getZeroBased());

        String expectedMessage = String.format(EditExpenseCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
                new UserPrefs(), new ModelHistory());
        expectedModel.commitModel("");

        assertCommandSuccess(editExpenseCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void run_filteredList_success() {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);

        Expense expenseInFilteredList = model
                .getFilteredExpenseList()
                .get(INDEX_FIRST_EXPENSE.getZeroBased());
        Expense editedExpense = new ExpenseBuilder(expenseInFilteredList)
                .withDescription(VALID_DESCRIPTION_TRANSPORT).build();
        EditExpenseCommand editExpenseCommand = new EditExpenseCommand(INDEX_FIRST_EXPENSE,
                new EditExpenseDescriptorBuilder()
                        .withDescription(VALID_DESCRIPTION_TRANSPORT).build());

        String expectedMessage = String.format(EditExpenseCommand.MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense);

        Model expectedModel = new ModelManager(new MooLah(model.getMooLah()),
                new UserPrefs(), new ModelHistory());
        expectedModel.setExpense(model.getFilteredExpenseList().get(0), editedExpense);
        expectedModel.setModelHistory(new ModelHistory("", makeModelStack(model), makeModelStack()));

        assertCommandSuccess(editExpenseCommand, model, expectedMessage, expectedModel);
    }

    // Editing an expense to have the same details as another should not result in failure

    @Test
    public void run_invalidExpenseIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredExpenseList().size() + 1);
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_TRANSPORT).build();
        EditExpenseCommand editExpenseCommand = new EditExpenseCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editExpenseCommand, model, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void run_invalidExpenseIndexFilteredList_failure() {
        showExpenseAtIndex(model, INDEX_FIRST_EXPENSE);
        Index outOfBoundIndex = INDEX_SECOND_EXPENSE;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMooLah().getExpenseList().size());

        EditExpenseCommand editExpenseCommand = new EditExpenseCommand(outOfBoundIndex,
                new EditExpenseDescriptorBuilder().withDescription(VALID_DESCRIPTION_TRANSPORT).build());

        assertCommandFailure(editExpenseCommand, model, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditExpenseCommand standardCommand = new EditExpenseCommand(INDEX_FIRST_EXPENSE, DESC_CHICKEN);

        // same values -> returns true
        EditExpenseDescriptor copyDescriptor = new EditExpenseDescriptor(DESC_CHICKEN);
        EditExpenseCommand commandWithSameValues = new EditExpenseCommand(INDEX_FIRST_EXPENSE, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditExpenseCommand(INDEX_SECOND_EXPENSE, DESC_CHICKEN)));
    }

}
