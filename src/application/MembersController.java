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
        tableView.refresh();  // Explicitly refresh the table view
    }

    public void initialize() {
        Data = FXCollections.observableArrayList();

        // Set up columns to map to Person properties
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
                    // Get the person associated with the current row
                    Person selectedPerson = getTableView().getItems().get(getIndex());

                    // Confirm deletion
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmAlert.setTitle("Delete Confirmation");
                    confirmAlert.setHeaderText("Are you sure you want to delete this entry?");
                    confirmAlert.setContentText("This action will delete the person and related data.");

                    Optional<ButtonType> result = confirmAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        // Perform the deletion in the database
                        deletePersonCascade(selectedPerson.getSsn());

                        // Remove from TableView
                        getTableView().getItems().remove(selectedPerson);

                        // Show success alert
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

                    refreshTable();  // Refresh the table after the edit form is closed
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

        // Map GUI column names to database column names
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
            case "Birth of Date":
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
                return; // Exit if column is not found
        }

        Data.clear(); // Clear the current data list

        String query = "SELECT p.ssn, p.first_name || ' ' || p.middle_name || ' ' || p.last_name AS name, "
                + "p.street || ', ' || p.city AS address, p.start_date, p.bod, p.user_name, "
                + "p.phone_number, p.gender, m.points "
                + "FROM \"IMPACT Club\".person p "
                + "JOIN \"IMPACT Club\".member m ON p.ssn = m.ssn "
                + "WHERE ";

   // Handle date columns specifically
   if (column.equals("Start Date") || column.equals("Birth of Date")) {
       query += dbColumn + " = TO_DATE(?, 'YYYY-MM-DD')";
   } else {
       query += dbColumn + " ILIKE ?";
   }

   try (Connection conn = data.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {

       if (column.equals("Start Date") || column.equals("Birth of Date")) {
           stmt.setString(1, value); // For date columns, use the input directly as a date
       } else {
           stmt.setString(1, "%" + value + "%"); // Use ILIKE for case-insensitive search
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

            tableView.setItems(Data); // Set the filtered data to TableView

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshTable() {
        loadDataFromDatabase();
        tableView.setItems(Data);
    }


}
