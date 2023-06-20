/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoSH
 *  FILE TITLE: warnings.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Used to show errors to the user, improves users interaction with the application..
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class warnings {
    public static Alert alertSameAmountOfSkates() {
        return new Alert(Alert.AlertType.WARNING, "Values are Equal, cannot add anymore skates", ButtonType.OK);
    }

    public static Alert alertEmptyBox() {
        return new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
    }

    public static Alert deleteNotComplete() {
        return new Alert(Alert.AlertType.ERROR, "Error: Record Could Not be Completed", ButtonType.OK);
    }

    public static Alert maintenanceEmpty() {
       return new Alert(Alert.AlertType.INFORMATION, "Error: You have empty mandatory fields, please check and try "
               + "again", ButtonType.OK);
    }

    public static Alert alertSkateValueError() {
        return new Alert(Alert.AlertType.ERROR, "Skate Count is No Longer Accurate", ButtonType.OK);
    }

    public static Alert sessionNotStarted(){
        return new Alert(Alert.AlertType.WARNING, "Cannot Admit Customers Before a Session Has Started");
    }
}
