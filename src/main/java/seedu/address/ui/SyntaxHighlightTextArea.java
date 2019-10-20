package seedu.address.ui;

import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.BACK_SLASH;
import static javafx.scene.input.KeyCode.C;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.K;
import static javafx.scene.input.KeyCode.KP_DOWN;
import static javafx.scene.input.KeyCode.KP_LEFT;
import static javafx.scene.input.KeyCode.KP_RIGHT;
import static javafx.scene.input.KeyCode.KP_UP;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyCode.V;
import static javafx.scene.input.KeyCode.X;
import static javafx.scene.input.KeyCode.Y;
import static javafx.scene.input.KeyCombination.SHIFT_ANY;
import static javafx.scene.input.KeyCombination.SHIFT_DOWN;
import static javafx.scene.input.KeyCombination.SHORTCUT_ANY;
import static javafx.scene.input.KeyCombination.SHORTCUT_DOWN;
import static javafx.scene.input.KeyCombination.keyCombination;
import static org.fxmisc.wellbehaved.event.EventPattern.ALL_MODIFIERS_AS_ANY;
import static org.fxmisc.wellbehaved.event.EventPattern.eventType;
import static org.fxmisc.wellbehaved.event.EventPattern.keyPressed;
import static org.fxmisc.wellbehaved.event.EventPattern.keyTyped;
import static org.fxmisc.wellbehaved.event.EventPattern.mousePressed;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.Caret;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.fxmisc.wellbehaved.event.EventPattern;
import org.fxmisc.wellbehaved.event.InputMap;
import org.fxmisc.wellbehaved.event.Nodes;
import org.reactfx.Subscription;

import javafx.beans.InvalidationListener;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.logic.parser.Prefix;

/**
 * A single line text area utilising RichTextFX to support syntax highlighting of user input.
 * This code is adapted from OverrideBehaviorDemo and JavaKeywordsDemo in RichTextFX.
 */
public class SyntaxHighlightTextArea extends StackPane {

    // temporary static patterns before proper integration

    private TextArea textField;

    private StyleClassedTextArea styleClassedTextArea;

    private StackPane stackPane;

    private static final String INPUT_PATTERN_TEMPLATE = "((?<preamble>^\\s*)(?<COMMAND>%s))|%s(?<arg>\\S+)";

    private Map<String, Pattern> stringPatternMap;

    private Map<String, Integer> stringIntMap;

    private Map<String, String> stringAutofillMap;

    private InputMap<Event> consumeKeyPress;

    private EventHandler<KeyEvent> disableAutoCompletionOnBackspaceDownHandler;

    private EventHandler<KeyEvent> enableAutoCompletionOnBackspaceReleasedHandler;

    private ChangeListener<String> autoFillAddCompulsoryFieldsListener;

    private Subscription cleanupWhenNoLongerNeedIt;

    public SyntaxHighlightTextArea() {
        super();

        // actual text field with text formatter
        TextArea a = new TextArea();
        textField = new TextArea();
        textField.setTextFormatter(new TextFormatter<String>(this::temp));

        // richtextfx element to underlay text field
        styleClassedTextArea = new StyleClassedTextArea();
        // stackpane to overlay textfield over richtextfx element

        getChildren().addAll(styleClassedTextArea, textField);




        stringPatternMap = new HashMap<>();
        stringIntMap = new HashMap<>();
        stringAutofillMap = new HashMap<>();

        // adapted from RichTextFX OverrideBehaviorDemo
        consumeKeyPress = InputMap.consume(EventPattern.anyOf(
                // enter
                keyPressed(ENTER, SHIFT_ANY, SHORTCUT_ANY),
                // disable paste cut copy
                keyPressed(LEFT, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(KP_LEFT, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(RIGHT, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(KP_RIGHT, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(DOWN, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(KP_DOWN, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(UP, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(KP_UP, SHIFT_DOWN, SHORTCUT_ANY),
                keyPressed(C, SHIFT_ANY, SHORTCUT_DOWN),
                keyPressed(X, SHIFT_ANY, SHORTCUT_DOWN),
                keyPressed(V, SHIFT_ANY, SHORTCUT_DOWN),
                // undo redo
                keyPressed(Y, SHIFT_ANY, SHORTCUT_DOWN),
                keyPressed(KeyCode.Z, SHIFT_ANY, SHORTCUT_DOWN),

                // prevent select all
                keyPressed(A, SHIFT_ANY, SHORTCUT_DOWN),
                // mouse select
                eventType(MouseEvent.MOUSE_DRAGGED),
                eventType(MouseEvent.DRAG_DETECTED),
                mousePressed().unless(e -> e.getClickCount() == 1 && !e.isShiftDown())
        ));

        // to apply arrow key navigation to styled text area

        textField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1) {
                textField.clear();
            }
        });

        textField.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode().isNavigationKey()) {
                styleClassedTextArea.fireEvent(keyEvent);
            } else if (keyEvent.getCode().isLetterKey() || keyEvent.getCode().isDigitKey()
                    || keyEvent.getCode().equals(KeyCode.COLON) || keyEvent.getCode().equals(KeyCode.SEMICOLON)
                    || keyEvent.getCode().equals(KeyCode.BRACELEFT) || keyEvent.getCode().equals(KeyCode.BRACERIGHT)
                    || keyEvent.getCode().equals(KeyCode.LEFT_PARENTHESIS) || keyEvent.getCode().equals(KeyCode.RIGHT_PARENTHESIS)
                    || keyEvent.getCode().equals(KeyCode.CLOSE_BRACKET) || keyEvent.getCode().equals(KeyCode.OPEN_BRACKET)
                    || keyEvent.getCode().equals(KeyCode.PERIOD) || keyEvent.getCode().equals(KeyCode.COMMA)
                    || keyEvent.getCode().equals(KeyCode.SLASH) || keyEvent.getCode().equals(BACK_SLASH)
                    || keyEvent.getCode().equals(KeyCode.QUOTE)|| keyEvent.getCode().equals(KeyCode.BACK_QUOTE)
                    || keyEvent.getCode().equals(KeyCode.SPACE) || keyEvent.getCode().equals(KeyCode.EQUALS)
                    || keyEvent.getCode().equals(KeyCode.MINUS)) {
                if (keyEvent.isShiftDown() || keyEvent.isShortcutDown()) {
                    return;
                }
                KeyEvent press = new KeyEvent(null, styleClassedTextArea, KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false);
                styleClassedTextArea.fireEvent(press);
            } else if (keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
                KeyEvent press = new KeyEvent(null, styleClassedTextArea, KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT, false, false, false, false);
                styleClassedTextArea.fireEvent(press);
            }

        });

        textField.textProperty().addListener((observableValue, s, t1) -> {
            styleClassedTextArea.replaceText(t1);

        });

        textField.caretPositionProperty().addListener((observableValue, number, t1) -> {
            try {
                styleClassedTextArea.displaceCaret((int) t1);
            } catch (IndexOutOfBoundsException e) {
                styleClassedTextArea.displaceCaret(styleClassedTextArea.getLength());
            }
        });



        prefWidth(-1);
        textField.prefWidth(-1);
        styleClassedTextArea.prefWidth(-1);

//         temporary sizes
        double h = 30;
        styleClassedTextArea.setPrefHeight(h);
        styleClassedTextArea.setMaxHeight(h);
        styleClassedTextArea.setMinHeight(h);
        styleClassedTextArea.setDisable(true);
        styleClassedTextArea.setShowCaret(Caret.CaretVisibility.ON);
//        textField.setStyle("-fx-text-fill: transparent");


        textField.setPadding(Insets.EMPTY);
        textField.setBackground(Background.EMPTY);
        textField.setOpacity(0);

        Nodes.addInputMap(textField, consumeKeyPress);

        Nodes.addInputMap(styleClassedTextArea, consumeKeyPress);

        // listeners for auto completion
//        textProperty().addListener(autoFillAddCompulsoryFieldsListener);

        // key event handlers
//        //addEventHandler(KeyEvent.KEY_PRESSED, enterKeyPressedHandler);
//        addEventHandler(KeyEvent.KEY_PRESSED, disableAutoCompletionOnBackspaceDownHandler);
//        addEventHandler(KeyEvent.KEY_RELEASED, enableAutoCompletionOnBackspaceReleasedHandler);

        // when no longer need syntax highlighting and wish to clean up memory leaks
        // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
    }

    public void clear() {
        textField.clear();
    }

    public String getText() {
        return textField.getText();
    }

    public StringProperty textProperty() {
        return textField.textProperty();
    }

    /**
     * Enable syntax highlighting.
     */
    public void enableSyntaxHighlighting() {
        cleanupWhenNoLongerNeedIt =
                styleClassedTextArea.multiPlainChanges()
                    .successionEnds(Duration.ofMillis(500))
                        .subscribe(ignore -> {
                            styleClassedTextArea.setStyleSpans(0, computeHighlighting(styleClassedTextArea.getText()));
                        });
    }

    /**
     * Sets the style class of all the text to the style class provided.
     * @param styleClass style class to apply to the text in the text area.
     */
    public void overrideStyle(String styleClass) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        spansBuilder.add(Collections.singleton(styleClass), styleClassedTextArea.getLength());
        styleClassedTextArea.setStyleSpans(0, spansBuilder.create());
        if (cleanupWhenNoLongerNeedIt != null) {
            cleanupWhenNoLongerNeedIt.unsubscribe();
        }
    }

    /**
     * Import the css stylesheet containing the different styles for the syntax highlighter.
     */
    public void importStyleSheet(Scene parentSceneOfNode) {
        parentSceneOfNode
                .getStylesheets()
                .add(SyntaxHighlightTextArea.class.getResource("/view/syntax-highlighting.css")
                        .toExternalForm());
        enableSyntaxHighlighting();
    }

    /**
     * Compile pattern for a command input syntax used for matching during syntax highlighting.
     * @param commandWord The command word of the command.
     * @param prefixes The list of prefixes of the command.
     * @return The compiled pattern.
     */
    private Pattern compileCommandPattern(String commandWord, List<Prefix> prefixes) {
        StringBuilder prefixesPatterns = new StringBuilder();
        int count = 0;
        for (Prefix prefix : prefixes) {
            count++;
            prefixesPatterns.append(String.format("(?<prefix%s>%s)|", count, prefix.getPrefix()));
        }

        return Pattern.compile(String.format(INPUT_PATTERN_TEMPLATE, commandWord, prefixesPatterns.toString()));
    }

    /**
     * Add support for syntax highlighting and auto fill for the specified command.
     *
     * @param command The command word
     * @param prefixes List of prefixes required in the command
     * @param requiredSyntax Syntax for the autofill to replace text with
     */
    public void createPattern(String command, List<Prefix> prefixes, String requiredSyntax) {
        Pattern p = compileCommandPattern(command, prefixes);
        stringPatternMap.put(command, p);
        stringIntMap.put(command, prefixes.size());
        stringAutofillMap.put(command, requiredSyntax);
    }

    /**
     * Remove support for syntax highlighting and auto fill for the specified command.
     * @param command
     */
    public void removePattern(String command) {
        if (stringPatternMap.containsKey(command)) {
            stringPatternMap.remove(stringPatternMap.get(command));
            stringIntMap.remove(stringIntMap.get(command));
            stringAutofillMap.remove(stringAutofillMap.get(command));
        }
    }

    /**
     * Returns the StyleSpans to apply rich text formatting to the text area.
     *
     * This method decides which pattern to use to highlight syntax.
     *
     * @param text The text to be formatted.
     * @return the StyleSpans to apply rich text formatting to the text area.
     */
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        String commandWordRegex = String.join("|", stringPatternMap.keySet());
        Matcher m = Pattern.compile("^\\s*(?<COMMAND>" + commandWordRegex + ")\\s*").matcher(text);

        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        if (m.find()) {
            Pattern p = stringPatternMap.get(m.group("COMMAND"));
            int prefixCount = stringIntMap.get(m.group("COMMAND"));

            if (p == null) {
                spansBuilder.add(Collections.emptyList(), text.length());
                return spansBuilder.create();
            } else {
                return computeHighlighting(text, p, prefixCount);
            }
        }

        // if not a command
        spansBuilder.add(Collections.emptyList(), text.length());
        return spansBuilder.create();
    }

    // adapted from RichTextFX JavaKeywordDemo
    /**
     * Returns the StyleSpans to apply rich text formatting to the text area, using a given pattern.
     *
     * @param text The text to be formatted (guaranteed that the input's command word matches this pattern).
     * @param pattern The pattern used to apply formatting.
     * @param prefixcount The number of prefixes in the command.
     * @return the StyleSpans to apply rich text formatting to the text area.
     */
    private StyleSpans<Collection<String>> computeHighlighting(String text, Pattern pattern, int prefixcount) {
        // pattern should match the command word
        Matcher matcher = pattern.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        String command = null;
        String lastPrefix = null;
        int offset = 0;
        while (matcher.find()) {

            // highlight command word
            String styleClass = null;
            if (matcher.group("COMMAND") != null) {
                command = matcher.group("COMMAND");
                // compute the whitespaces before the command word to be unstyled
                offset = matcher.group("preamble").length();
                styleClass = "command-word";
            } else {
                if (command == null) {
                    break;
                }
                for (int groupNum = 1; groupNum <= prefixcount; groupNum++) {
                    if (matcher.group("prefix" + groupNum) != null) {
                        lastPrefix = "prefix" + groupNum;
                        styleClass = "style" + groupNum;
                        break;
                    } else if (matcher.group("arg") != null) {
                        if (lastPrefix != null) {
                            styleClass = lastPrefix.replace("prefix", "arg");
                        } else {
                            styleClass = "string";
                        }
                        break;
                    }
                }
            }
            /* should not remain null */ assert styleClass != null;

            spansBuilder.add(Collections.emptyList(), matcher.start() + offset - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start() - offset);
            offset = 0;
            lastKwEnd = matcher.end();
        }

        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);

        return spansBuilder.create();
    }

    // text formatter to handle replacing of placeholders
    private TextFormatter.Change temp(TextFormatter.Change change) {
        if (change.isContentChange()) {

            String ch = "";
            if (change.isReplaced()) {
                ch = "replace";
            } else if (change.isDeleted()) {
                ch = "del";
            } else {
                ch = "add";
            }

            String syntax = "add d/ <description> p/ <price>";
            Pattern syntaxRegex = Pattern.compile("(?<whitespace1>\\s*)(?<commandword>add)(?<whitepspace2>\\s+)" +
                    "(?<prefix1>d/)(?<description>.*?)(?<whitespace4>\\s+)" +
                    "(?<prefix2>p/)(?<price>.*)");

            Pattern commandWordRegex = Pattern.compile("(\\s*)add(\\s*)");

            int start = change.getRangeStart();
            int end = change.getRangeEnd();

            int caret = change.getControlCaretPosition();
            int anchor = change.getControlAnchor();


            //change.setText(change.getText().replaceAll("\\s", "_"));

            String state = "";
            String description = "";
            String price = "";


            boolean hasNonNullDesc = false;
            boolean hasNonNullPrice = false;

            if (commandWordRegex.matcher(change.getControlNewText()).matches()) {
                state = "[found command adding syntax]";
                change.setRange(0, change.getControlNewText().length()-1);
                change.setText(syntax);
            }

            if (syntaxRegex.matcher(change.getControlNewText()).matches()) {

                Matcher m1 = syntaxRegex.matcher(change.getControlNewText());
                m1.find();

                int groupStart = m1.start("description");
                int groupEnd = m1.end("description");

                if (change.getCaretPosition() <= groupEnd && change.getCaretPosition() >= groupStart) {
                    state = "[changing description]";
                    Matcher m2 = syntaxRegex.matcher(change.getControlText());
                    if (m2.find() && m2.group("description").trim().equals("<description>")) {
                        String before = m1.group("commandword") + " " + m1.group("prefix1") + " ";
                        String input = change.getText();
                        String after = " " + m1.group("prefix2") + " " + m1.group("price");
                        if (change.isDeleted()) {
                            change.setRange(0, change.getControlNewText().length()-1);
                        } else {
                            change.setRange(0, change.getControlNewText().length()-1);
                        }
                        change.setText(before + input + after);
                        change.setCaretPosition(before.length() + input.length());
                        change.setAnchor(before.length() + input.length());
                    }
                }

                description = m1.group("description").trim();
                hasNonNullDesc = !description.trim().isEmpty();

                groupStart = m1.start("price");
                groupEnd = m1.end("price");

                if ( groupStart <= change.getCaretPosition() && change.getCaretPosition() <= groupEnd) {
                    state = "[changing price]";
                    Matcher m2 = syntaxRegex.matcher(change.getControlText());
                    if (m2.find() && m2.group("price").trim().equals("<price>")) {
                        String before = m1.group("commandword") + " " + m1.group("prefix1") + " " + m1.group("description") + " " + m1.group("prefix2") + " ";
                        String input = change.getText();
                        String after = "";
                        change.setRange(0, change.getControlNewText().length() - 1);
                        change.setText(before + input + after);
                        change.setCaretPosition(before.length() + input.length());
                        change.setAnchor(before.length() + input.length());
                    }
                }

                price = m1.group("price").trim();
                hasNonNullPrice = !price.trim().isEmpty();



            }

        }


        return change;

    }


}
