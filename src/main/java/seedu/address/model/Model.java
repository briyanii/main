package seedu.address.model;

import java.nio.file.Path;
import java.time.Period;
import java.util.Optional;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.Alias;
import seedu.address.commons.core.AliasMappings;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Event;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Timestamp;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Expense> PREDICATE_SHOW_ALL_EXPENSES = unused -> true;

    /** {@code Predicate} that always evaluate to true */
    Predicate<Event> PREDICATE_SHOW_ALL_EVENTS = unused -> true;

    Predicate<Budget> PREDICATE_SHOW_ALL_BUDGETS = unused -> true;

    /**
     * Resets data to the given model.
     */
    void resetData(Model model);

    // ======== MODEL HISTORY ===============

    /**
     * Returns the model history.
     */
    ReadOnlyModelHistory getModelHistory();

    /**
     * Replaces model history with the data in {@code history}.
     */
    void setModelHistory(ReadOnlyModelHistory history);

    /**
     * Adds the current model to the history.
     */
    void addToHistory();

    /**
     * Adds a to the past history.
     */
    void addToPastHistory(Model model);

    /**
     * Adds a to the future history.
     */
    void addToFutureHistory(Model model);

    /**
     * Checks whether model can be rolled-back.
     */
    boolean canRollback();

    /**
     * Returns the rolled-back version of the model to the immediate previous state.
     */
    Optional<Model> rollbackModel();

    /**
     * Checks whether model can be migrated.
     */
    boolean canMigrate();

    /**
     * Returns the migrated version of the model to the immediate next state.
     */
    Optional<Model> migrateModel();

    // ======== USER PREFS ===============
    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    // ======== GUI SETTINGS ===============

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    // ======== ALIAS SETTINGS ===========
    /**
     * Return's the user prefs' alias mappings.
     */
    AliasMappings getAliasMappings();

    /**
     * Sets the user prefs' alias mappings.
     */
    void setAliasMappings(AliasMappings aliasMappings);

    /**
     * Add a user defined alias to the user prefs' alias mappings.
     */
    void addUserAlias(Alias alias);

    // ======== ADDRESS BOOK SETTINGS ===============
    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    // ======== ADDRESS BOOK ACTIONS ===============
    /**
     * Returns true if a expense with the same identity as {@code expense}
     * exists in the address book.
     */
    boolean hasExpense(Expense expense);

    /**
     * Deletes the given expense.
     * The expense must exist in the address book.
     */
    void deleteExpense(Expense target);

    /**
     * Adds the given expense.
     * {@code expense} must not already exist in the address book.
     */
    void addExpense(Expense expense);

    /**
     * Replaces the given expense {@code target} with {@code editedExpense}.
     * {@code target} must exist in the address book.
     * The expense identity of {@code editedExpense} must not be the same as another
     * existing expense in the address book.
     */
    void setExpense(Expense target, Expense editedExpense);

    boolean hasBudget(Budget budget);

    boolean hasBudgetWithName(Description targetDescription);

    Budget getPrimaryBudget();

    void addBudget(Budget budget);

    void switchBudgetTo(Description description);

    void setPrimary(Budget budget);

    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Budget> getFilteredBudgetList();


    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Expense> getFilteredExpenseList();

    /** Returns the predicate of the filltred expense list. **/
    Predicate<? super Expense> getFilteredExpensePredicate();

    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredExpenseList(Predicate<? super Expense> predicate);

    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<Event> getFilteredEventList();

    /** Returns the predicate of the filtered expense list */
    Predicate<? super Event> getFilteredEventPredicate();

    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredEventList(Predicate<? super Event> predicate);

    boolean hasEvent(Event event);


    void addEvent(Event event);

    void deleteEvent(Event target);

    String calculateStatistics(String command, Timestamp date1, Timestamp date2, Period period);

    boolean hasStatistic();

    StringBuilder getStatistic();

}
