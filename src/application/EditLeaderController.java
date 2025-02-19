package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class EditLeaderController {
    @FXML
    private TextField ssnField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField addressField;
    @FXML
    private DatePicker startDateField;
    @FXML
    private DatePicker birthDateField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField majorField; 
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Leader leader;
    private DatabaseConnection data = new DatabaseConnection();

    public void setLeaderData(Leader leader) {
        this.leader = leader;

       
        ssnField.setText(leader.getSsn());
        fullNameField.setText(leader.getFullName());
        addressField.setText(leader.getAddress());
        emailField.setText(leader.getUserName());
        phoneField.setText(leader.getPhoneNumber());
        majorField.setText(leader.getMajor()); 

        try {
            startDateField.setValue(LocalDate.parse(leader.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        try {
            birthDateField.setValue(LocalDate.parse(leader.getBod(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        if (leader.getGender().equalsIgnoreCase("M")) {
            maleRadio.setSelected(true);
        } else {
            femaleRadio.setSelected(true);
        }
    }

    @FXML
    private void initialize() {
     
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);

    
        saveButton.setOnAction(event -> {
            saveLeaderData();
            closeWindow();
        });

       
        cancelButton.setOnAction(event -> closeWindow());
    }

    private void saveLeaderData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String newStartDate = startDateField.getValue().format(formatter);
        String newBirthDate = birthDateField.getValue().format(formatter);
        String newEmail = emailField.getText();
        String newPhoneNumber = phoneField.getText();
        String newMajor = majorField.getText(); 
        String newGender = maleRadio.isSelected() ? "M" : "F";

        System.out.println("New Start Date: " + newStartDate);
        System.out.println("New Birth Date: " + newBirthDate);
        System.out.println("New Email: " + newEmail);
        System.out.println("New Phone Number: " + newPhoneNumber);
        System.out.println("New Major: " + newMajor);
        System.out.println("New Gender: " + newGender);

        leader.setStartDate(newStartDate);
        leader.setBod(newBirthDate);
        leader.setUserName(newEmail);
        leader.setPhoneNumber(newPhoneNumber);
        leader.setMajor(newMajor); 
        leader.setGender(newGender);

        String updatePersonSQL = "UPDATE \"IMPACT Club\".person SET first_name = ?, middle_name = ?, last_name = ?, " +
                                 "gender = ?, bod = ?, phone_number = ?, user_name = ?, start_date = ?, street = ?, city = ? " +
                                 "WHERE ssn = ?";
        String updateLeaderSQL = "UPDATE \"IMPACT Club\".leader SET major = ? WHERE ssn = ?";

        try (Connection conn = data.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement personStmt = conn.prepareStatement(updatePersonSQL);
                 PreparedStatement leaderStmt = conn.prepareStatement(updateLeaderSQL)) {

                String[] names = fullNameField.getText().split(" ");
                String firstName = names[0];
                String middleName = names.length > 1 ? names[1] : "";
                String lastName = names.length > 2 ? names[2] : "";

             
                personStmt.setString(1, firstName);
                personStmt.setString(2, middleName);
                personStmt.setString(3, lastName);
                personStmt.setString(4, leader.getGender());
                personStmt.setDate(5, java.sql.Date.valueOf(leader.getBod()));
                personStmt.setString(6, leader.getPhoneNumber());
                personStmt.setString(7, leader.getUserName());
                personStmt.setDate(8, java.sql.Date.valueOf(leader.getStartDate()));

                String[] addressParts = leader.getAddress().split(", ");
                String street = addressParts.length > 0 ? addressParts[0] : "";
                String city = addressParts.length > 1 ? addressParts[1] : "";

                personStmt.setString(9, street);
                personStmt.setString(10, city);
                personStmt.setString(11, leader.getSsn());

                int personUpdatedRows = personStmt.executeUpdate();
                System.out.println("Person table rows updated: " + personUpdatedRows);

           
                leaderStmt.setString(1, leader.getMajor());
                leaderStmt.setString(2, leader.getSsn());

                int leaderUpdatedRows = leaderStmt.executeUpdate();
                System.out.println("Leader table rows updated: " + leaderUpdatedRows);

                conn.commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Leader data updated successfully.");
                alert.showAndWait();
            } catch (SQLException e) {
                conn.rollback();
               
            }
        } catch (SQLException e) {
         
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}