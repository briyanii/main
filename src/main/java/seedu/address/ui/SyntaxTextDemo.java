package seedu.address.ui;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import seedu.address.logic.parser.Prefix;

/**
 * Stub. to be deleted.
 */
public class SyntaxTextDemo extends Application {

    /**
     * Stub.
     */
    public void start(Stage stage) {
        CommandSyntaxHighlightingTextArea commandSyntaxHighlightingTextArea = new CommandSyntaxHighlightingTextArea();

        Scene scene = new Scene(commandSyntaxHighlightingTextArea);
        commandSyntaxHighlightingTextArea.importStyleSheet(scene);
        commandSyntaxHighlightingTextArea.createPattern("add", List.of(new Prefix("d/"), new Prefix("p/")), "add d/ <type here to replace placeholder> p/ <placeholder>");
        commandSyntaxHighlightingTextArea.enableSyntaxHighlighting();
        stage.setScene(scene);
        stage.show();

    }
}
