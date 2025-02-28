package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_DISPLAY_STATISTICS_WITHOUT_BUDGET;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_REPEATED_PREFIX_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIRST_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SECOND_START_DATE;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.statistics.StatsCompareCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.budget.BudgetPeriod;
import seedu.address.model.budget.UniqueBudgetList;
import seedu.address.model.expense.Timestamp;

/**
 * Parses input arguments and creates a new StatsCompareCommand object
 */
public class StatsCompareCommandParser implements Parser<StatsCompareCommand> {

    public static final List<Prefix> REQUIRED_PREFIXES = Collections.unmodifiableList(
            List.of(PREFIX_FIRST_START_DATE, PREFIX_SECOND_START_DATE, PREFIX_PERIOD)
    );

    public static final List<Prefix> OPTIONAL_PREFIXES = Collections.unmodifiableList(List.of());

    /**
     * Parses the given {@code String} of arguments in the context of the StatsCompareCommand
     * and returns an StatsCompareCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public StatsCompareCommand parse(String args) throws ParseException {
        if (UniqueBudgetList.staticIsEmpty()) {
            throw new ParseException(MESSAGE_DISPLAY_STATISTICS_WITHOUT_BUDGET);
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_FIRST_START_DATE, PREFIX_SECOND_START_DATE, PREFIX_PERIOD);

        if (!arePrefixesPresent(argMultimap, PREFIX_FIRST_START_DATE, PREFIX_SECOND_START_DATE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatsCompareCommand.MESSAGE_USAGE));
        }

        if (hasRepeatedPrefixes(argMultimap, PREFIX_FIRST_START_DATE, PREFIX_SECOND_START_DATE, PREFIX_PERIOD)) {
            throw new ParseException(MESSAGE_REPEATED_PREFIX_COMMAND);
        }

        BudgetPeriod period;
        if (argMultimap.getValue(PREFIX_PERIOD).isPresent()) {
            period = ParserUtil.parsePeriod(argMultimap.getValue(PREFIX_PERIOD).get());
        } else {
            period = UniqueBudgetList.getPrimaryBudgetPeriod();
        }

        Timestamp startDate1 = ParserUtil.parseTimestamp(argMultimap.getValue(PREFIX_FIRST_START_DATE).get());
        Timestamp startDate2 = ParserUtil.parseTimestamp(argMultimap.getValue(PREFIX_SECOND_START_DATE).get());
        //Period period = ParserUtil.parsePeriod(argMultimap.getValue(PREFIX_PERIOD).get()).getPeriod();

        return new StatsCompareCommand(startDate1, startDate2, period);
    }

    /**
     * Returns true if at least one prefix have more than one usage
     * {@code ArgumentMultiMap}.
     */
    private static boolean hasRepeatedPrefixes(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return !(Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getAllValues(prefix).size() <= 1));
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}


