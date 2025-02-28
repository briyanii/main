package seedu.address.logic.commands.statistics;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIRST_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SECOND_START_DATE;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandGroup;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.budget.BudgetPeriod;
import seedu.address.model.expense.Timestamp;
import seedu.address.ui.StatsPanel;

/**
 * Calculates comparison statistics for MooLah. Creates a table that shows
 * the similarity of expenses in both periods and the difference of expenses in both periods
 */
public class StatsCompareCommand extends Command {
    public static final String COMMAND_WORD = "statscompare" + CommandGroup.GENERAL;

    public static final String MESSAGE_SUCCESS = "Statistics Comparison Calculated!";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Compare statistics between two time periods\n"
            + "Parameters: "
            + "[" + PREFIX_FIRST_START_DATE + "START_DATE] "
            + "[" + PREFIX_SECOND_START_DATE + "END_DATE] "
            + PREFIX_PERIOD + "PERIOD\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FIRST_START_DATE + "11-11-1111 "
            + PREFIX_SECOND_START_DATE + "12-12-1212 "
            + PREFIX_PERIOD + "month";

    private final Timestamp firstStartDate;

    private final Timestamp secondStartDate;

    private final BudgetPeriod period;

    public StatsCompareCommand(Timestamp date1, Timestamp date2, BudgetPeriod period) {
        requireNonNull(date1);
        requireNonNull(date2);
        requireNonNull(period);

        this.firstStartDate = date1;
        this.secondStartDate = date2;
        this.period = period;
    }

    @Override
    protected void validate(Model model) {
        requireNonNull(model);
    }


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.calculateStatistics(COMMAND_WORD, firstStartDate , secondStartDate, period, false);
        return new CommandResult(MESSAGE_SUCCESS, false, false, StatsPanel.PANEL_NAME);
    }

    @Override
    public boolean equals(Object other) {
        return other == this //short circuit if same object
                || (other instanceof StatsCompareCommand // instance of handles nulls
                && firstStartDate.equals(((StatsCompareCommand) other).firstStartDate)
                && secondStartDate.equals(((StatsCompareCommand) other).secondStartDate)
                && period.equals(((StatsCompareCommand) other).period));
    }
}







