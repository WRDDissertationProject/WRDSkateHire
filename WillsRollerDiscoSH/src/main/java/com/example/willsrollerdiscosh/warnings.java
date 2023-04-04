package com.example.willsrollerdiscosh;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class warnings {
    public static Alert alertSameAmountOfSkates() {
        Alert sameSkates = new Alert(Alert.AlertType.WARNING, "Values are Equal, cannot add anymore skates", ButtonType.OK);
        return sameSkates;
    }

    public static Alert alertEmptyBox() {
        Alert emptyField = new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
        return emptyField;
    }
}
