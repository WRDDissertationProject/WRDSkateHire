package com.example.willsrollerdiscosh;

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
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

public class sceneSelector {
    @FXML
    Button homeButton, lockButton, unlockButton;
    private Stage stage;
    private static Scene scene;
    private Parent root;

    @FXML
    TextArea ticketsTextBox;

    @FXML
    static
    Label sessionStatus;

    boolean skateHireLocked = false;

    static String resourceName;
    static String lockedBy = "SkateHireApp";

    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSkateHire(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skateHire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SkateHireReloader();
    }

    public void switchToTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        TicketsReloader();
    }

    public void switchToCreateTicket(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("createTicket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void checkForRecord(int count) {
        Button skateHire = (Button) scene.lookup("#skateHireButton");
        Button tickets = (Button) scene.lookup("#ticketsButton");
        Button maintenance = (Button) scene.lookup("#maintenanceButton");

        if (count > 0) {
            System.out.println("Session Currently Running");

            //skateHire.setDisable(false);
            // tickets.setDisable(false);
            // maintenance.setDisable(false);
        } else {
            System.out.println("Session Stopped");
            //Label session = (Label) scene.lookup("#sessionStatus");
            //session.setText("Session Stopped");

            //skateHire.setDisable(true);
            //tickets.setDisable(true);
            //maintenance.setDisable(true);
        }
    }

    public static void recordExists() {
        //Label session = (Label) scene.lookup("#sessionStatus");
        //session.setText("Session Currently Running");
        System.out.println("Session Currently Running");
    }

    public static void recordDoesNotExist() {
        //Label session = (Label) scene.lookup("#sessionStatus");

        Button skateHire = (Button) scene.lookup("#skateHireButton");
        skateHire.setDisable(true);
    }

    public void lockSkateHire(ActionEvent event) throws IOException {
        System.out.println("Locked");
        this.lockButton.setVisible(false);
        skateHireLocked = true;
        this.unlockButton.setVisible(true);
    }

    public void unlockSkateHire(ActionEvent event) throws IOException {
        System.out.println("unlocked");
        this.unlockButton.setVisible(false);
        skateHireLocked = false;
        this.lockButton.setVisible(true);
    }

    public static ObservableList<Skate> loadSkateHire(ListView lv) throws SQLException{
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
                                if (compareToMaxValue(currentValue, item.getSkateSize()) == false) {

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
                                } else {
                                    //errors.minusSkates();
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


    public void SkateHireReloader() {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHListView");
                        if (lv != null) {
                            listViews.loadSkateHireListView(lv);
                            System.out.println("Skate Hire Reload");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 800);

    }

    public void TicketsReloader() {
        Timer reloadTickets = new Timer();
        reloadTickets.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                    if (lv != null) {
                        try {
                            listViews.loadTicketsListView(lv);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Ticket Reload");
                    }
                });
            }
        }, 0, 800);

    }

    public static boolean compareToMaxValue(int currentValue, String skateSize) throws SQLException {
        int newValue = currentValue + 1;
        int currentMax = DBConnect.checkSkatesForMax(newValue, skateSize);

        if(currentValue == currentMax) {
         System.out.println("Values are Equal, cannot add anymore skates");
         //warnings.alertSameAmountOfSkates();

         return true;
        }
        else if (newValue > currentMax) {
            System.out.println("Error, Skate Count is No Longer Accurate");
            return true;
        }
        return false;

    }

    public void createTicket(ActionEvent event) throws IOException, SQLException {
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
}




//add / remove skates
//max number = max of skates

//if add skate = < max Number
//error can't be added