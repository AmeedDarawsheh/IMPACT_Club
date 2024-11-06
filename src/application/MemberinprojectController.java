package application;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberinprojectController {
 DatabaseConnection data= new DatabaseConnection();
    @FXML
    private TableView<PersonInProject> tableView;

    @FXML
    private TableColumn<PersonInProject, String> memberIdCol;

    @FXML
    private TableColumn<PersonInProject, String> nameCol;

    @FXML
    private TableColumn<PersonInProject, String> roleInProjectCol;

    @FXML
    private TableColumn<PersonInProject, String> feedbackCol;

    @FXML
    private TableColumn<PersonInProject, Integer> involvementLevelCol;
    @FXML
    private ComboBox<String> columnComboBox;
    @FXML
    private TextField searchField;
    private ObservableList<PersonInProject> projectMemberData;
    private int projectId;

	public void setProjectId(int projectId) {
	       this.projectId = projectId;
	       loadDataFromDatabase(projectId);
	    }

    public void initialize() {
        projectMemberData = FXCollections.observableArrayList();

        // Set up columns to map to PersonInProject properties
        memberIdCol.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleInProjectCol.setCellValueFactory(new PropertyValueFactory<>("roleInProject"));
        feedbackCol.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        involvementLevelCol.setCellValueFactory(new PropertyValueFactory<>("projectInvolvementLevel"));
        columnComboBox.setItems(FXCollections.observableArrayList("Member ID", "Name", "Role in Project", "Feedback", "Project Involvement Level"));

        // Load data from the database for a specific project
        loadDataFromDatabase(projectId); // Replace with the desired project ID
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

        // Map the user-friendly column names to database columns
        switch (column) {
            case "Member ID":
                dbColumn = "p.ssn";
                break;
            case "Name":
                dbColumn = "p.first_name || ' ' || p.middle_name || ' ' || p.last_name";
                break;
            case "Role in Project":
                dbColumn = "mp.roleinproject";
                break;
            case "Feedback":
                dbColumn = "mp.projectfeedback";
                break;
            case "Project Involvement Level":
                dbColumn = "mp.projectinvolvementlevel";
                break;
            default:
                return;
        }

        // Clear the current data
        projectMemberData.clear();

       

        String query = "SELECT p.ssn, p.first_name || ' ' || p.middle_name || ' ' || p.last_name AS name, "
                     + "mp.roleinproject, mp.projectfeedback, mp.projectinvolvementlevel "
                     + "FROM \"IMPACT Club\".memberproject mp "
                     + "JOIN \"IMPACT Club\".member m ON mp.memberid = m.memberid "
                     + "JOIN \"IMPACT Club\".person p ON m.ssn = p.ssn "
                     + "WHERE mp.projectid = ? AND " + dbColumn + " ILIKE ?";

        try (Connection conn = data.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
        	if ("Project Involvement Level".equals(column)) {
                // For integer fields, cast the value to integer
                stmt.setInt(2, Integer.parseInt(value));
            } else {
                // For string fields, use ILIKE for case-insensitive search
                stmt.setString(2, "%" + value + "%");
            }
            stmt.setInt(1, projectId);  // Replace with the appropriate project ID or make it dynamic

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String memberId = rs.getString("ssn");
                String name = rs.getString("name");
                String roleInProject = rs.getString("roleinproject");
                String feedback = rs.getString("projectfeedback");
                int involvementLevel = rs.getInt("projectinvolvementlevel");

                projectMemberData.add(new PersonInProject(memberId, name, roleInProject, feedback, involvementLevel));
            }

            tableView.setItems(projectMemberData);  // Display filtered data in TableView

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void enableEditing() {
        tableView.setEditable(true);
        setColumnEditable();
    }
    private void setColumnEditable() {
        tableView.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            PersonInProject person = event.getRowValue();
            person.setName(event.getNewValue());
        });

        roleInProjectCol.setCellFactory(TextFieldTableCell.forTableColumn());
        roleInProjectCol.setOnEditCommit(event -> {
            PersonInProject person = event.getRowValue();
            person.setRoleInProject(event.getNewValue());
        });

        feedbackCol.setCellFactory(TextFieldTableCell.forTableColumn());
        feedbackCol.setOnEditCommit(event -> {
            PersonInProject person = event.getRowValue();
            person.setFeedback(event.getNewValue());
        });

        involvementLevelCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        involvementLevelCol.setOnEditCommit(event -> {
            PersonInProject person = event.getRowValue();
            person.setProjectInvolvementLevel(event.getNewValue());
        });
    }
    // Save the changes to the database on Save button click
    @FXML
    private void saveChanges() {
       
    	String updateQuery = "UPDATE \"IMPACT Club\".memberproject "
                + "SET roleinproject = ?, projectfeedback = ?, projectinvolvementlevel = ? "
                + "WHERE memberid = (SELECT memberid FROM \"IMPACT Club\".member WHERE ssn = ?) "
                + "AND projectid = ?";

try (Connection conn = data.getConnection();
  PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

 for (PersonInProject person : projectMemberData) {
     stmt.setString(1, person.getRoleInProject());
     stmt.setString(2, person.getFeedback());
     stmt.setInt(3, person.getProjectInvolvementLevel());
     stmt.setString(4, person.getMemberId()); // Use ssn here as input to retrieve memberid
     stmt.setInt(5, projectId); // Project ID as specified

     stmt.executeUpdate();
 }

 System.out.println("Changes saved to the database.");

} catch (Exception e) {
 e.printStackTrace();
}

    }
    @FXML
private void refresh() { 
    	projectMemberData.clear();
	loadDataFromDatabase(projectId);
}
    private void loadDataFromDatabase(int projectId) {
       DatabaseConnection data=new DatabaseConnection();
       String query = "SELECT p.ssn, p.first_name || ' ' || p.middle_name || ' ' || p.last_name AS name, "
               + "mp.roleinproject, mp.projectfeedback, mp.projectinvolvementlevel "
               + "FROM \"IMPACT Club\".memberproject mp "
               + "JOIN \"IMPACT Club\".member m ON mp.memberid = m.memberid "
               + "JOIN \"IMPACT Club\".person p ON m.ssn = p.ssn "
               + "WHERE mp.projectid = ?";


        try (Connection conn = data.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String memberId = rs.getString("ssn");
                String name = rs.getString("name");
                String roleInProject = rs.getString("roleinproject");
                String feedback = rs.getString("projectfeedback");
                int involvementLevel = rs.getInt("projectinvolvementlevel");

                projectMemberData.add(new PersonInProject(memberId, name, roleInProject, feedback, involvementLevel));
            }

            tableView.setItems(projectMemberData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
