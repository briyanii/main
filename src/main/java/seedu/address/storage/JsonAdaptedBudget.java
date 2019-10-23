package seedu.address.storage;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Price;
import seedu.address.model.expense.Timestamp;

/**
 * Jackson-friendly version of {@link Budget}.
 */
class JsonAdaptedBudget {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Budget's %s field is missing!";

    private final String description;
    private final String amount;
    private final String startDate;
    private final String endDate;
    private final String period;
    private final boolean isPrimary;
    private List<JsonAdaptedExpense> expenses = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedBudget} with the given budget details.
     */
    @JsonCreator
    public JsonAdaptedBudget(@JsonProperty("description") String description,
                             @JsonProperty("amount") String amount,
                             @JsonProperty("startDate") String startDate,
                             @JsonProperty("endDate") String endDate,
                             @JsonProperty("period") String period,
                             @JsonProperty("expenses") List<JsonAdaptedExpense> expenses,
                             @JsonProperty("isPrimary") boolean isPrimary) {
        this.description = description;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
        if (expenses != null) {
            this.expenses.addAll(expenses);
        }
        this.isPrimary = isPrimary;
    }

    /**
     * Converts a given {@code Budget} into this class for Jackson use.
     */
    public JsonAdaptedBudget(Budget source) {
        description = source.getDescription().fullDescription;
        amount = source.getAmount().value;
        startDate = source.getStartDate().toString();
        endDate = source.getEndDate().toString();
        period = ParserUtil.formatPeriod(source.getPeriod());
        expenses.addAll(source.getExpenses().stream()
                .map(JsonAdaptedExpense::new)
                .collect(Collectors.toList()));
        isPrimary = source.isPrimary();
    }

    /**
     * Converts this Jackson-friendly adapted budget object into the model's {@code Budget} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted budget.
     */
    public Budget toModelType() throws IllegalValueException {
        final ObservableList<Expense> expenseList = FXCollections.observableArrayList();
        for (JsonAdaptedExpense expense : expenses) {
            expenseList.add(expense.toModelType());
        }

        if (description == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        if (amount == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Price.class.getSimpleName()));
        }
        if (!Price.isValidPrice(amount)) {
            throw new IllegalValueException(Price.MESSAGE_CONSTRAINTS);
        }
        final Price modelAmount = new Price(amount);

        if (startDate == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, LocalDate.class.getSimpleName()));
        }
        //if (!Timestamp.isValidTimestamp(startDate)) {
        //    throw new IllegalValueException(Timestamp.MESSAGE_CONSTRAINTS_DATE);
        //}
        final Timestamp modelStartDate = Timestamp.createTimestampIfValid(startDate).get();

        /*
        if (endDate == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, LocalDate.class.getSimpleName()));
        }
        if (!Timestamp.isValidTimestamp(endDate)) {
            throw new IllegalValueException(Timestamp.MESSAGE_CONSTRAINTS_DATE);
        }
        final LocalDate modelEndDate = ParserUtil.parseDate(endDate);
         */

        if (period == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Period.class.getSimpleName()));
        }
        final Period modelPeriod = ParserUtil.parsePeriod(period);

        Budget budget = new Budget(modelDescription, modelAmount, modelStartDate, modelPeriod, expenseList);

        if (isPrimary) {
            budget.setPrimary();
        }
        return budget;
    }
}
