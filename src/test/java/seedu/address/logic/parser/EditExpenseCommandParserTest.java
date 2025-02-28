package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_CHICKEN;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_CHICKEN;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_TRANSPORT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_CLAIMABLE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_DISCOUNTED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_FOOD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_CHICKEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_CHICKEN;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_TRANSPORT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_EXPENSE;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_EXPENSE;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.expense.EditExpenseCommand;
import seedu.address.model.category.Category;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Price;
import seedu.address.testutil.EditExpenseDescriptorBuilder;

public class EditExpenseCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_CATEGORY;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditExpenseCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_DESCRIPTION_CHICKEN, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditExpenseCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + DESCRIPTION_DESC_CHICKEN, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + DESCRIPTION_DESC_CHICKEN, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(
                parser,
                "1" + INVALID_DESCRIPTION_DESC, Description.MESSAGE_CONSTRAINTS); // invalid description
        assertParseFailure(parser, "1" + INVALID_PRICE_DESC, Price.MESSAGE_CONSTRAINTS); // invalid price
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Category.MESSAGE_CONSTRAINTS); // invalid category

        // invalid price followed by valid email
        assertParseFailure(
                parser, "1" + INVALID_PRICE_DESC + VALID_DESCRIPTION_CHICKEN, Price.MESSAGE_CONSTRAINTS);

        // valid price followed by invalid price. The test case for invalid price followed by valid price
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser,
                "1" + PRICE_DESC_TRANSPORT + INVALID_PRICE_DESC, Price.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_TAG} alone will reset the tags of the {@code Expense} being edited,
        // parsing it together with a valid category results in error
        assertParseFailure(
                parser,
                "1" + TAG_DESC_CLAIMABLE + TAG_DESC_DISCOUNTED + TAG_EMPTY, Category.MESSAGE_CONSTRAINTS);
        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(
                parser,
                "1" + INVALID_DESCRIPTION_DESC + INVALID_PRICE_DESC
                        + INVALID_DESCRIPTION_DESC + VALID_PRICE_CHICKEN,
                Description.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_EXPENSE;
        String userInput = targetIndex.getOneBased() + PRICE_DESC_TRANSPORT
                + DESCRIPTION_DESC_CHICKEN + TAG_DESC_CLAIMABLE;

        EditExpenseCommand.EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_CHICKEN)
                .withPrice(VALID_PRICE_TRANSPORT)
                .withCategory(VALID_CATEGORY_FOOD).build();

        EditExpenseCommand expectedCommand = new EditExpenseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_EXPENSE;
        String userInput = targetIndex.getOneBased() + PRICE_DESC_TRANSPORT;

        EditExpenseCommand.EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withPrice(VALID_PRICE_TRANSPORT).build();
        EditExpenseCommand expectedCommand = new EditExpenseCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // description
        Index targetIndex = INDEX_THIRD_EXPENSE;
        String userInput = targetIndex.getOneBased() + DESCRIPTION_DESC_CHICKEN;
        EditExpenseCommand.EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withDescription(VALID_DESCRIPTION_CHICKEN).build();
        EditExpenseCommand expectedCommand = new EditExpenseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // price
        userInput = targetIndex.getOneBased() + PRICE_DESC_CHICKEN;
        descriptor = new EditExpenseDescriptorBuilder().withPrice(VALID_PRICE_CHICKEN).build();
        expectedCommand = new EditExpenseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + TAG_DESC_CLAIMABLE;
        descriptor = new EditExpenseDescriptorBuilder().withCategory(VALID_CATEGORY_FOOD).build();
        expectedCommand = new EditExpenseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_EXPENSE;
        String userInput = targetIndex.getOneBased() + INVALID_PRICE_DESC + PRICE_DESC_TRANSPORT;
        EditExpenseCommand.EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder()
                .withPrice(VALID_PRICE_TRANSPORT).build();
        EditExpenseCommand expectedCommand = new EditExpenseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + INVALID_PRICE_DESC + PRICE_DESC_TRANSPORT;
        descriptor = new EditExpenseDescriptorBuilder().withPrice(VALID_PRICE_TRANSPORT).build();
        expectedCommand = new EditExpenseCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
