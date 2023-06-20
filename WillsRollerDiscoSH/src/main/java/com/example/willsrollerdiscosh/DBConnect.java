/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoSH
 *  FILE TITLE: DBConnect.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *     All the code that relates to database manipulation, including the database connection as well as creates, inserts
 *     ,updates and deletes.
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnect {
    private static final Logger log = Logger.getLogger(String.valueOf(DBConnect.class));
    String url = "jdbc:mysql://localhost:3306/wrdDatabase";
    String username = "root";
    String password = "root";

    static Connection connection = null;

    static ResultSet rs;

    public void connect() {
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Class Not Found");
            }
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Connection)");
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Database Connected");
        }
    }

    public void sessionStartChecker() {
        Timer reloadSessionChecker = new Timer();
        reloadSessionChecker.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Statement statement;
                    try {
                        statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM current_session");
                        resultSet.next();
                        int count = resultSet.getInt(1);
                        boolean recordExists;
                        sceneSelector.checkForRecord(count);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 2000, 2000);
    }

    public static List<String> loadAnnouncement() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT announcement_details FROM announcements");
        List<String> announcementsList = new ArrayList<>();

        while (resultSet.next()) {
            String announcement = resultSet.getString("announcement_details");
            announcementsList.add(announcement);
        }
        return announcementsList;
    }

    public static ObservableList<Skate> loadSkates() {
        ObservableList<Skate> data = FXCollections.observableArrayList();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT skateSize, skateAmount FROM current_skates");

            // iterate through the result set and add Skate objects to the ObservableList
            while (rs.next()) {
                String size = rs.getString("skateSize");
                int amount = rs.getInt("skateAmount");
                data.add(new Skate(size, amount));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public static void updateSkateListPlus(int newSkateAmount, String skateSize) throws SQLException {
        //System.out.println(newSkateAmount + "Test");
        //System.out.println(skateSize + " Test");
        //update value by inserting into database
        Statement stmt = connection.createStatement();
        String sql = "UPDATE current_skates SET skateAmount = " + newSkateAmount + " WHERE skateSize = '" + skateSize + "'";

        stmt.executeUpdate(sql);
        System.out.println("Updated Skate Amount");
    }

    public static int checkSkatesForMax(String skateSize) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT skateAmount FROM skate_inventory WHERE skateSize = '" + skateSize + "'");
        int amount = 0;
        if (rs.next()) {
            amount = rs.getInt("skateAmount");
        }
        System.out.println("Maximum Amount" + amount);
        return amount;
    }

    public static List<String> loadTickets() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        while (resultSet.next()) {
            String ticket_details = resultSet.getString("ticket_details");
            String dateCreated = resultSet.getString("ticket_date");
            String timeCreated = resultSet.getString("ticket_time");
            String postedBy = resultSet.getString("staff_id");
            ticketsList.add("Ticket: " + ticket_details + " \nPosted By: " + postedBy + "\n"
                    + "Date: " + dateCreated + " Time: " + timeCreated);
        }
        return ticketsList;
    }

    public static List<String> loadTicketsEditOrDelete() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        while (resultSet.next()) {
            String ticket_details = resultSet.getString("ticket_details");
            ticketsList.add("Ticket: " + ticket_details);
        }
        return ticketsList;
    }

    public static void insertTicket(String text) {
        //insert into query
        String ticketDate = dateTime.justDate();
        String ticketTime = dateTime.justTime();
        String postedBy = "Skate Hire App";
        try {
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO tickets(ticket_date, ticket_time, ticket_details, staff_id) VALUES(?, ?, ?, ?)");

            pstmt.setString(1, ticketDate);
            pstmt.setString(2, ticketTime);
            pstmt.setString(3, text);
            pstmt.setString(4, postedBy);

            pstmt.executeUpdate();

            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            log.log(Level.SEVERE,"Announcement not inserted", e);
        }
    }

    public static boolean deleteTicket(String value) {
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM tickets WHERE ticket_details = '" + value + "'";
            System.out.println(query);
            stmt.executeUpdate(query);
            success = true;
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Ticket Nor Deleted", e);
            success = false;
        }
        return success;
    }

    public static List<String> loadMaintenance() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM maintenance");
        List<String> maintenanceList = new ArrayList<>();

        while (resultSet.next()) {
            String record = resultSet.getString("maintenance_details");
            String type = resultSet.getString("maintenance_type");
            String skateSize = resultSet.getString("skateSize");

            if((skateSize == null) || (skateSize.equals("null"))){
                skateSize = "N/A";
            }
            maintenanceList.add(
                    "Details: " + record + "\nType: " + type + "\nSkate Size: " +skateSize);
        }
        return maintenanceList;
    }

    public static void insertMaintenance(String typeIn, String details, String skateSizeIn) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO maintenance(maintenance_type, maintenance_details, skateSize)" +
                    " VALUES('" + typeIn + "', '" + details + "', '" + skateSizeIn + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount - 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount - 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Announcement not inserted", e);
        }
    }

    public static void updateSkateSizeAmount(String skateSize, int newAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE current_skates SET skateAmount = '" + newAmount + "' WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Skate inventory not updated", e);
        }
    }

    public static void updateSkateInventory(String skateSize, int newAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE skate_inventory SET skateAmount = " + newAmount + " WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Skate inventory not updated", e);
        }
    }

    public static int fetchSkateSizeCurrent(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM skate_inventory WHERE skateSize = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, skateSize);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static int fetchSkateSizeAmount(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM current_skates WHERE skateSize = '" + skateSize + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean checkForSessionStart() throws SQLException {
        String query = "SELECT * FROM current_session";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.next();
    }
    public static String getSessionStartTime() {
        String query = "SELECT current_dateTime FROM current_session";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("current_dateTime");
            } else {
                return "Session not started";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addExtraPurchase(Double purchaseCost) throws SQLException {
        //fetch current session
        String currentSessionQuery = "SELECT * FROM current_session";
        Statement selectStmt = connection.createStatement();
        ResultSet rs = selectStmt.executeQuery(currentSessionQuery);

        int extrasSoldAmountFetch = 0;
        double extrasSoldTotalFetch =0.00;
        String currentSession = " ";

        while (rs.next()) {
            currentSession = rs.getString("current_dateTime");
            extrasSoldAmountFetch = rs.getInt("Current_Extras_sold_amount");
            extrasSoldTotalFetch = rs.getDouble("Current_Extras_sold_total");
        }

        int extrasSoldAmount = extrasSoldAmountFetch + 1;
        double extrasSoldTotal = extrasSoldTotalFetch + purchaseCost;

        String updateQuery = "UPDATE current_session SET Current_Extras_sold_amount = ?, " +
                "Current_Extras_sold_total = ? WHERE current_dateTime = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setInt(1, extrasSoldAmount);
        updateStmt.setDouble(2, extrasSoldTotal);
        updateStmt.setString(3, currentSession);

        updateStmt.executeUpdate();
    }

    public static void addTransaction(String session, String type, String time, Double value) throws SQLException {
        System.out.println("Transaction Added Test");
        Statement stmt = connection.createStatement();
        String sql = "INSERT INTO transaction_history(session_dateTime, transaction_type, transaction_time, " +
                "transaction_value)" +
                " VALUES('" + session + "', '" + type + "', '" + time + "', '" + value + "')";
        stmt.executeUpdate(sql);
        System.out.println("Inserted Into Database");
    }
}
