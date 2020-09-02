package org.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Cell{

    private StackPane stackPane;
    private String cellValue;
    public Cell(String value) {
        cellValue = value;
        stackPane = new StackPane();
        stackPane.getChildren().addAll(createRectangle(), createText(value));
    }

    public Cell() {
        stackPane = new StackPane();
        cellValue = "";
        stackPane.getChildren().addAll(createRectangle(), createTextArea());
    }

    public StackPane getCellStackPane() {
        return stackPane;
    }


    private Rectangle createRectangle() {
        Rectangle cellBackground = new Rectangle(80,80, Color.WHITE);
        cellBackground.setStroke(Color.BLACK);

        return cellBackground;
    }

    private Text createText(String value) {
        Text cellText = new Text(value);
        cellText.setFont(new Font(40));

        return cellText;
    }

    private TextField createTextArea() {
        TextField field = new TextField();
        field.setPrefWidth(80);


        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                cellValue = newValue;
            }
        });


        return field;
    }

    public String getCellValue() {
        return cellValue;
    }
}
