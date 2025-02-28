package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.budget.DeleteBudgetByNameCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.expense.Description;

/**
 * Parses input arguments and creates a new DeleteBudgetByNameCommand object
 */
public class DeleteBudgetByNameCommandParser {
    public static final List<Prefix> REQUIRED_PREFIXES = Collections.unmodifiableList(List.of(PREFIX_DESCRIPTION));
    public static final List<Prefix> OPTIONAL_PREFIXES = Collections.unmodifiableList(List.of());

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteBudgetByNameCommand
     * and returns an DeleteBudgetByNameCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteBudgetByNameCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DESCRIPTION);

        if (!arePrefixesPresent(argMultimap, PREFIX_DESCRIPTION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteBudgetByNameCommand.MESSAGE_USAGE));
        }

        Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get());

        return new DeleteBudgetByNameCommand(description);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
