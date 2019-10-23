package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_REPEATED_PREFIX_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_TRANSPORT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PRICE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PRICE_DESC_TRANSPORT;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_CLAIMABLE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_DISCOUNTED;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TRANSPORT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRICE_TRANSPORT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.expense.AddCommand;
import seedu.address.model.category.Category;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Price;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    // addCommand should never return a same command as another, so cannot check if the command is same as expected

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing description prefix
        assertParseFailure(parser, VALID_DESCRIPTION_TRANSPORT + PRICE_DESC_TRANSPORT,
                expectedMessage);

        // missing price prefix
        assertParseFailure(parser, DESCRIPTION_DESC_TRANSPORT + VALID_PRICE_TRANSPORT,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_DESCRIPTION_TRANSPORT + VALID_PRICE_TRANSPORT,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid description
        assertParseFailure(parser,
                INVALID_DESCRIPTION_DESC + PRICE_DESC_TRANSPORT
                        + TAG_DESC_DISCOUNTED,
                Description.MESSAGE_CONSTRAINTS);

        // invalid price
        assertParseFailure(parser,
                DESCRIPTION_DESC_TRANSPORT + INVALID_PRICE_DESC
                        + TAG_DESC_DISCOUNTED,
                Price.MESSAGE_CONSTRAINTS);

        // invalid category
        assertParseFailure(parser, DESCRIPTION_DESC_TRANSPORT + PRICE_DESC_TRANSPORT
                + INVALID_TAG_DESC, Category.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_DESCRIPTION_DESC + PRICE_DESC_TRANSPORT + TAG_DESC_DISCOUNTED,
                Description.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser,
                PREAMBLE_NON_EMPTY + DESCRIPTION_DESC_TRANSPORT + PRICE_DESC_TRANSPORT
                        + TAG_DESC_DISCOUNTED,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedPrefix_failure() {
        // multiple categories - not accepted
        assertParseFailure(parser, DESCRIPTION_DESC_TRANSPORT + PRICE_DESC_TRANSPORT + TAG_DESC_CLAIMABLE
                + TAG_DESC_CLAIMABLE, MESSAGE_REPEATED_PREFIX_COMMAND);

        // multiple names - not accepted
        assertParseFailure(parser, DESCRIPTION_DESC_TRANSPORT + DESCRIPTION_DESC_TRANSPORT
                + PRICE_DESC_TRANSPORT + TAG_DESC_CLAIMABLE + TAG_DESC_CLAIMABLE, MESSAGE_REPEATED_PREFIX_COMMAND);
    }

}
