package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS_ALIAS_INPUT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ALIAS_ALIAS_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandTestUtil;
import seedu.address.logic.commands.alias.AliasCommand;
import seedu.address.logic.commands.expense.ClearCommand;
import seedu.address.logic.commands.expense.DeleteCommand;
import seedu.address.logic.commands.expense.EditCommand;
import seedu.address.logic.commands.expense.EditCommand.EditExpenseDescriptor;
import seedu.address.logic.commands.expense.FindCommand;
import seedu.address.logic.commands.expense.ListCommand;
import seedu.address.logic.commands.general.ExitCommand;
import seedu.address.logic.commands.general.HelpCommand;
import seedu.address.logic.commands.statistics.StatsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.expense.DescriptionContainsKeywordsPredicate;
import seedu.address.model.expense.Expense;
import seedu.address.testutil.AliasTestUtil;
import seedu.address.testutil.EditExpenseDescriptorBuilder;
import seedu.address.testutil.ExpenseBuilder;
import seedu.address.testutil.ExpenseUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser;

    private final ReadOnlyUserPrefs readOnlyUserPrefs;

    public AddressBookParserTest() {
        parser = new AddressBookParser();

        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAliasMappings(AliasTestUtil.VALID_ALIAS_MAPPINGS);
        readOnlyUserPrefs = userPrefs;
    }

    @Test
    public void parseCommand_alias() throws Exception {
        AliasCommand aliasCommand = (AliasCommand) parser.parseCommand(
                String.format("%s %s a %s b",
                        AliasCommand.COMMAND_WORD, PREFIX_ALIAS_ALIAS_NAME, PREFIX_ALIAS_ALIAS_INPUT),
                readOnlyUserPrefs);
        assertEquals(aliasCommand, new AliasCommand(AliasTestUtil.ALIAS_A_TO_B));
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD, readOnlyUserPrefs) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3", readOnlyUserPrefs) instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_EXPENSE.getOneBased(), readOnlyUserPrefs);
        assertEquals(new DeleteCommand(INDEX_FIRST_EXPENSE), command);
    }


    @Test
    public void parseCommand_edit() throws Exception {
        Expense expense = new ExpenseBuilder().build();
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(expense).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_EXPENSE.getOneBased() + " "
                        + ExpenseUtil.getEditExpenseDescriptorDetails(descriptor),
                readOnlyUserPrefs);

        assertEquals(new EditCommand(INDEX_FIRST_EXPENSE, descriptor), command);
    }


    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD, readOnlyUserPrefs) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3", readOnlyUserPrefs) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")), readOnlyUserPrefs);
        assertEquals(new FindCommand(new DescriptionContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD, readOnlyUserPrefs) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3", readOnlyUserPrefs) instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD, readOnlyUserPrefs) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3", readOnlyUserPrefs) instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand("", readOnlyUserPrefs));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, ()
            -> parser.parseCommand("unknownCommand", readOnlyUserPrefs));
    }

    @Test
    void parseCommand_stats() throws Exception {
        StatsCommand command = (StatsCommand) parser.parseCommand(
                String.format("%s %s01-10-2019 %s31-10-2019",
                        StatsCommand.COMMAND_WORD,
                        PREFIX_START_DATE,
                        PREFIX_END_DATE),
                readOnlyUserPrefs);
        assertEquals(command, new StatsCommand(
                CommandTestUtil.OCTOBER_FIRST,
                CommandTestUtil.OCTOBER_LAST));

    }

}
