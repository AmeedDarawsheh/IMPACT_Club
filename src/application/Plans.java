package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Plans {
    @FXML
    private Button januaryButton;
    @FXML
    private Button februaryButton;
    @FXML
    private Button marchButton;
    @FXML
    private Button aprilButton;
    @FXML
    private Button mayButton;
    @FXML
    private Button juneButton;
    @FXML
    private Button julyButton;
    @FXML
    private Button augustButton;
    @FXML
    private Button septemberButton;
    @FXML
    private Button octoberButton;
    @FXML
    private Button novemberButton;
    @FXML
    private Button decemberButton;

   
    public void initialize() {
        januaryButton.setOnAction(event -> openMonthPlan("January", 1));
        februaryButton.setOnAction(event -> openMonthPlan("February", 2));
        marchButton.setOnAction(event -> openMonthPlan("March", 3));
        aprilButton.setOnAction(event -> openMonthPlan("April", 4));
        mayButton.setOnAction(event -> openMonthPlan("May", 5));
        juneButton.setOnAction(event -> openMonthPlan("June", 6));
        julyButton.setOnAction(event -> openMonthPlan("July", 7));
        augustButton.setOnAction(event -> openMonthPlan("August", 8));
        septemberButton.setOnAction(event -> openMonthPlan("September", 9));
        octoberButton.setOnAction(event -> openMonthPlan("October", 10));
        novemberButton.setOnAction(event -> openMonthPlan("November", 11));
        decemberButton.setOnAction(event -> openMonthPlan("December", 12));
    }

    private void openMonthPlan(String month, int monthNumber) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MonthPlan.fxml"));
            Parent root = loader.load();

            
            MonthPlanController controller = loader.getController();
            if (controller != null) {
                controller.setMonth(monthNumber, 2024); // 
                controller.setMonthName(month);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setTitle(month + " Plan");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
