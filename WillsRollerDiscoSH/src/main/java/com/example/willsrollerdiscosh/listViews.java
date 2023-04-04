package com.example.willsrollerdiscosh;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.List;

import static com.example.willsrollerdiscosh.sceneSelector.*;

public class listViews {
    public static void loadAnnouncementsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadAnnouncement();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }
    /*Before
    *List<String> announcementsData = DBConnect.loadAnnouncement();
                        ObservableList<String> items = FXCollections.observableArrayList(announcementsData);
                        ListView lv = (ListView) scene.lookup("#announcementsList");
                        lv.setItems(items);
    * After
    *
    * ListView lv = (ListView) scene.lookup("#announcementsList");
                        listViews.loadAnnouncementsListView(lv);*/


    public static void loadTicketsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTickets();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    public static void loadSkateHireListView(ListView lv) throws SQLException {
        ObservableList<Skate> data = sceneSelector.loadSkateHire(lv);
        lv.setItems(data);
    }



}
