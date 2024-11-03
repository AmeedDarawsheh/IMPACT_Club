package application;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class EnterPasswordController {
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private String userEmail; // The email of the user whose password needs to be updated

    // Setter to receive the user email
    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    private void handleSubmitNewPassword() {
        String newPassword = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in both password fields.", Alert.AlertType.ERROR);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match. Please try again.", Alert.AlertType.ERROR);
            return;
        }

        // Update the password in the database
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = "UPDATE \"IMPACT Club\".person SET password = ? WHERE user_name = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, userEmail);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert("Success", "Password updated successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "An error occurred while updating the password. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while updating the password.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
