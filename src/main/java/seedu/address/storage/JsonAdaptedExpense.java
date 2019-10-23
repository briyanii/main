package seedu.address.storage;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.category.Category;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Price;
import seedu.address.model.expense.Timestamp;
import seedu.address.model.expense.UniqueIdentifier;


/**
 * Jackson-friendly version of {@link Expense}.
 */
class JsonAdaptedExpense {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Expense's %s field is missing!";

    private final String description;
    private final String price;
    private final String uniqueIdentifier;
    private final String category;
    private final String rawTimestamp;

    /**
     * Constructs a {@code JsonAdaptedExpense} with the given expense details.
     */
    @JsonCreator
    public JsonAdaptedExpense(@JsonProperty("description") String description,
                              @JsonProperty("price") String price,
                              @JsonProperty("category") String category,
                              @JsonProperty("timestamp") String rawTimestamp,
                              @JsonProperty("uniqueIdentifier") String uniqueIdentifier) {
        this.description = description;
        this.price = price;
        this.rawTimestamp = rawTimestamp;
        this.uniqueIdentifier = uniqueIdentifier;
        this.category = category;
    }

    /**
     * Converts a given {@code Expense} into this class for Jackson use.
     */
    public JsonAdaptedExpense(Expense source) {
        this.description = source.getDescription().fullDescription;
        this.price = source.getPrice().value;
        this.category = source.getCategory().getCategoryName();
        this.uniqueIdentifier = source.getUniqueIdentifier().value;
        this.rawTimestamp = source.getTimestamp().toString();
    }

    /**
     * Converts this Jackson-friendly adapted expense object into the model's {@code Expense} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted expense.
     */
    public Expense toModelType() throws IllegalValueException {

        if (category == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Category.class.getSimpleName()));
        }

        Category modelCategory = new Category(category);

        if (description == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        if (price == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Price.class.getSimpleName()));
        }
        if (!Price.isValidPrice(price)) {
            throw new IllegalValueException(Price.MESSAGE_CONSTRAINTS);
        }
        final Price modelPrice = new Price(price);


        if (uniqueIdentifier == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, UniqueIdentifier.class.getSimpleName()));
        }
        if (!UniqueIdentifier.isValidUniqueIdentifier(uniqueIdentifier)) {
            throw new IllegalValueException(UniqueIdentifier.MESSAGE_CONSTRAINTS);
        }
        final UniqueIdentifier modelUniqueIdentifier = new UniqueIdentifier(uniqueIdentifier);

        if (rawTimestamp == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Timestamp.class.getSimpleName()));
        }

        Optional<Timestamp> potentialTimestamp = Timestamp.createTimestampIfValid(rawTimestamp);
        if (potentialTimestamp.isEmpty()) {
            throw new IllegalValueException(Timestamp.MESSAGE_CONSTRAINTS_DATE);
        }
        final Timestamp modelTimestamp = potentialTimestamp.get();

        return new Expense(modelDescription, modelPrice, modelCategory, modelTimestamp, modelUniqueIdentifier);
    }

}
