package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class EditMemController {
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
    private TextField pointsField;
    @FXML
    private RadioButton maleRadio;
    @FXML
    private RadioButton femaleRadio;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Person member;
    private DatabaseConnection data = new DatabaseConnection();

    // Called by the parent controller to pass the data for the selected member
    public void setMemberData(Person member) {
        this.member = member;

        // Populate the form fields with the selected member's data
        ssnField.setText(member.getSsn());
        fullNameField.setText(member.getFullName() );
        addressField.setText(member.getAddress());
        emailField.setText(member.getUserName());
        phoneField.setText(member.getPhoneNumber());
        pointsField.setText(String.valueOf(member.getPoints()));
        try {
            startDateField.setValue(LocalDate.parse(member.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }

        // Parse and set the birth date
        try {
            birthDateField.setValue(LocalDate.parse(member.getBod(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        // Set gender radio button
        if (member.getGender().equalsIgnoreCase("M")) {
            maleRadio.setSelected(true);
        } else {
            femaleRadio.setSelected(true);
        }
    }

    @FXML
    private void initialize() {
        // Group radio buttons
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);

        // Save button action
        saveButton.setOnAction(event -> {
            saveMemberData();
            closeWindow();
        });

        // Cancel button action
        cancelButton.setOnAction(event -> closeWindow());
    }

    private void saveMemberData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Read and set new values from input fields to the Person object
        String newStartDate = startDateField.getValue().format(formatter);
        String newBirthDate = birthDateField.getValue().format(formatter);
        String newEmail = emailField.getText();
        String newPhoneNumber = phoneField.getText();
        int newPoints = Integer.parseInt(pointsField.getText());
        String newGender = maleRadio.isSelected() ? "M" : "F";

        // Debugging output to verify the new values are read correctly
        System.out.println("New Start Date: " + newStartDate);
        System.out.println("New Birth Date: " + newBirthDate);
        System.out.println("New Email: " + newEmail);
        System.out.println("New Phone Number: " + newPhoneNumber);
        System.out.println("New Points: " + newPoints);
        System.out.println("New Gender: " + newGender);

        member.setStartDate(newStartDate);
        member.setBod(newBirthDate);
        member.setUserName(newEmail);
        member.setPhoneNumber(newPhoneNumber);
        member.setPoints(newPoints);
        member.setGender(newGender);

        String updatePersonSQL = "UPDATE \"IMPACT Club\".person SET first_name = ?, middle_name = ?, last_name = ?, " +
                                 "gender = ?, bod = ?, phone_number = ?, user_name = ?, start_date = ?, street = ?, city = ? " +
                                 "WHERE ssn = ?";
        String updateMemberSQL = "UPDATE \"IMPACT Club\".member SET points = ? WHERE ssn = ?";

        try (Connection conn = data.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement personStmt = conn.prepareStatement(updatePersonSQL);
                 PreparedStatement memberStmt = conn.prepareStatement(updateMemberSQL)) {

                String[] names = fullNameField.getText().split(" ");
                String firstName = names[0];
                String middleName = names.length > 1 ? names[1] : "";
                String lastName = names.length > 2 ? names[2] : "";

                // Update person table
                personStmt.setString(1, firstName);
                personStmt.setString(2, middleName);
                personStmt.setString(3, lastName);
                personStmt.setString(4, member.getGender());
                personStmt.setDate(5, java.sql.Date.valueOf(member.getBod()));
                personStmt.setString(6, member.getPhoneNumber());
                personStmt.setString(7, member.getUserName());
                personStmt.setDate(8, java.sql.Date.valueOf(member.getStartDate()));

                String[] addressParts = member.getAddress().split(", ");
                String street = addressParts.length > 0 ? addressParts[0] : "";
                String city = addressParts.length > 1 ? addressParts[1] : "";

                personStmt.setString(9, street);
                personStmt.setString(10, city);
                personStmt.setString(11, member.getSsn());

                int personUpdatedRows = personStmt.executeUpdate();
                System.out.println("Person table rows updated: " + personUpdatedRows);

                // Update member table
                memberStmt.setInt(1, member.getPoints());
                memberStmt.setString(2, member.getSsn());

                int memberUpdatedRows = memberStmt.executeUpdate();
                System.out.println("Member table rows updated: " + memberUpdatedRows);

                conn.commit();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Member data updated successfully.");
                alert.showAndWait();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
