package seedu.address.model.expense;

// import java.time.LocalDateTime;
// import java.time.temporal.ChronoUnit;
// import java.util.Optional;

/**
 * A reminder of an upcoming task that the user had inputted.
 */
public class Reminder {
    private Event event;
    private long daysLeft;

    public Reminder(Event event, long daysLeft) {
        this.event = event;
        this.daysLeft = daysLeft;
    }

    /**
     * Formats the reminder into a readable form for the user.
     *
     * @return The reminder message.
     */
    @Override
    public String toString() {
        return String.format(
                "You have %d days left before this event: %s!",
                daysLeft, event.getDescription());
    }

}
