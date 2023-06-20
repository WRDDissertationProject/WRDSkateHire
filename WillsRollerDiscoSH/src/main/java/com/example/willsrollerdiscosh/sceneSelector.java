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

public class sceneSelector {
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

    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        reloader.sessionStartReloader();
    }

    public static boolean sessionChecker() throws SQLException {return DBConnect.checkForSessionStart();}

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

    public void switchToSkateHire(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skateHire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        reloader.SkateHireReloader(scene);
        reloader.sessionStartReloader();
    }

    public void switchToTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        reloader.TicketsReloader(scene);
        reloader.sessionStartReloader();
    }

    public void switchToCreateTicket(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("createTicket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        reloader.sessionStartReloader();
    }

    public void switchToEditOrDeleteTicket(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("editOrDeleteTicket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        reloader.EditOrDeleteTicketsReloader(scene);
        reloader.sessionStartReloader();
    }

    public void switchToMaintenance(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        reloader.maintenanceReloader(scene);
        reloader.sessionStartReloader();
    }

    @FXML
    public void switchToAddMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("createMaintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        setMaintenanceTypeChoiceBox();
        setSkateSizeChoiceBox();
        reloader.sessionStartReloader();
    }

    @FXML
    public void switchToExtraSales(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("extraSales.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        reloader.sessionStartReloader();
    }

    public static void checkForRecord(int count) {
        if (count > 0) {
            System.out.println("Session Currently Running");

        } else {
            System.out.println("Session Stopped");

        }
    }

    public void lockSkateHire() {
        System.out.println("Locked");
        this.lockButton.setVisible(false);
        skateHireLocked = true;
        this.unlockButton.setVisible(true);
    }

    public void unlockSkateHire() {
        System.out.println("unlocked");
        this.unlockButton.setVisible(false);
        skateHireLocked = false;
        this.lockButton.setVisible(true);
    }

    public static ObservableList<Skate> loadSkateHire(ListView lv){
        resourceName = "skateHire";
        ObservableList<Skate> data = DBConnect.loadSkates();
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

    public void createTicket() throws SQLException {
        resourceName = "tickets";
        if (ticketsTextBox.getText().isEmpty()) {
            warnings.alertEmptyBox().show();
        } else {
            locks.lock(resourceName, lockedBy);
            String ticketText = ticketsTextBox.getText();
            System.out.println("Text Found");
            DBConnect insertTicket = new DBConnect();
            DBConnect.insertTicket(ticketText);
            ticketsTextBox.clear();
            locks.unlock(resourceName,lockedBy);
        }
    }

    public static void deleteFunction(String item){
        System.out.println("Delete Clicked");
        resourceName = "tickets";
        try {
            locks.lock(resourceName, lockedBy);
            String ticket = item.substring(8);
            boolean success = DBConnect.deleteTicket(ticket);

            if (success) {
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListViewEditOrDelete");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadTicketEditOrDeleteListView(lv);
                    System.out.println("Tickets Reload Delete");
                }
            }locks.unlock(resourceName,lockedBy);
        }
        catch(Exception e) {
            warnings.deleteNotComplete().show();
        }
    }

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

    public void createMaintenanceSubmit() {
        System.out.println("Clicked");

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        ChoiceBox<String> skateSize = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        TextArea maintenanceDetails = (TextArea) scene.lookup("#maintenanceDetails");

        //if null
        if (choiceBox == null || choiceBox.getValue() == null || choiceBox.getValue().isEmpty() || maintenanceDetails == null || maintenanceDetails.getText().isEmpty()) {
            warnings.maintenanceEmpty().show();
        } else {
            try {
                resourceName = "maintenance";
                locks.lock(resourceName, lockedBy);
                String typeIn = choiceBox.getValue();
                String details = maintenanceDetails.getText();
                String skateSizeIn = skateSize.getValue();
                DBConnect.insertMaintenance(typeIn, details, skateSizeIn);

                maintenanceDetails.clear();
                locks.unlock(resourceName, lockedBy);
            } catch (SQLException e) {
                System.out.println("Couldn't Add Maintenance Record");
                throw new RuntimeException(e);

            }

        }
    }

    public void showSalesLabel(Parent root) {
        Label salesLabel= (Label) root.lookup("#salesStatus");
        salesLabel.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> Platform.runLater(() -> salesLabel.setVisible(false)), 2, TimeUnit.SECONDS);
    }

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