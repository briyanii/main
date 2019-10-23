package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRICE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.expense.EditCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.expense.DescriptionContainsKeywordsPredicate;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Timestamp;
import seedu.address.testutil.EditExpenseDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_DESCRIPTION_CHICKEN = "Chicken Rice";
    public static final String VALID_DESCRIPTION_TRANSPORT = "Taxi to work";
    public static final String VALID_PRICE_CHICKEN = "11111.11";
    public static final String VALID_PRICE_TRANSPORT = "222,22222";
    public static final String VALID_TAG_CLAIMABLE = "toClaimFromWork";
    public static final String VALID_TAG_DISCOUNTED = "usedCouponCode";
    public static final String VALID_UNIQUE_IDENTIFIER = "Expense@12341234-1234-1234-1234-123412341234";

    public static final String VALID_DESCRIPTION_SCHOOL = "school related expenses";
    public static final String VALID_AMOUNT_SCHOOL = "300";
    public static final String VALID_START_DATE_SCHOOL = "01-10-2019";
    public static final String VALID_PERIOD_SCHOOL = "month";

    public static final String DESCRIPTION_DESC_CHICKEN = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_CHICKEN;
    public static final String DESCRIPTION_DESC_TRANSPORT = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_TRANSPORT;
    public static final String PRICE_DESC_CHICKEN = " " + PREFIX_PRICE + VALID_PRICE_CHICKEN;
    public static final String PRICE_DESC_TRANSPORT = " " + PREFIX_PRICE + VALID_PRICE_TRANSPORT;
    public static final String TAG_DESC_CLAIMABLE = " " + PREFIX_CATEGORY + VALID_TAG_CLAIMABLE;
    public static final String TAG_DESC_DISCOUNTED = " " + PREFIX_CATEGORY + VALID_TAG_DISCOUNTED;

    public static final String DESCRIPTION_DESC_SCHOOL = " " + PREFIX_DESCRIPTION + VALID_DESCRIPTION_SCHOOL;
    public static final String AMOUNT_DESC_SCHOOL = " " + PREFIX_PRICE + VALID_AMOUNT_SCHOOL;
    public static final String START_DATE_DESC_SCHOOL = " " + PREFIX_START_DATE + VALID_START_DATE_SCHOOL;
    public static final String PERIOD_DESC_SCHOOL = " " + PREFIX_PERIOD + VALID_PERIOD_SCHOOL;

    // '&' not allowed in descriptions
    public static final String INVALID_DESCRIPTION_DESC = " " + PREFIX_DESCRIPTION + "James&";
    // 'a' not allowed in prices
    public static final String INVALID_PRICE_DESC = " " + PREFIX_PRICE + "911a";
    // '*' not allowed in tags
    public static final String INVALID_TAG_DESC = " " + PREFIX_CATEGORY + "hubby*";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditExpenseDescriptor DESC_CHICKEN;

    public static final String STATS_WITHOUT_TAG = " 5";
    public static final String STATS_PREFIX_WITHOUT_INPUT = String.format(" %s ", PREFIX_START_DATE);
    public static final String STATS_INVALID_PREFIX = String.format(" %s ", PREFIX_CATEGORY);
    public static final String STATS_HIGHER_END_DATE = String.format(" %s31-10-2019 %s01-10-2019",
            PREFIX_START_DATE, PREFIX_END_DATE);
    public static final String STATS_DUPLICATE_TAG = String.format("%s31-10-2019 %s01-10-2019",
            PREFIX_START_DATE, PREFIX_START_DATE);

    public static final Timestamp OCTOBER_FIRST = Timestamp.createTimestampIfValid("01-10-2019").get();
    public static final Timestamp OCTOBER_LAST = Timestamp.createTimestampIfValid("31-10-2019").get();

    static {
        DESC_CHICKEN = new EditExpenseDescriptorBuilder().withDescription(VALID_DESCRIPTION_CHICKEN)
                .withPrice(VALID_PRICE_CHICKEN)
                .withCategory(VALID_TAG_DISCOUNTED).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.run(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered expense list and selected expense in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Expense> expectedFilteredList = new ArrayList<>(actualModel.getFilteredExpenseList());

        assertThrows(CommandException.class, expectedMessage, () -> command.run(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredExpenseList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the expense at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showExpenseAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredExpenseList().size());

        Expense expense = model.getFilteredExpenseList().get(targetIndex.getZeroBased());
        final String[] splitDescription = expense.getDescription().fullDescription.split("\\s+");
        model.updateFilteredExpenseList(new DescriptionContainsKeywordsPredicate(Arrays.asList(splitDescription[0])));

        assertEquals(1, model.getFilteredExpenseList().size());
    }

}
