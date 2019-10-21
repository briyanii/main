package seedu.address.logic.parser;

import seedu.address.logic.commands.ui.ViewPanelCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.ui.panel.PanelName;

public class ViewPanelCommandParser implements Parser<ViewPanelCommand> {
    @Override
    public ViewPanelCommand parse(String userInput) throws ParseException {
        System.out.println("parsing view");
        if (userInput.isEmpty()) {
            throw new ParseException("No panel specified");
        }
        return new ViewPanelCommand(new PanelName(userInput.trim()));
    }
}
