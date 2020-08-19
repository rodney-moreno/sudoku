package org.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.control.Button;
import java.io.File;
import java.io.FileNotFoundException;


public class App extends Application {

    private File input;
    private Board board;

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 960);
        scene.setFill(Color.LIGHTGREY);

        addRect(root);
        addLines(root);
        addNewBoardButton(root, stage);

        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void addRect(Group root) {
        int x = 75;
        int y = 120;
        int width = 720;
        int height = 720;
        Rectangle rect = new Rectangle(x,y,width, height);
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.WHITE);
        root.getChildren().add(rect);
    }

    private void addLines(Group root) {
        for(int i = 1; i <= 9; i++) {
            int x = 75 + (i * 80);
            int y1 = 120;
            int y2 = 840;

            Line line = new Line(x, y1, x, y2);
            root.getChildren().add(line);
        }

        for(int i = 1; i <= 8; i++) {
            int x1 = 75;
            int y = 120 + (i * 80);
            int x2 = 795;

            Line line = new Line(x1,y,x2,y);
            root.getChildren().add(line);
        }
    }

    private void addNewBoardButton(Group root, Stage stage) throws FileNotFoundException {
        Button bt = new Button("New Board");
        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                input = fileChooser.showOpenDialog(stage);
            }
        });

        board = new Board(input);
        root.getChildren().add(bt);
    }
}