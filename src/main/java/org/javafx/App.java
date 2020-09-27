package org.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.control.Button;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;


public class App extends Application {

    private File input;
    private HashMap<Integer, TextField> emptyCells;
    private HashMap<Integer, Character> nonEmptyCells;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        BorderPane root = new BorderPane();
        emptyCells = new HashMap<>();
        nonEmptyCells = new HashMap<>();

        // right button
        VBox right = new VBox();
        right.setPrefWidth(120);
        BorderPane.setMargin(right, new Insets(120,60, 120, 0));
        addCheckButton(right);
        addSolveButton(right);

        // game board
        input = new File("puzzle-6.txt");
        GridPane gp = initializeBoard();
        root.setCenter(gp);
        BorderPane.setMargin(gp, new Insets(120, 60, 120, 120));

        root.setRight(right);


        Scene scene = new Scene(root, 1200, 960);
        scene.setFill(Color.GRAY);
        scene.getStylesheets().add("file:style.css");

        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addNewBoardButton(BorderPane root, Stage stage) throws FileNotFoundException {
        Button bt = new Button("New Board");
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                input = fileChooser.showOpenDialog(stage);
            }
        });

        root.setRight(bt);
    }

    private void addCheckButton(VBox right) {
        Button bt = new Button("Check");
        bt.setMinWidth(180);
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                char[][] board = new char[9][9];
                for(int i = 0; i < 9; i++) {
                    for(int j = 0; j < 9; j++) {
                        if(emptyCells.containsKey(i * 9 + j)) {
                            board[i][j] = emptyCells.get(i * 9 + j).getText().charAt(0);
                        } else {
                            board[i][j] = nonEmptyCells.get(i * 9 + j);
                        }
                    }
                }

                Board puzzle = new Board(board);
                System.out.println(puzzle.isValidSudoku());
            }
        });

        right.getChildren().add(bt);
    }

    private void addSolveButton(VBox right) {
        Button bt = new Button("Solve");
        bt.setMinWidth(180);
        right.getChildren().add(bt);
    }

    private Rectangle createRectangle() {
        Rectangle cellBackground = new Rectangle(80,80, Color.WHITE);
        cellBackground.setStroke(Color.BLACK);

        return cellBackground;
    }

    private TextField createTextField() {
        return new TextField();
    }

    private Text createText(String value) {
        Text text = new Text(value);
        text.setFont(new Font(20));
        return text;
    }

    private GridPane initializeBoard() throws FileNotFoundException {
        GridPane gridPane = new GridPane();
        Scanner fileScanner = new Scanner(input);

        int row = 0;
        while(fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            for(int i = 0; i < line.length(); i++) {
                StackPane stack = new StackPane();
                int index = row * 9 + i;
                if(line.charAt(i) == '.') {
                    TextField textfield = createTextField();
                    emptyCells.put(index, textfield);
                    stack.getChildren().addAll(createRectangle(), textfield);
                } else {
                    Text text = createText("" + line.charAt(i));
                    stack.getChildren().addAll(createRectangle(), createText("" + line.charAt(i)));
                    nonEmptyCells.put(index, line.charAt(i));
                }
                gridPane.add(stack, i, row);
            }
            row++;
        }

        return gridPane;
    }
}