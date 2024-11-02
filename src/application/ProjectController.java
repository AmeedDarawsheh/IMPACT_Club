package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectController {
    @FXML
    private GridPane projectGrid;

    private Map<String, String> projectMap = new HashMap<>(); // Map to store project names and IDs

    @FXML
    public void initialize() {
        Project projectDAO = new Project();
        List<String[]> projects = projectDAO.getProjectList();

        int columns = 2;
        int row = 0;

        for (int i = 0; i < projects.size(); i++) {
            String projectId = projects.get(i)[0];
            String projectName = projects.get(i)[1];

            projectMap.put(projectName, projectId); // Store the project ID

            Button projectButton = new Button(projectName);
            projectButton.setStyle("-fx-background-color: #20c997; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 25; -fx-padding: 20 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 1);");

            projectButton.setOnMouseEntered(e -> projectButton.setStyle("-fx-background-color: #1c7430; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 25; -fx-padding: 20 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 1);"));
            projectButton.setOnMouseExited(e -> projectButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 25; -fx-padding: 20 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 1);"));
            projectButton.setOnAction(e -> openProjectDetailWindow(projectName));

            projectGrid.add(projectButton, i % columns, row);
            if (i % columns == columns - 1) {
                row++;
            }
        }
    }

    private void openProjectDetailWindow(String projectName) {
        try {
            String projectId = projectMap.get(projectName); // Get the project ID from the map

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectDetails.fxml"));
            Parent root = loader.load();

            ProjectDetailController controller = loader.getController();
            controller.setProjectId(Integer.parseInt(projectId)); // Pass the project ID to the detail controller

            Stage currentStage = (Stage) projectGrid.getScene().getWindow();
            Stage stage = new Stage();
            stage.setTitle("Project Details: " + projectName);
            stage.setScene(new Scene(root));
            currentStage.close();
            stage.show();
            stage.centerOnScreen();
            stage.setResizable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}







