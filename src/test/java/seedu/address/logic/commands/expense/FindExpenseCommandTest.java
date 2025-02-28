package seedu.address.logic.commands.expense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalExpenses.CHICKEN_RICE;
import static seedu.address.testutil.TypicalExpenses.ENTERTAINMENT;
import static seedu.address.testutil.TypicalExpenses.FASHION;
import static seedu.address.testutil.TypicalExpenses.getTypicalMooLah;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelHistory;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.expense.DescriptionContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindExpenseCommandTest {
    private Model model = new ModelManager(getTypicalMooLah(), new UserPrefs(), new ModelHistory());
    private Model expectedModel = new ModelManager(getTypicalMooLah(), new UserPrefs(), new ModelHistory());

    @Test
    public void equals() {
        DescriptionContainsKeywordsPredicate firstPredicate =
                new DescriptionContainsKeywordsPredicate(Collections.singletonList("first"));
        DescriptionContainsKeywordsPredicate secondPredicate =
                new DescriptionContainsKeywordsPredicate(Collections.singletonList("second"));

        FindExpenseCommand findFirstCommand = new FindExpenseCommand(firstPredicate);
        FindExpenseCommand findSecondCommand = new FindExpenseCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindExpenseCommand findFirstCommandCopy = new FindExpenseCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different expense -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void run_zeroKeywords_noExpenseFound() {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 0);
        DescriptionContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindExpenseCommand command = new FindExpenseCommand(predicate);
        expectedModel.commitModel("");
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredExpenseList());
    }

    @Test
    public void run_multipleKeywords_multipleExpensesFound() {
        String expectedMessage = String.format(MESSAGE_EXPENSES_LISTED_OVERVIEW, 3);
        DescriptionContainsKeywordsPredicate predicate = preparePredicate("the Chicken Movie");
        FindExpenseCommand command = new FindExpenseCommand(predicate);
        expectedModel.commitModel("");
        expectedModel.updateFilteredExpenseList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CHICKEN_RICE, ENTERTAINMENT, FASHION), model.getFilteredExpenseList());
    }

    /**
     * Parses {@code userInput} into a {@code DescriptionContainsKeywordsPredicate}.
     */
    private DescriptionContainsKeywordsPredicate preparePredicate(String userInput) {
        return new DescriptionContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
