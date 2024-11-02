package application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MonthPlanController {

    @FXML
    private VBox tasksContainer; 
    @FXML
    private Button addTaskButton;
    @FXML
    private Button saveButton;

    private int month;
    private int yearlyplanId;
    private Connection databaseConnection; 
    @FXML
    private Label monthLabel; 

    public void setMonthName(String monthName) {
        if (monthLabel != null) {
            monthLabel.setText(monthName + " Plan");
        }}

    
    public void setMonth(int month, int yearlyplanId) {
        this.month = month;
        this.yearlyplanId = yearlyplanId;

  
        if (databaseConnection == null) {
            databaseConnection = DatabaseConnection.getConnection();
        }
        
        loadTasksFromDatabase(); 
    }

    
    private void loadTasksFromDatabase() {
        if (databaseConnection == null) {
            System.out.println("Database connection is not established.");
            return;
        }

        String query = "SELECT task_id,task_name, completed FROM \"IMPACT Club\".tasks " +
                       "JOIN \"IMPACT Club\".monthlyplan ON tasks.monthlyplan_id = monthlyplan.monthlyplan_id " +
                       "WHERE monthlyplan.yearplan_id = ? AND monthlyplan.month = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(query)) {
            statement.setInt(1, yearlyplanId);
            statement.setInt(2, month);

            ResultSet resultSet = statement.executeQuery();
            tasksContainer.getChildren().clear(); 

            while (resultSet.next()) {
            	int taskId = resultSet.getInt("task_id");
                String description = resultSet.getString("task_name");
                boolean completed = resultSet.getBoolean("completed");

                CheckBox taskCheckBox = new CheckBox(description);
                taskCheckBox.setSelected(completed);
                taskCheckBox.setUserData(taskId);
                tasksContainer.getChildren().add(taskCheckBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void saveTasksToDatabase() {
        if (databaseConnection == null) {
            System.out.println("Database connection is not established.");
            return;
        }

        String updateQuery = "UPDATE \"IMPACT Club\".tasks SET completed = ? WHERE task_id = ?";

        try (PreparedStatement statement = databaseConnection.prepareStatement(updateQuery)) {
            for (var node : tasksContainer.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox taskCheckBox = (CheckBox) node;
                    Object taskIdObj = taskCheckBox.getUserData();

                    
                    if (taskIdObj == null) {
                        System.out.println("task_id is null for task: " + taskCheckBox.getText());
                        continue; 
                    }

                    int taskId = (int) taskIdObj;
                    boolean completed = taskCheckBox.isSelected();

                    System.out.println("Updating task_id: " + taskId + " to completed: " + completed);
                    
                    statement.setBoolean(1, completed);
                    statement.setInt(2, taskId);
                    statement.executeUpdate(); 
                }
            }
            System.out.println("Tasks saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    
    public void addNewTask() {
        if (databaseConnection == null) {
            System.out.println("Database connection is not established.");
            return;
        }

        TextField newTaskField = new TextField();
        newTaskField.setPromptText("Enter new task here");
        tasksContainer.getChildren().add(newTaskField);

        newTaskField.setOnAction(event -> {
            String newTaskName = newTaskField.getText();

            if (!newTaskName.isEmpty()) {
                String insertQuery = "INSERT INTO \"IMPACT Club\".tasks (task_name, completed, monthlyplan_id) " +
                                     "VALUES (?, false, (SELECT monthlyplan_id FROM \"IMPACT Club\".monthlyplan " +
                                     "WHERE yearplan_id = ? AND month = ?)) RETURNING task_id";

                try (PreparedStatement statement = databaseConnection.prepareStatement(insertQuery)) {
                    statement.setString(1, newTaskName);
                    statement.setInt(2, yearlyplanId);
                    statement.setInt(3, month);
                    
                    ResultSet resultSet = statement.executeQuery();
                    
                    if (resultSet.next()) {
                        int taskId = resultSet.getInt("task_id"); 

                       
                        CheckBox newTaskCheckBox = new CheckBox(newTaskName);
                        newTaskCheckBox.setSelected(false);
                        newTaskCheckBox.setUserData(taskId); 

                        tasksContainer.getChildren().add(newTaskCheckBox);
                    }

                    tasksContainer.getChildren().remove(newTaskField);

                    System.out.println("Task added successfully.");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
