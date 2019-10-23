package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.budget.Budget;
import seedu.address.model.budget.UniqueBudgetList;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Event;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.UniqueEventList;
import seedu.address.model.expense.UniqueExpenseList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameExpense comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueExpenseList expenses;
    private final UniqueBudgetList budgets;
    private final UniqueEventList events;

    /*
     * The 'unusual' code block below is a non-static initialization block,
     * sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to
     * avoid duplication among constructors.
     */
    /*{
        expenses = new UniqueExpenseList();
        budgets = new UniqueBudgetList();
        if (budgets.isEmpty()) {
            Budget defaultBudget = Budget.createDefaultBudget();
            defaultBudget.setPrimary();
            budgets.add(defaultBudget);
        }
        events = new UniqueEventList();
    }*/

    public AddressBook() {
        expenses = new UniqueExpenseList();
        budgets = new UniqueBudgetList();
        events = new UniqueEventList();
    }

    /**
     * Creates an AddressBook using the Expenses in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     * {@code expenses} must not contain duplicate expenses.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.setExpenses(expenses);
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets.setBudgets(budgets);
    }

    public void setPrimary(Budget budget) {
        requireNonNull(budget);
        budgets.setPrimary(budget);
    }

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     * {@code expenses} must not contain duplicate expenses.
     */
    public void setEvents(List<Event> events) {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setExpenses(newData.getExpenseList());
        setBudgets(newData.getBudgetList());
        setEvents(newData.getEventList());
    }

    //=========== Expense-level operations =============================================================

    /**
     * Returns true if an expense with the same identity as {@code expense}
     * exists in the address book.
     */
    public boolean hasExpense(Expense expense) {
        requireNonNull(expense);
        return expenses.contains(expense);
    }

    /**
     * Adds an expense to the address book.
     * The expense must not already exist in the address book.
     */
    public void addExpense(Expense p) {
        if (budgets.isEmpty()) {
            Budget defaultBudget = Budget.createDefaultBudget();
            defaultBudget.setPrimary();
            budgets.add(defaultBudget);
        }
        Budget primaryBudget = budgets.getPrimaryBudget();
        if (p.getBudgetName() == null) {
            p.setBudget(primaryBudget);
            primaryBudget.addExpense(p);
            primaryBudget.updateProportionUsed();
        } else {
            Budget budget = budgets.getBudgetWithName(p.getBudgetName());
            if (budget != null) {
                budget.addExpense(p);
                budget.updateProportionUsed();
            }
        }
        expenses.add(p);
    }

    /**
     * Replaces the given expense {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the address book.
     * The expense identity of {@code editedExpense} must not be the same as another existing
     * expense in the address book.
     */
    public void setExpense(Expense target, Expense editedExpense) {
        requireNonNull(editedExpense);

        expenses.setExpense(target, editedExpense);
        for (Budget budget : budgets) {
            budget.setExpense(target, editedExpense);
        }
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeExpense(Expense key) {
        expenses.remove(key);
        for (Budget budget : budgets) {
            budget.removeIdentical(key);
        }
    }

    //=========== Budget-level operations ================================================================
    /**
     * Returns true if a budget with the same identity as {@code budget}
     * exists in Moolah.
     */
    public boolean hasBudget(Budget budget) {
        requireNonNull(budget);
        return budgets.contains(budget);
    }

    /**
     * Adds a budget to Moolah.
     * The budget must not already exist in Moolah.
     */
    public void addBudget(Budget budget) {
        requireNonNull(budget);
        budgets.add(budget);
    }

    /**
     * Checks whether address book has a budget with the specified name.
     * @param targetDescription The description (i.e. name) of the budget to check.
     * @return A boolean indicating whether the address book has such budget.
     */
    public boolean hasBudgetWithName(Description targetDescription) {
        requireNonNull(targetDescription);
        return budgets.hasBudgetWithName(targetDescription);
    }

    /**
     * Returns the primary budget in the address book.
     * @return The primary budget in the address book.
     */
    public Budget getPrimaryBudget() {
        return budgets.getPrimaryBudget();
    }

    /**
     * Switches the primary budget to the budget with the specified name.
     *
     * @param targetDescription The name of the budget to be switched to.
     */
    public void switchBudgetTo(Description targetDescription) {
        Budget targetBudget = budgets.getBudgetWithName(targetDescription);
        budgets.setPrimary(targetBudget);
    }

    //=========== Event-level operations ================================================================

    /**
     * Returns true if an event with the same identity as {@code event}
     * exists in the address book.
     */
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return events.contains(event);
    }

    /**
     * Adds an event to the address book.
     * The event must not already exist in the address book.
     */
    public void addEvent(Event event) {
        events.add(event);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeEvent(Event key) {
        events.remove(key);
    }
    //// util methods

    @Override
    public String toString() {
        return expenses.asUnmodifiableObservableList().size() + " expenses";
        // TODO: refine later
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Budget> getBudgetList() {
        return budgets.asUnmodifiableObservableList();
    }

    public ObservableList<Event> getEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && expenses.equals(((AddressBook) other).expenses));
    }

    @Override
    public int hashCode() {
        return expenses.hashCode();
    }
}
