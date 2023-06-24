/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoSH
 *  FILE TITLE: sceneSelector.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *    All Methods that are not seperated and grouped, contains all the scene switches and all the methods that relate,
 *    such as scene selectors, actions and buttons etc.
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*Resources Used:
 * Loggers:
 * Choice Boxes:
 * Custom List Views
 * Locks
 * Delays */

public class sceneSelector {
    //Setting Variables
    @FXML
    Button lockButton, unlockButton;
    private Stage stage;
    private static Scene scene;
    private static Parent root;
    @FXML
    TextArea ticketsTextBox;
    @FXML
    static
    Label sessionStatus, salesStatus;
    boolean skateHireLocked = false;
    static String  resourceName, lockedBy = "SkateHireApp";

    //When the home tab button is pressed, is called
    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Reloading automatically every second
        reloader.sessionStartReloader();
    }
    //used to check if the session has started, contains a single call to the database
    public static boolean sessionChecker() throws SQLException {return DBConnect.checkForSessionStart();}

    //Checks if a session has started
    //If a session has started sets the text at the top of the app to the session details
    public static void sessionStatus(){
        Platform.runLater(() -> {

            sessionStatus = (Label) root.lookup("#sessionStatus");

            // Check if session has started
            try {
                String sessionTime = DBConnect.getSessionStartTime();
                if (sessionChecker()) {
                    sessionStatus.setText("Session: " + sessionTime);
                } else {
                    sessionStatus.setText(sessionTime);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //Loaded when skate hire is clicked
    public void switchToSkateHire(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skateHire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Reloads the skate hire
        //Continues start of session starts
        reloader.SkateHireReloader(scene);
        reloader.sessionStartReloader();
    }
    //Loaded when tickets is clicked
    public void switchToTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Reloads the ticket
        //Continues start of session starts
        reloader.TicketsReloader(scene);
        reloader.sessionStartReloader();
    }
    //Loads when create ticket is clicked from within the ticket scene
    public void switchToCreateTicket(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("createTicket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Continues checks of session status
        reloader.sessionStartReloader();
    }
    //Switches to edit or delete ticket scene from within the ticket scene
    public void switchToEditOrDeleteTicket(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("editOrDeleteTicket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Reloads the tickets with the edit and delete buttons
        //Continuous checks of the session status at the top of the scene
        reloader.EditOrDeleteTicketsReloader(scene);
        reloader.sessionStartReloader();
    }

    //Loaded when maintenance button is clicked
    public void switchToMaintenance(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //Reloads the maintenance logs
        //Continuous checks of the session status at the top of the scene
        reloader.maintenanceReloader(scene);
        reloader.sessionStartReloader();
    }

    //Loaded when a user wants to add a new maintenance issue
    @FXML
    public void switchToAddMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("createMaintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //setting Choice Box with values for the user to select from
        //otherwise is blank
        setMaintenanceTypeChoiceBox();
        setSkateSizeChoiceBox();
        //Continuous checks of the session status at the top of the scene
        reloader.sessionStartReloader();
    }

    //Method starts when user clicks the extra sales page to log purchases
    @FXML
    public void switchToExtraSales(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("extraSales.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Continuous checks of the session status at the top of the scene
        reloader.sessionStartReloader();
    }

    //Checks if a session is currently running, if above 0 there is a session running as record is present
    public static void checkForRecord(int count) {
        if (count > 0) {
            System.out.println("Session Currently Running");

        } else {
            System.out.println("Session Stopped");

        }
    }

    //Half finished
    //Changes the appearance of lock button
    public void lockSkateHire() {
        System.out.println("Locked");
        this.lockButton.setVisible(false);
        skateHireLocked = true;
        this.unlockButton.setVisible(true);
    }

    //Half finished
    //Changes the appearance of lock button
    public void unlockSkateHire() {
        System.out.println("unlocked");
        this.unlockButton.setVisible(false);
        skateHireLocked = false;
        this.lockButton.setVisible(true);
    }

    //Loads when the user is on the skate hire page
    public static ObservableList<Skate> loadSkateHire(ListView lv){
        resourceName = "skateHire";
        //Load all skate values
        ObservableList<Skate> data = DBConnect.loadSkates();
        // get the existing list view and replace it with the list items
        if (lv.getItems().isEmpty()){
            lv.setCellFactory(tv -> new ListCell<Skate>() {
                @Override
                protected void updateItem(Skate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Button button = new Button("Pair Returned");
                        button.getStyleClass().add("SH-Button");
                        Label label = new Label( "Skate Size: " + item.getSkateSize() + "\nSkates Available: " + item.getSkateAmount());
                        HBox hbox = new HBox();
                        hbox.getChildren().addAll(label, button);
                        hbox.setSpacing(15);
                        setGraphic(hbox);
                        //Colour Coding based on the skate amount (indicator of skates being hired)
                        if (item.getSkateAmount() > 10) {
                            setStyle("-fx-background-color:#48992B;");
                            label.setTextFill(Color.web("BEBEBE"));
                        } else if (item.getSkateAmount() <= 10 && item.getSkateAmount() > 5) {
                            setStyle("-fx-background-color: #F7AE2C;");
                            label.setTextFill(Color.web("2D2D2D"));
                        } else if (item.getSkateAmount() <= 5) {
                            setStyle("-fx-background-color: #FA3837;");
                            label.setTextFill(Color.web("BEBEBE"));
                        } else {
                            setStyle("");
                        }
                        button.setOnAction(event -> {
                            // increment skate amount for this row
                            //locked so other transactions on the skate count can't take place
                            try {
                                locks.lock(resourceName, lockedBy);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            // increment skate amount for this row
                            int currentValue = item.getSkateAmount();

                            //if skates below max value replace
                            try {
                                if (!compareToMaxValue(currentValue, item.getSkateSize())) {

                                    int newValue = item.getSkateAmount() + 1 ;
                                    item.setSkateAmount(newValue);
                                    String skateSize = item.getSkateSize();
                                    try {
                                        DBConnect.updateSkateListPlus(newValue, skateSize);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }

                                    if (item.getSkateAmount() > 0) {
                                        label.setText(item.getSkateSize() + " " + item.getSkateAmount());
                                        label.setText(item.getSkateSize() + " " + item.getSkateAmount());
                                    } else if (item.getSkateAmount() > 10) {
                                        setStyle("-fx-background-color:#48992B;");
                                        label.setTextFill(Color.web("BEBEBE"));

                                    } else if (item.getSkateAmount() <= 10 && item.getSkateAmount() > 5) {
                                        setStyle("-fx-background-color: #F7AE2C;");
                                        label.setTextFill(Color.web("2D2D2D"));

                                    } else if (item.getSkateAmount() <= 5) {
                                        setStyle("-fx-background-color: #FA3837;");
                                        label.setTextFill(Color.web("BEBEBE"));
                                    }
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                locks.unlock(resourceName, lockedBy);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                    }

                }
            });
        }
        return data;
    }

    //Comapring the skate values to the maximum, if the values are equal then no more skates are added
    //If there are more skates than the starting value then the user is prompted that the skate count is no longer accurate
    public static boolean compareToMaxValue(int currentValue, String skateSize) throws SQLException {
        int newValue = currentValue + 1;
        int currentMax = DBConnect.checkSkatesForMax(skateSize);

        if(currentValue == currentMax) {
         System.out.println("Values are Equal, cannot add anymore skates");
         warnings.alertSameAmountOfSkates().show();

         return true;
        }
        else if (newValue > currentMax) {
            System.out.println("Error, Skate Count is No Longer Accurate");
            warnings.alertSkateValueError().show();
            return true;
        }
        return false;

    }

    //Called when the user clicks to create a ticket
    public void createTicket() throws SQLException {
        //used for locks
        resourceName = "tickets";
        //Stops the user from submitting an empty text box as a ticket
        if (ticketsTextBox.getText().isEmpty()) {
            warnings.alertEmptyBox().show();
        } else {
            //Before proceeding, locks so other resources cannot access tickets until complete
            locks.lock(resourceName, lockedBy);
            //Get the users text
            String ticketText = ticketsTextBox.getText();
            System.out.println("Text Found");
            //insert the text into the database
            DBConnect insertTicket = new DBConnect();
            //if successful clear the text box and reload the list view to show the new ticket
            DBConnect.insertTicket(ticketText);
            ticketsTextBox.clear();
            //release lock when finished
            locks.unlock(resourceName,lockedBy);
        }
    }
    //Runs when the user chooses to delete a ticket
    public static void deleteFunction(String item){
        resourceName = "tickets";
        try {
            //locks the resource so other changes can not be made to the tickets while this transaction is running
            locks.lock(resourceName, lockedBy);
            //Deletes based on contents of the ticket
            String ticket = item.substring(8);
            boolean success = DBConnect.deleteTicket(ticket);

            //If successful clears the text boxes to reduce confusion
            //Refreshes the list view so the ticket is no longer on the list
            if (success) {
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListViewEditOrDelete");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadTicketEditOrDeleteListView(lv);
                    System.out.println("Tickets Reload Delete");
                }
            }
            //Releases locks when transaction is completed
            locks.unlock(resourceName,lockedBy);
        }
        catch(Exception e) {
            warnings.deleteNotComplete().show();
        }
    }

    //Setting the defaults for a choice box
    //Separate method for code quality
    public void setMaintenanceTypeChoiceBox() {
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        List<String> type = new ArrayList<>();
        type.add("Skate Hire");
        type.add("Games Equipment");
        type.add("Lighting Rig");
        type.add("DJ Equipment");
        type.add("Front Door");
        type.add("Other");
        choiceBox.getItems().addAll(type);
    }

    //Setting the defaults for a choice box
    //Separate method for code quality
    public void setSkateSizeChoiceBox() {
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        List<String> skate = new ArrayList<>();

        skate.add(null);
        skate.add("C11");
        skate.add("C12");
        skate.add("C13");
        skate.add("1");
        skate.add("2");
        skate.add("3");
        skate.add("4");
        skate.add("5");
        skate.add("6");
        skate.add("7");
        skate.add("8");
        skate.add("9");
        skate.add("10");
        skate.add("11");
        skate.add("12");
        skate.add("13");
        choiceBox.getItems().addAll(skate);
    }

    //Used for the maintenance forms
    public void createMaintenanceYesButton(){
        Button noButton = (Button) scene.lookup("#CMRNo");
        noButton.setVisible(true);

        Button yesButton = (Button) scene.lookup("#CMRYes");
        yesButton.setVisible(false);

        Label skateSizeLbl = (Label) scene.lookup("#skateSizeLbl");
        skateSizeLbl.setVisible(true);

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        choiceBox.setVisible(true);
    }

    //Used for the maintenance forms
    public void createMaintenanceNoButton(){
        Button yesButton = (Button) scene.lookup("#CMRYes");
        yesButton.setVisible(true);

        Button noButton = (Button) scene.lookup("#CMRNo");
        noButton.setVisible(false);

        Label skateSizeLbl = (Label) scene.lookup("#skateSizeLbl");
        skateSizeLbl.setVisible(false);

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        choiceBox.setVisible(false);
    }

    //Called when maintenance log is submitted
    public void createMaintenanceSubmit() {
        //Fetches selected value from choice boxes and text boxes
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        ChoiceBox<String> skateSize = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        TextArea maintenanceDetails = (TextArea) scene.lookup("#maintenanceDetails");

        //if null, show an error
        //No values on the form can be empty
        if (choiceBox == null || choiceBox.getValue() == null || choiceBox.getValue().isEmpty() || maintenanceDetails == null || maintenanceDetails.getText().isEmpty()) {
            warnings.maintenanceEmpty().show();
        } else {
            try {
                //If submitted then lock the maintenance resource so no other transactions can take place on that
                //table
                resourceName = "maintenance";
                locks.lock(resourceName, lockedBy);
                String typeIn = choiceBox.getValue();
                String details = maintenanceDetails.getText();
                String skateSizeIn = skateSize.getValue();
                DBConnect.insertMaintenance(typeIn, details, skateSizeIn);
                //Once completed, clear the boxes and unlock the resource
                maintenanceDetails.clear();
                locks.unlock(resourceName, lockedBy);
            } catch (SQLException e) {
                System.out.println("Couldn't Add Maintenance Record");
                throw new RuntimeException(e);

            }

        }
    }

    //Used to show users an output when they add an extra sales, stops duplicates
    public void showSalesLabel(Parent root) {
        Label salesLabel= (Label) root.lookup("#salesStatus");
        salesLabel.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        //Delay so the user can read the message before it automatically disapeers
        executor.schedule(() -> Platform.runLater(() -> salesLabel.setVisible(false)), 2, TimeUnit.SECONDS);
    }

    //Adds Glow Sticks to Sales Log
    public void addGlowstickSale(ActionEvent event) throws SQLException {
        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            double value = 0.20;
            String type = "Glow Stick";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Glow Stick Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            warnings.sessionNotStarted().show();
        }
    }
    //Adds foam glow sticks to sales log
    public void addFoamGlowstickSale(ActionEvent event) throws SQLException {
        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            double value = 2.00;
            String type = "Foam Glow Stick";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Foam Glow Stick Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            warnings.sessionNotStarted().show();
        }
    }

    //Add skate laces to extra sales log
    public void addSkateLacesSale(ActionEvent event) throws SQLException {
        double value = 3.00;
        String type = "Skate Laces";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add Skate Laces Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            warnings.sessionNotStarted().show();
        }
    }

    //Add seasonal sale to extra sales log
    public void addSeasonalSale(ActionEvent event) throws SQLException {
        double value = 1.00;
        String type = "Seasonal Sale";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add Seasonal Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            warnings.sessionNotStarted().show();
        }
    }

    //Add item replacement to sales log
    public void addItemReplacement(ActionEvent event) throws SQLException {
        double value = 0.00;
        String type = "Replacement";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add ItemReplacement");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            warnings.sessionNotStarted().show();
        }
    }

    //Add log of free promotion to extra sales
    public void addFreePromotion(ActionEvent event) throws SQLException {
        double value = 0.00;
        String type = "Free Promotion";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add Free Promotion");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());
            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            warnings.sessionNotStarted().show();
        }
    }
}