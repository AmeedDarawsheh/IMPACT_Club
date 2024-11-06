package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MembersController {

    @FXML
    private TableView<Person> tableView;

    @FXML
    private TableColumn<Person, String> idCol;

    @FXML
    private TableColumn<Person, String> nameCol;

    @FXML
    private TableColumn<Person, String> addressCol;

    @FXML
    private TableColumn<Person, String> memberSinceCol;

    @FXML
    private TableColumn<Person, String> dobCol;

    @FXML
    private TableColumn<Person, String> emailCol;

    @FXML
    private TableColumn<Person, String> phoneCol;

    @FXML
    private TableColumn<Person, Integer> pointsCol;

    @FXML
    private TableColumn<Person, String> genderCol;

    private ObservableList<Person> Data;

    public void initialize() {
        Data = FXCollections.observableArrayList();

        // Set up columns to map to Person properties
        idCol.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        memberSinceCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("bod"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        System.out.println("MembersController initialized");

        // Load data from the database
        loadDataFromDatabase();
        tableView.setItems(Data); // Set the data
        tableView.refresh();
    }

    private void loadDataFromDatabase() {
     DatabaseConnection database =new DatabaseConnection();

        String query = "SELECT p.ssn, p.first_name, p.middle_name, p.last_name, p.street, p.city, "
                     + "p.start_date, p.bod, p.user_name, p.phone_number, p.gender, m.points "
                     + "FROM \"IMPACT Club\".person p JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String ssn = rs.getString("ssn");
                
                String fullName = rs.getString("first_name") + " " + rs.getString("middle_name") + " " + rs.getString("last_name");
                System.out.print(fullName); String address = rs.getString("street") + ", " + rs.getString("city");
                String startDate = rs.getString("start_date");
                String bod = rs.getString("bod");
                String email = rs.getString("user_name");
                String phoneNumber = rs.getString("phone_number");
                String gender = rs.getString("gender");
                int points = rs.getInt("points");

                Data.add(new Person(ssn, fullName, address, startDate, bod, email, phoneNumber, gender, points));
            }
            tableView.setItems(Data); // Set the data
            tableView.refresh();
            

          

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Inner class to represent each member
    public static class Person {
        private String ssn;
        private String fullName;
        private String address;
        private String startDate;
        private String bod;
        private String userName;
        private String phoneNumber;
        private String gender;
        private int points;

        public Person(String ssn, String fullName, String address, String startDate, String bod, String userName, String phoneNumber, String gender, int points) {
            this.ssn = ssn;
            this.fullName = fullName;
            this.address = address;
            this.startDate = startDate;
            this.bod = bod;
            this.userName = userName;
            this.phoneNumber = phoneNumber;
            this.gender = gender;
            this.points = points;
        }

        // Getters for each property
        public String getSsn() { return ssn; }
        public String getFullName() { return fullName; }
        public String getAddress() { return address; }
        public String getStartDate() { return startDate; }
        public String getBod() { return bod; }
        public String getUserName() { return userName; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getGender() { return gender; }
        public int getPoints() { return points; }
    }
}
