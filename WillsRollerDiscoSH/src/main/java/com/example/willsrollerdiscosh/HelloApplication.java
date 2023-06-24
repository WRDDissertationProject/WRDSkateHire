/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoSH
 *  FILE TITLE: HelloApplication.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Application starting point, launches application and also connects to database
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

/*Resources Used:
 * On Exit Code:  */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //loading the starting scene and setting window size
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("announcements.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 600);

        //setting window related values, such as name, icon and scene
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setTitle("Wills Roller Disco - Skate Hire");
        stage.setScene(scene);
        stage.show();

        //Database connection is also needed here, otherwise announcements cannot be loaded
        //when the application starts
        DBConnect connect = new DBConnect();
        connect.connect();

        //Reloads the announcements when the app is started, so they are up-to-date
        Timer reloadAnnouncements = new Timer();
        reloadAnnouncements.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView lv = (ListView) scene.lookup("#announcementsList");
                        listViews.loadAnnouncementsListView(lv);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); //Reload every second

        //on exit stops all process to ensure nothing continues running in the background.
        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        //When started, application connects to the core database
        DBConnect connect = new DBConnect();
        connect.connect();
        //Checking if a session has currently started
        connect.sessionStartChecker();

        //After the database connection is established, the locks' connection is established
        locks locks = new locks();
        locks.connect();

        //application launch
        launch();
    }
}