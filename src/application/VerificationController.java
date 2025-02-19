package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VerificationController {
    @FXML
    private TextField verificationCodeField;

    private int verificationCode;
    private String userEmail;

   
    public void setVerificationCode(int code,String email) {
        this.verificationCode = code;
        this.userEmail = email;
    }

    @FXML
    private void handleVerifyCode(ActionEvent event) {
        String enteredCode = verificationCodeField.getText().trim();

        if (enteredCode.isEmpty()) {
            showAlert("Error", "Please enter the verification code.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int enteredCodeInt = Integer.parseInt(enteredCode);
            System.out.print(enteredCodeInt);
            if (enteredCodeInt == verificationCode) {
                showAlert("Success", "Verification successful!", Alert.AlertType.INFORMATION);
                openEnterPasswordWindow(event);
              
            } else {
                showAlert("Error", "Incorrect verification code. Please try again.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid code format. Please enter a valid number.", Alert.AlertType.ERROR);
        }
    }
    private void openEnterPasswordWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("enterPassword.fxml"));
            Parent root = loader.load();

            EnterPasswordController enterPasswordController = loader.getController();
            enterPasswordController.setUserEmail(userEmail);

            Stage stage = new Stage();
            stage.setTitle("Enter New Password");
            stage.setScene(new Scene(root));
            stage.show();

        
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
