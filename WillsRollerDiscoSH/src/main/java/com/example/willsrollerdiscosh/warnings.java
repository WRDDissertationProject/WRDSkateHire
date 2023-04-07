package com.example.willsrollerdiscosh;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;

public class warnings {
    public static Alert alertSameAmountOfSkates() {
        Alert sameSkates = new Alert(Alert.AlertType.WARNING, "Values are Equal, cannot add anymore skates", ButtonType.OK);
        return sameSkates;
    }

    public static Alert alertEmptyBox() {
        Alert emptyField = new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
        return emptyField;
    }

    public static Alert deleteNotComplete() {
        Alert deleteNotComplete = new Alert(Alert.AlertType.ERROR, "Error: Record Could Not be Completed", ButtonType.OK);
        return deleteNotComplete;
    }

    public static Alert maintenanceEmpty() {
        Alert maintenanceEmpty = new Alert(Alert.AlertType.INFORMATION, "Error: You have empty mandatory fields, please check and try again", ButtonType.OK);
        return maintenanceEmpty;
    }

    public static Alert alertSkateValueError() {
        Alert valueError = new Alert(Alert.AlertType.ERROR, "Skate Count is No Longer Accurate", ButtonType.OK);
        return valueError;
    }
}
