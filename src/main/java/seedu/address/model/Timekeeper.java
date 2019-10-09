package seedu.address.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;

import seedu.address.model.expense.Event;
import seedu.address.model.expense.Reminder;

/**
 * Compares system time with upcoming Events.
 */
public class Timekeeper {
    public static final LocalDate SYSTEM_DATE = LocalDate.now();
    public static final long THRESHOLD = 7;
    private ObservableList<Event> events;

    public Timekeeper(Model model) {
        events = model.getFilteredEventList();
    }

    public List<Reminder> getReminders() {
        List<Reminder> reminders = new ArrayList<>();
        for (Event event : events) {
            Optional<Reminder> potentialReminder = createReminderIfValid(event);
            if (potentialReminder.isPresent()) {
                reminders.add(potentialReminder.get());
            }
        }
        return reminders;
    }

    /**
     * Dummy.
     *
     * @param event dummy.
     */
    private static Optional<Reminder> createReminderIfValid(Event event) {
        LocalDate timestamp = event.getTimestamp().timestamp;

        long daysLeft = SYSTEM_DATE.until(timestamp, ChronoUnit.DAYS);
        String totalTimeDifference = String.format("%d days", daysLeft);

        return (isUrgent(daysLeft)) ? Optional.of(new Reminder(event, totalTimeDifference)) : Optional.empty();
    }

    private static boolean isUrgent(long daysLeft) {
        return daysLeft < THRESHOLD;
    }
}
