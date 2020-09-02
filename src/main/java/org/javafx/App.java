package org.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.Stack;


public class App extends Application {

    private File input;
    private GridPane gp;
    private HashMap<Integer, TextField> map;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        BorderPane root = new BorderPane();

        map = new HashMap<Integer, TextField>();
        input = new File("board-1.txt");
        GridPane gp = initializeBoard();
        root.setCenter(gp);
        addCheckButton(root);


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

    private Group createEmptyBoard() {
        Group emptyBoard = new Group();



        // create blank square
        Rectangle rect = new Rectangle(720,720);
        rect.setFill(Color.WHITE);

        emptyBoard.getChildren().add(rect);


        // add the vertical lines
        for(int i = 1; i < 9; i++) {
            int x = i * 80;
            int y1 = 0;
            int y2 = 720;

            Line line = new Line(x, y1, x, y2);
            line.setFill(Color.BLACK);
            emptyBoard.getChildren().add(line);
        }

        // add the horizontal lines
        for(int i = 1; i < 9; i++) {
            int x1 = 0;
            int y = i * 80;
            int x2 = 720;

            Line line = new Line(x1,y,x2,y);
            line.setFill(Color.BLACK);
            emptyBoard.getChildren().add(line);
        }

        return emptyBoard;
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

    private void addCheckButton(BorderPane root) {
        Button bt = new Button("Check");
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for(int key : map.keySet()) {
                    System.out.println("Index: " + key + " Value: " + map.get(key).getText());
                }
            }
        });

        root.setRight(bt);
    }

    private Rectangle createRectangle() {
        Rectangle cellBackground = new Rectangle(80,80, Color.WHITE);
        cellBackground.setStroke(Color.BLACK);

        return cellBackground;
    }

    private TextField createTextField() {
        TextField cur = new TextField();
        return cur;
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
                if(line.charAt(i) == '.') {
                    TextField textfield = createTextField();
                    int index = row * 9 + i;
                    map.put(index, textfield);
                    stack.getChildren().addAll(createRectangle(), textfield);
                } else {
                    stack.getChildren().addAll(createRectangle(), createText("" + line.charAt(i)));
                }
                gridPane.add(stack, i, row);
            }
            row++;
        }

        return gridPane;
    }
}