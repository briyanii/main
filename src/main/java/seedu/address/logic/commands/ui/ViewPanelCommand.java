package seedu.address.logic.commands.ui;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.ui.panel.PanelName;

public class ViewPanelCommand extends Command {

    public static final String COMMAND_WORD = "view";
    private PanelName panelName;

    public ViewPanelCommand(PanelName panelName) {
        this.panelName = panelName;
    }

    @Override
    protected void validate(Model model) throws CommandException {
        // no validation needed
    }

    @Override
    protected CommandResult execute(Model model) throws CommandException {
        System.out.println("viewing");
        return new CommandResult("Now Showing ...", false, false, false, true, panelName);
    }

}
