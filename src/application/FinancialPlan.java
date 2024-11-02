package application;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FinancialPlan {
	private Connection databaseConnection; 
	 private TextField objectivesTextField = new TextField();
	   private TextField budgetTextField = new TextField();
	   private TextField expenditureTextField = new TextField();
	   private TextField fundingSourcesTextField = new TextField();
    @FXML
    private Button budgetButton,fundingSourcesButton,objectivesButton,expenditureButton,editButton,saveButton;
    @FXML
    private Label budgetLabel,expenditureLabel,fundingSourcesLabel,objectivesLabel;
	private int projectId;
    public void setProjectId(int projectId) {
        this.projectId = projectId;
     }

     public void initialize() {
     	
    	 budgetButton.setOnMouseEntered(event -> {
    		    DropShadow shadow = new DropShadow();
    		    shadow.setColor(Color.GRAY);
    		    shadow.setRadius(5);
    		    budgetButton.setEffect(shadow);
    		    budgetButton.setCursor(Cursor.HAND);
    		});

    		

         
         budgetButton.setOnAction(event -> {
             moveLabel(budgetLabel);
             displaybudjetFromDatabase();
         });
         expenditureButton.setOnMouseEntered(event -> {
 		    DropShadow shadow = new DropShadow();
 		    shadow.setColor(Color.GRAY);
 		    shadow.setRadius(5);
 		    budgetButton.setEffect(shadow);
 		    budgetButton.setCursor(Cursor.HAND);
 		});

 		

      
         expenditureButton.setOnAction(event -> {
         
		moveLabel(expenditureLabel);
          displayexpenditureFromDatabase();
      });
         fundingSourcesButton.setOnMouseEntered(event -> {
  		    DropShadow shadow = new DropShadow();
  		    shadow.setColor(Color.GRAY);
  		    shadow.setRadius(5);
  		    fundingSourcesButton.setEffect(shadow);
  		    fundingSourcesButton.setCursor(Cursor.HAND);
  		});

  		

       
          fundingSourcesButton.setOnAction(event -> {
          System.out.print("pppp");
 		moveLabel(fundingSourcesLabel);
           fundingSourcesFromDatabase();
       });
          objectivesButton.setOnMouseEntered(event -> {
    		    DropShadow shadow = new DropShadow();
    		    shadow.setColor(Color.GRAY);
    		    shadow.setRadius(5);
    		    objectivesButton.setEffect(shadow);
    		    objectivesButton.setCursor(Cursor.HAND);
    		});

    		

         
            objectivesButton.setOnAction(event -> {
   		moveLabel(objectivesLabel);
             objectivesFromDatabase();
         });
            editButton.setOnAction(event -> enableEditing());
            
          
            saveButton.setOnAction(event -> saveData());
     }

     private void moveLabel(Label label) {
         
         TranslateTransition transition = new TranslateTransition(Duration.millis(500), label);
         transition.setByX(50); 
         transition.play();
     }
    
     private void displaybudjetFromDatabase() {
         DatabaseConnection databaseConnection = new DatabaseConnection();
         String query = "SELECT budget FROM \"IMPACT Club\".projectplan WHERE projectid = ?";

         try (Connection connection = databaseConnection.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(query)) {

             preparedStatement.setInt(1, projectId);
             ResultSet resultSet = preparedStatement.executeQuery();

             if (resultSet.next()) {
                 String budget = resultSet.getString("budget");
                 budgetLabel.setText(budget != null ? budget : "No budget available.");
             } else {
                 budgetLabel.setText("Project not found.");
             }

         } catch (SQLException e) {
             e.printStackTrace();
             budgetLabel.setText("Error retrieving budget.");
         }
     }
     private void displayexpenditureFromDatabase() {
         DatabaseConnection databaseConnection = new DatabaseConnection();
         String query = "SELECT expenditure FROM \"IMPACT Club\".projectplan WHERE projectid = ?";

         try (Connection connection = databaseConnection.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(query)) {

             preparedStatement.setInt(1, projectId);
             ResultSet resultSet = preparedStatement.executeQuery();

             if (resultSet.next()) {
                 String expenditure = resultSet.getString("expenditure");
                 expenditureLabel.setText(expenditure != null ? expenditure : "No budget available.");
             } else {
            	 expenditureLabel.setText("Project not found.");
             }

         } catch (SQLException e) {
             e.printStackTrace();
             expenditureLabel.setText("Error retrieving budget.");
         }
     }
     private void fundingSourcesFromDatabase() {
         DatabaseConnection databaseConnection = new DatabaseConnection();
         String query = "SELECT fundingsources FROM \"IMPACT Club\".projectplan WHERE projectid = ?";

         try (Connection connection = databaseConnection.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(query)) {

             preparedStatement.setInt(1, projectId);
             ResultSet resultSet = preparedStatement.executeQuery();

             if (resultSet.next()) {
                 String fundingsources = resultSet.getString("fundingsources");
                 fundingSourcesLabel.setText(fundingsources != null ? fundingsources : "No funding Sources available.");
             } else {
                 fundingSourcesLabel.setText("Project not found.");
             }

         } catch (SQLException e) {
             e.printStackTrace();
             fundingSourcesLabel.setText("Error retrieving budget.");
         }
     }
     private void objectivesFromDatabase() {
         DatabaseConnection databaseConnection = new DatabaseConnection();
         String query = "SELECT objectives FROM \"IMPACT Club\".projectplan WHERE projectid = ?";

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
     private void enableEditing() {
        
         budgetTextField.setText(budgetLabel.getText());
         budgetTextField.setLayoutX(budgetLabel.getLayoutX());
         budgetTextField.setLayoutY(budgetLabel.getLayoutY());
         budgetTextField.setPrefWidth(budgetLabel.getPrefWidth());
         budgetTextField.setPrefHeight(budgetLabel.getPrefHeight());

         expenditureTextField.setText(expenditureLabel.getText());
         expenditureTextField.setLayoutX(expenditureLabel.getLayoutX());
         expenditureTextField.setLayoutY(expenditureLabel.getLayoutY());
         expenditureTextField.setPrefWidth(expenditureLabel.getPrefWidth());
         expenditureTextField.setPrefHeight(expenditureLabel.getPrefHeight());
         
         objectivesTextField.setText(objectivesLabel.getText());
         objectivesTextField.setLayoutX(objectivesLabel.getLayoutX());
         objectivesTextField.setLayoutY(objectivesLabel.getLayoutY());
         objectivesTextField.setPrefWidth(objectivesLabel.getPrefWidth());
         objectivesTextField.setPrefHeight(objectivesLabel.getPrefHeight());
         
         fundingSourcesTextField.setText(fundingSourcesLabel.getText());
         fundingSourcesTextField.setLayoutX(fundingSourcesLabel.getLayoutX());
         fundingSourcesTextField.setLayoutY(fundingSourcesLabel.getLayoutY());
         fundingSourcesTextField.setPrefWidth(fundingSourcesLabel.getPrefWidth());
         fundingSourcesTextField.setPrefHeight(fundingSourcesLabel.getPrefHeight());

         objectivesTextField.setEditable(true);
         expenditureTextField.setEditable(true);
         budgetTextField.setEditable(true);
         fundingSourcesTextField.setEditable(true);
       
         if (!((AnchorPane) objectivesLabel.getParent()).getChildren().contains(objectivesTextField)) {
             ((AnchorPane) objectivesLabel.getParent()).getChildren().add(objectivesTextField);
         }
         if (!((AnchorPane) expenditureLabel.getParent()).getChildren().contains(expenditureTextField)) {
             ((AnchorPane) expenditureLabel.getParent()).getChildren().add(expenditureTextField);
         }
         if (!((AnchorPane) budgetLabel.getParent()).getChildren().contains(budgetTextField)) {
             ((AnchorPane) budgetLabel.getParent()).getChildren().add(budgetTextField);
         }
         if (!((AnchorPane) fundingSourcesLabel.getParent()).getChildren().contains(fundingSourcesTextField)) {
             ((AnchorPane) fundingSourcesLabel.getParent()).getChildren().add(fundingSourcesTextField);
         }
        
         objectivesLabel.setVisible(false);
         fundingSourcesLabel.setVisible(false);
         expenditureLabel.setVisible(false);
         budgetLabel.setVisible(false);
     }


     private void saveData() {
         String updatedObjectives = objectivesTextField.getText();
         String updatedbudget = budgetTextField.getText();
         String updatedexpenditure = expenditureTextField.getText();
         String updatedfunding = fundingSourcesTextField.getText();
         BigDecimal numericBudget = new BigDecimal(updatedbudget);
         BigDecimal numericExpenditure = new BigDecimal(updatedexpenditure);
         String query = "UPDATE \"IMPACT Club\".projectplan SET objectives = ?, fundingsources = ?, expenditure = ? ,budget=? WHERE projectid = ?";


         try (Connection connection = DatabaseConnection.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(query)) {

             preparedStatement.setString(1, updatedObjectives);
             preparedStatement.setString(2, updatedfunding);
             preparedStatement.setBigDecimal(3, numericExpenditure);
             preparedStatement.setBigDecimal(4, numericBudget);
             preparedStatement.setInt(5, projectId);

             int rowsUpdated = preparedStatement.executeUpdate();
             if (rowsUpdated > 0) {
                 objectivesLabel.setText(updatedObjectives);
                 budgetLabel.setText(updatedbudget);
                 expenditureLabel.setText(updatedexpenditure);
                 fundingSourcesLabel.setText(updatedfunding);

                 ((AnchorPane) objectivesTextField.getParent()).getChildren().remove(objectivesTextField);
                 ((AnchorPane) budgetTextField.getParent()).getChildren().remove(budgetTextField);
                 ((AnchorPane) expenditureTextField.getParent()).getChildren().remove(expenditureTextField);
                 ((AnchorPane) fundingSourcesTextField.getParent()).getChildren().remove(fundingSourcesTextField);
                 objectivesLabel.setVisible(true);
                 fundingSourcesLabel.setVisible(true);
                 expenditureLabel.setVisible(true);
                 budgetLabel.setVisible(true);

                 System.out.println("Data updated successfully.");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

}
