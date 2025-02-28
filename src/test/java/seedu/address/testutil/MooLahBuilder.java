package seedu.address.testutil;

import seedu.address.model.MooLah;
import seedu.address.model.expense.Expense;

/**
 * A utility class to help with building MooLah objects.
 * Example usage: <br>
 *     {@code MooLah ab = new MooLahBuilder().withExpense("John", "Doe").build();}
 */
public class MooLahBuilder {

    private MooLah mooLah;

    public MooLahBuilder() {
        mooLah = new MooLah();
    }

    public MooLahBuilder(MooLah mooLah) {
        this.mooLah = mooLah;
    }

    /**
     * Adds a new {@code Expense} to the {@code MooLah} that we are building.
     */
    public MooLahBuilder withExpense(Expense expense) {
        mooLah.addExpense(expense);
        return this;
    }

    public MooLah build() {
        return mooLah;
    }
}
