package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
public class AboutController {
    private Connection databaseConnection; 

    @FXML
    private Button goalsButton;
    @FXML
    private Label objectivesLabel;
    @FXML
    private Button descriptionButton;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button statusButton;
    @FXML
    private RadioButton completedRadioButton;
    @FXML
    private RadioButton inProgressRadioButton;
    @FXML
    private ToggleGroup statusGroup;
    @FXML
    private Button editButton,saveButton;
   private int projectId;
   private TextField objectivesTextField = new TextField();
   private TextField descriptionTextField = new TextField();

    public void setProjectId(int projectId) {
       this.projectId = projectId;
    }

    public void initialize() {
    	completedRadioButton.setVisible(false);
        inProgressRadioButton.setVisible(false);
        // Adding hover effect
        goalsButton.setOnMouseEntered(event -> {
            goalsButton.setStyle("-fx-background-color: #d1e7dd; -fx-cursor: hand;"); // Change background color on hover
        });

        goalsButton.setOnMouseExited(event -> {
            goalsButton.setStyle("-fx-background-color: #6c757d;"); 
        });

        
        goalsButton.setOnAction(event -> {
            
            moveLabel(objectivesLabel);
           
            displayGoalsFromDatabase();
        });
        descriptionButton.setOnMouseEntered(event -> {
            descriptionButton.setStyle("-fx-background-color: #d1e7dd; -fx-cursor: hand;"); 
        });

        descriptionButton.setOnMouseExited(event -> {
            descriptionButton.setStyle("-fx-background-color: #6c757d;"); 
        });

        
        descriptionButton.setOnAction(event -> {
            
            moveLabel(descriptionLabel);
           
            displaydescriptionFromDatabase();
        });
        statusButton.setOnMouseEntered(event -> {
            statusButton.setStyle("-fx-background-color: #d1e7dd; -fx-cursor: hand;"); 
        });

        statusButton.setOnMouseExited(event -> {
           statusButton.setStyle("-fx-background-color: #6c757d;"); 
        });
        statusButton.setOnAction(event -> {
        	  completedRadioButton.setVisible(true);
              inProgressRadioButton.setVisible(true);
            displayStatusFromDatabase();
            movetoggle(statusGroup);
        });
editButton.setOnAction(event -> enableEditing());
        
        
        saveButton.setOnAction(event -> saveData());
    }

    private void moveLabel(Label label) {
        
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), label);
        transition.setByX(50);
        transition.play();
    }
    private void movetoggle(ToggleGroup toggleGroup) {
       
        for (Toggle toggle : toggleGroup.getToggles()) {
            if (toggle instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) toggle;

            
                TranslateTransition transition = new TranslateTransition(Duration.millis(500), radioButton);
                transition.setByX(50); 
                transition.play();
            }
        }
    }
    private void displayGoalsFromDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = "SELECT objectives FROM \"IMPACT Club\".project WHERE projectid = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String objectives = resultSet.getString("objectives");
                objectivesLabel.setText(objectives != null ? objectives : "No objectives available.");
            } else {
                objectivesLabel.setText("Project not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            objectivesLabel.setText("Error retrieving objectives.");
        }
    }
    private void displaydescriptionFromDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = "SELECT description FROM \"IMPACT Club\".project WHERE projectid = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String description = resultSet.getString("description");
                descriptionLabel.setText(description != null ? description: "No description available.");
            } else {
                descriptionLabel.setText("Project not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            descriptionLabel.setText("Error retrieving objectives.");
        }
    }
    private void displayStatusFromDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = "SELECT projectstatus FROM \"IMPACT Club\".project WHERE projectid = ?";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("projectstatus");
                if ("Completed".equalsIgnoreCase(status)) {
                    completedRadioButton.setSelected(true);
                } else if ("In Progress".equalsIgnoreCase(status)) {
                    inProgressRadioButton.setSelected(true);
                } else {
                    statusGroup.selectToggle(null); 
                }
            } else {
                statusGroup.selectToggle(null);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            statusGroup.selectToggle(null); 
        }
    }
   
    private void enableEditing() {
       
        objectivesTextField.setText(objectivesLabel.getText());
        objectivesTextField.setLayoutX(objectivesLabel.getLayoutX());
        objectivesTextField.setLayoutY(objectivesLabel.getLayoutY());
        objectivesTextField.setPrefWidth(objectivesLabel.getPrefWidth());
        objectivesTextField.setPrefHeight(objectivesLabel.getPrefHeight());

        descriptionTextField.setText(descriptionLabel.getText());
        descriptionTextField.setLayoutX(descriptionLabel.getLayoutX());
        descriptionTextField.setLayoutY(descriptionLabel.getLayoutY());
        descriptionTextField.setPrefWidth(descriptionLabel.getPrefWidth());
        descriptionTextField.setPrefHeight(descriptionLabel.getPrefHeight());

       
        objectivesTextField.setEditable(true);
        descriptionTextField.setEditable(true);

       
        if (!((AnchorPane) objectivesLabel.getParent()).getChildren().contains(objectivesTextField)) {
            ((AnchorPane) objectivesLabel.getParent()).getChildren().add(objectivesTextField);
        }
        if (!((AnchorPane) descriptionLabel.getParent()).getChildren().contains(descriptionTextField)) {
            ((AnchorPane) descriptionLabel.getParent()).getChildren().add(descriptionTextField);
        }

        objectivesLabel.setVisible(false);
        descriptionLabel.setVisible(false);
    }


    private void saveData() {
        String updatedObjectives = objectivesTextField.getText();
        String updatedDescription = descriptionTextField.getText();
        String updatedStatus = completedRadioButton.isSelected() ? "Completed" : "In Progress";

        String query = "UPDATE \"IMPACT Club\".project SET objectives = ?, description = ?, projectstatus = ?::\"IMPACT Club\".projectstatustype WHERE projectid = ?";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, updatedObjectives);
            preparedStatement.setString(2, updatedDescription);
            preparedStatement.setString(3, updatedStatus);
            preparedStatement.setInt(4, projectId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
               
                objectivesLabel.setText(updatedObjectives);
                descriptionLabel.setText(updatedDescription);

                ((AnchorPane) objectivesTextField.getParent()).getChildren().remove(objectivesTextField);
                ((AnchorPane) descriptionTextField.getParent()).getChildren().remove(descriptionTextField);
                objectivesLabel.setVisible(true);
                descriptionLabel.setVisible(true);

                System.out.println("Data updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
