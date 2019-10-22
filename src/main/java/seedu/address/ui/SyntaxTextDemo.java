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
        commandSyntaxHighlightingTextArea.createPattern("add",
                List.of(
                        new Prefix("d/", "no argument description"),
                        new Prefix("p/", "no argument description")));
        commandSyntaxHighlightingTextArea.enableSyntaxHighlighting();
        stage.setScene(scene);
        scene.widthProperty().addListener((observableValue, number, t1) -> {
            commandSyntaxHighlightingTextArea.setPrefWidth(t1.doubleValue());
            commandSyntaxHighlightingTextArea.setMaxWidth(t1.doubleValue());
            commandSyntaxHighlightingTextArea.setMinWidth(t1.doubleValue());
        });
        stage.show();

    }
}
