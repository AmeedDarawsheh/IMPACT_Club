package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LeaderController {
    DatabaseConnection data = new DatabaseConnection();
    private int LeaderLoginId; // Field to store the logged-in leader's ID

    @FXML
    private TableView<Leader> tableView;

    @FXML
    private TableColumn<Leader, String> idCol;
    @FXML
    private TableColumn<Leader, String> nameCol;
    @FXML
    private TableColumn<Leader, String> addressCol;
    @FXML
    private TableColumn<Leader, String> memberSinceCol;
    @FXML
    private TableColumn<Leader, String> dobCol;
    @FXML
    private TableColumn<Leader, String> emailCol;
    @FXML
    private TableColumn<Leader, String> phoneCol;
    @FXML
    private TableColumn<Leader, String> majorCol;
    @FXML
    private TableColumn<Leader, String> genderCol;
    
    @FXML
    private ComboBox<String> columnComboBox;
    @FXML
    private TextField searchField;

    private ObservableList<Leader> Data;

  
    public LeaderController(int leaderLoginId) {
        this.LeaderLoginId = leaderLoginId;
    }
    public LeaderController() {
        
        this.LeaderLoginId = -1;
    }

  
    
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
        majorCol.setCellValueFactory(new PropertyValueFactory<>("major")); // Use major instead of points
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        columnComboBox.setItems(FXCollections.observableArrayList("SSN", "Full Name", "address", "Start Date", "Birth of date", "Gmail", "Phone number", "major", "gender"));

        
        idCol.setPrefWidth(100);
        nameCol.setPrefWidth(150);
        addressCol.setPrefWidth(150);
        memberSinceCol.setPrefWidth(100);
        dobCol.setPrefWidth(100);
        emailCol.setPrefWidth(150);
        phoneCol.setPrefWidth(150);
        majorCol.setPrefWidth(100);
        genderCol.setPrefWidth(100);

        
        loadDataFromDatabase();
        tableView.setItems(Data);
        tableView.refresh();
    }

    
   
    public void loadDataFromDatabase() {
        String query = "SELECT p.ssn, p.first_name, p.middle_name, p.last_name, p.street, p.city, "
                     + "p.start_date, p.bod, p.user_name, p.phone_number, p.gender, l.major "
                     + "FROM \"IMPACT Club\".person p "
                     + "JOIN \"IMPACT Club\".leader l ON p.ssn = l.ssn";

        try (Connection conn = data.getConnection();
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
                String major = rs.getString("major");

                Data.add(new Leader(ssn, fullName, address, startDate, bod, email, phoneNumber, gender, major));
            }

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
            case "Birth of date":
                dbColumn = "p.bod";
                break;
            case "Gmail":
                dbColumn = "p.user_name";
                break;
            case "Phone number":
                dbColumn = "p.phone_number";
                break;
            case "major":
                dbColumn = "l.major";
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
                     + "p.phone_number, p.gender, l.major "
                     + "FROM \"IMPACT Club\".person p "
                     + "JOIN \"IMPACT Club\".leader l ON p.ssn = l.ssn "
                     + "WHERE ";

        if (column.equals("Start Date") || column.equals("Birth of date")) {
            query += dbColumn + " = TO_DATE(?, 'YYYY-MM-DD')";
        } else {
            query += dbColumn + " ILIKE ?";
        }

        try (Connection conn = data.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (column.equals("Start Date") || column.equals("Birth of date")) {
                stmt.setString(1, value);
            } else {
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
                String gender = rs.getString("gender").equalsIgnoreCase("M") ? "Male" : "Female";
                
                String major = rs.getString("major");

                Data.add(new Leader(ssn, name, address, startDate, birthDate, email, phoneNumber, gender,major));
            }

            tableView.setItems(Data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        Data.clear();
        loadDataFromDatabase();
    }

    @FXML
    private void handleSendEmail() {
        Leader selectedMember = tableView.getSelectionModel().getSelectedItem();

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

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Email Sent");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("The email has been sent to " + email + ".");
                    successAlert.showAndWait();
                } catch (Exception e) {
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

