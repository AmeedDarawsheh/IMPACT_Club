package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.Optional;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MembersController {
DatabaseConnection data =new DatabaseConnection();
    @FXML
    private TableView<Person> tableView;

    @FXML
    private TableColumn<Person, String> idCol;

    @FXML
    private TableColumn<Person, String> nameCol;

    @FXML
    private TableColumn<Person, String> addressCol;

    @FXML
    private TableColumn<Person, String> memberSinceCol;

    @FXML
    private TableColumn<Person, String> dobCol;

    @FXML
    private TableColumn<Person, String> emailCol;

    @FXML
    private TableColumn<Person, String> phoneCol;

    @FXML
    private TableColumn<Person, Integer> pointsCol;

    @FXML
    private TableColumn<Person, String> genderCol;
    @FXML
    private TableColumn<Person, String> actionCol;
    @FXML
    private ComboBox<String> columnComboBox;
    @FXML
    private TextField searchField;
    private ObservableList<Person> Data;
    public void setl() {
  
        loadDataFromDatabase();
        tableView.setItems(Data);
        tableView.refresh(); 
    }

    public void initialize() {
        Data = FXCollections.observableArrayList();

        
        idCol.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        memberSinceCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("bod"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columnComboBox.setItems(FXCollections.observableArrayList("SSN", "Full Name", "address", "Start Date", "Birth of date","Gmail","Phone number","points","gender"));
        idCol.setPrefWidth(100);
        nameCol.setPrefWidth(150);
        addressCol.setPrefWidth(150);
        memberSinceCol.setPrefWidth(100);
        dobCol.setPrefWidth(100);
        emailCol.setPrefWidth(150);
        phoneCol.setPrefWidth(150);
        pointsCol.setPrefWidth(100);
        genderCol.setPrefWidth(100);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Edit");

            {
                deleteButton.setStyle("-fx-text-fill: red;");
                editButton.setStyle("-fx-text-fill: blue;");

                deleteButton.setOnAction(event -> {
                    
                    Person selectedPerson = getTableView().getItems().get(getIndex());

                    
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Delete Confirmation");
                    confirmAlert.setHeaderText("Are you sure you want to delete this entry?");
                    confirmAlert.setContentText("This action will delete the person and related data.");

                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                       
                        deletePersonCascade(selectedPerson.getSsn());

                      
                        getTableView().getItems().remove(selectedPerson);

                       
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Deletion Successful");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("The person and related data were successfully deleted.");
                        successAlert.showAndWait();
                    }
                });
                editButton.setOnAction(event -> {
                    Person selectedPerson = getTableView().getItems().get(getIndex());
                    openEditForm(selectedPerson);
                });
                	
            }
            private void deletePersonCascade(String ssn) {
                String deleteQuery = "DELETE FROM \"IMPACT Club\".person WHERE ssn = ?";

                try (Connection conn = data.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
                    
                    stmt.setString(1, ssn);
                    int affectedRows = stmt.executeUpdate();
                    
                    if (affectedRows > 0) {
                        System.out.println("Person with SSN " + ssn + " and related data deleted successfully.");
                    } else {
                        System.out.println("No person found with SSN " + ssn);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            private void openEditForm(Person selectedPerson) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("editMem.fxml"));
                    Parent root = loader.load();

                    EditMemController editController = loader.getController();
                    editController.setMemberData(selectedPerson);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Edit Member");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                    tableView.getItems().clear();

                    refreshTable();  
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

           
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox(editButton, deleteButton);
                    hBox.setSpacing(10);
                    setGraphic(hBox);
                }
            }
        });
        

        // Load data from the database
        loadDataFromDatabase();
        tableView.setItems(Data); // Set the data
        tableView.refresh();
    }

    public void loadDataFromDatabase() {
     DatabaseConnection database =new DatabaseConnection();

        String query = "SELECT p.ssn, p.first_name, p.middle_name, p.last_name, p.street, p.city, "
                     + "p.start_date, p.bod, p.user_name, p.phone_number, p.gender, m.points "
                     + "FROM \"IMPACT Club\".person p JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn";

        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String ssn = rs.getString("ssn");
                
                String fullName = rs.getString("first_name") + " " + rs.getString("middle_name") + " " + rs.getString("last_name");
                String address = rs.getString("street") + ", " + rs.getString("city");
                String startDate = rs.getString("start_date");
                String bod = rs.getString("bod");
                String email = rs.getString("user_name");
                String phoneNumber = rs.getString("phone_number");
                String gender = rs.getString("gender");
                int points = rs.getInt("points");

                Data.add(new Person(ssn, fullName, address, startDate, bod, email, phoneNumber, gender, points));
            }
            
            
            //tableView.setItems(Data); // Set the data
            //tableView.refresh();
            

          

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void refresh() { 
    
    	Data.clear();
	loadDataFromDatabase();
}
    @FXML
    private void handleSearch() {
        String selectedColumn = columnComboBox.getValue();
        String searchText = searchField.getText().trim();

        if (selectedColumn != null && !searchText.isEmpty()) {
            filterData(selectedColumn, searchText);
        }
    }

    private void filterData(String column, String value) {
        String dbColumn;

       
        switch (column) {
            case "SSN":
                dbColumn = "p.ssn";
                break;
            case "Full Name":
                dbColumn = "p.first_name || ' ' || p.middle_name || ' ' || p.last_name";
                break;
            case "address":
                dbColumn = "p.street || ', ' || p.city";
                break;
            case "Start Date":
                dbColumn = "p.start_date";
                break;
            case "Birth of date":
                dbColumn = "p.bod";
                break;
            case "Gmail":
                dbColumn = "p.user_name";
                break;
            case "Phone number":
                dbColumn = "p.phone_number";
                break;
            case "points":
                dbColumn = "m.points";
                break;
            case "gender":
                dbColumn = "p.gender";
                break;
            default:
                return;
        }

        Data.clear();

        String query = "SELECT p.ssn, p.first_name || ' ' || p.middle_name || ' ' || p.last_name AS name, "
                + "p.street || ', ' || p.city AS address, p.start_date, p.bod, p.user_name, "
                + "p.phone_number, p.gender, m.points "
                + "FROM \"IMPACT Club\".person p "
                + "JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn "
                + "WHERE ";

   // Handle date columns specifically
   if (column.equals("Start Date") || column.equals("Birth of date")) {
       query += dbColumn + " = TO_DATE(?, 'YYYY-MM-DD')";
   } else if(column.equals("points")) {
	   query += dbColumn + " = ?"; 
   }
   else {
       query += dbColumn + " ILIKE ?";
   }

   try (Connection conn = data.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {

       if (column.equals("Start Date") || column.equals("Birth of date")) {
           stmt.setString(1, value); 
       } else if (column.equals("points")) {
    	   stmt.setInt(1, Integer.parseInt(value.trim()));}
       else {
           stmt.setString(1, "%" + value + "%"); 
       }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String ssn = rs.getString("ssn");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String startDate = rs.getString("start_date");
                String birthDate = rs.getString("bod");
                String email = rs.getString("user_name");
                String phoneNumber = rs.getString("phone_number");
                String gender = rs.getString("gender");
                int points = rs.getInt("points");

               Data.add(new Person(ssn, name, address, startDate, birthDate, email, phoneNumber, gender, points));
            }

            tableView.setItems(Data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshTable() {
        loadDataFromDatabase();
        tableView.setItems(Data);
    }
    
    @FXML
    private void handleSendEmail() {
      
        Person selectedMember = tableView.getSelectionModel().getSelectedItem();

        if (selectedMember == null) {
         
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Member Selected");
            alert.setContentText("Please select a member to send an email.");
            alert.showAndWait();
        } else {
           
            String email = selectedMember.getUserName(); 

         
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Send Email");
            dialog.setHeaderText("Enter email subject and message");

            
            Label subjectLabel = new Label("Subject:");
            TextField subjectField = new TextField();
            Label messageLabel = new Label("Message:");
            TextArea messageArea = new TextArea();

           
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.add(subjectLabel, 0, 0);
            grid.add(subjectField, 1, 0);
            grid.add(messageLabel, 0, 1);
            grid.add(messageArea, 1, 1);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String subject = subjectField.getText();
                String message = messageArea.getText();

                try {
                    new SendEmail(email, subject, message);

                    // Success alert
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Email Sent");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("The email has been sent to " + email + ".");
                    successAlert.showAndWait();
                } catch (Exception e) {
                    // Error alert
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to send email");
                    errorAlert.setContentText("There was an error sending the email. Please try again.");
                    errorAlert.showAndWait();
                    e.printStackTrace();
                }
            }
        }
    }

}
