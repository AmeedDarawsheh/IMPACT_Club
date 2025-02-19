package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectController {
    @FXML
    private GridPane projectGrid;
    @FXML
    private ScrollPane scroll;
    private Map<String, String> projectMap = new HashMap<>();

    @FXML
    public void initialize() {
        
        //scroll.setFitToWidth(true);
        //scroll.setFitToHeight(true);
        projectGrid.getChildren().clear();
        projectGrid.setHgap(100);
        projectGrid.setVgap(100);

        Project projectDAO = new Project();
        List<String[]> projects = projectDAO.getProjectList();

        int columns = 3;
        int row = 0;

        for (int i = 0; i < projects.size(); i++) {
            String projectId = projects.get(i)[0];
            String projectName = projects.get(i)[1];

            projectMap.put(projectName, projectId); // Store the project ID

            Button projectButton = new Button(projectName);
            projectButton.setStyle("-fx-background-color: #20c997; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 25; -fx-padding: 20 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 1);");

            projectButton.setOnMouseEntered(e -> projectButton.setStyle("-fx-background-color: #1c7430; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 25; -fx-padding: 20 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 1);"));
            projectButton.setOnMouseExited(e -> projectButton.setStyle("-fx-background-color: #20c997; -fx-text-fill: white; -fx-font-size: 20; -fx-background-radius: 25; -fx-padding: 20 30; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 1);"));
            projectButton.setOnAction(e -> openProjectDetailWindow(projectName));

            projectGrid.add(projectButton, i % columns, row);
            GridPane.setMargin(projectButton, new Insets(100,0,0,0));

            if (i % columns == columns - 1) {
                row++;
            }
        }
       // projectGrid.setMinHeight(Region.USE_PREF_SIZE);
        //projectGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        //projectGrid.setMaxHeight(Region.USE_PREF_SIZE);
        // Ensure GridPane resizes based on its content
       projectGrid.setMinWidth(Region.USE_PREF_SIZE);
        projectGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        projectGrid.setMaxWidth(Region.USE_PREF_SIZE);
        projectGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
        projectGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        projectGrid.setMaxHeight(Region.USE_PREF_SIZE);
    }

    private void openProjectDetailWindow(String projectName) {
        try {
            String projectId = projectMap.get(projectName); 

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProjectDetails.fxml"));
            Parent root = loader.load();

            ProjectDetailController controller = loader.getController();
            controller.setProjectId(Integer.parseInt(projectId));

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







