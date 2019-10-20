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
        SyntaxHighlightTextArea syntaxHighlightTextArea = new SyntaxHighlightTextArea();

        Scene scene = new Scene(syntaxHighlightTextArea);
        syntaxHighlightTextArea.importStyleSheet(scene);
        syntaxHighlightTextArea.createPattern("add", List.of(new Prefix("d/"), new Prefix("p/")), "add d/ <description> p/ <price>");
        syntaxHighlightTextArea.enableSyntaxHighlighting();

        stage.setScene(scene);
        stage.show();

    }



}
