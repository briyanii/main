package seedu.address.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Description;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Price;
import seedu.address.model.expense.util.UniqueIdentifierGenerator;
import seedu.address.model.tag.Tag;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;

public class BudgetCardDemo extends Application {

    public void start(Stage stage) {
        Budget budget = new Budget(new Description("aaaa"), new Price("10000"), LocalDate.of(2019,10, 1), Period.of(0, 1, 0));
        Button button1 = new Button("add");
        Button button2 = new Button("delete");
        Button button3 = new Button("update");
        Button button4 = new Button("replace");

        VBox vBox = new VBox();
        BudgetCard budgetCard = new BudgetCard(budget);

        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(button1, button2, button3, button4);

        vBox.getChildren().addAll(budgetCard.getRoot(), buttonBar);

        stage.setScene(new Scene(vBox));
        stage.show();

        button1.setOnMouseClicked(event -> {
            budget.addExpense(new Expense(
                    new Description("temp"), new Price("1000"),
                    new HashSet<Tag>(), UniqueIdentifierGenerator.generateRandomUniqueIdentifier()));
            System.out.println(budget.getExpenses().size());
        });

        button2.setOnMouseClicked(event -> {
            if (budget.getExpenses().size() > 0) {
                budget.getExpenses().remove(0);
                System.out.println(budget.getExpenses().size());
            }
        });

        button3.setOnMouseClicked(mouseEvent -> {
            if (budget.getExpenses().size() > 0) {
                budget.getExpenses().get(0).getTags().add(new Tag("aaaa"));
            }
        });

        button4.setOnMouseClicked(mouseEvent -> {
            if (budget.getExpenses().size() > 0) {
                budget.getExpenses().set(0, new Expense(
                        new Description("temp"), new Price("1000"),
                        new HashSet<Tag>(), UniqueIdentifierGenerator.generateRandomUniqueIdentifier()));
            }
        });
    }

}
