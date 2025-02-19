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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.control.Button;
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
    private TableColumn<PersonInProject, String> actionCol;
    
    @FXML
    private ComboBox<String> columnComboBox;
    @FXML
    private TextField searchField;
    private ObservableList<PersonInProject> projectMemberData;
    private int projectId;
    PersonInProject person=null;
	public void setProjectId(int projectId) {
	       this.projectId = projectId;
	       loadDataFromDatabase(projectId);
	    }

    public void initialize() {
        projectMemberData = FXCollections.observableArrayList();

       
        memberIdCol.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleInProjectCol.setCellValueFactory(new PropertyValueFactory<>("roleInProject"));
        feedbackCol.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        involvementLevelCol.setCellValueFactory(new PropertyValueFactory<>("projectInvolvementLevel"));
        columnComboBox.setItems(FXCollections.observableArrayList("Member ID", "Name", "Role in Project", "Feedback", "Project Involvement Level"));
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            private final Button editButton = new Button("Edit");

            {
                deleteButton.setStyle("-fx-text-fill: red;");
                editButton.setStyle("-fx-text-fill: blue;");

                deleteButton.setOnAction(event -> {
                    PersonInProject selectedPerson = getTableView().getItems().get(getIndex());
                    deletePersonInProject(selectedPerson);
                    refresh();  
                });

                editButton.setOnAction(event -> {
                    PersonInProject selectedPerson = getTableView().getItems().get(getIndex());
                    editPersonInProject(selectedPerson);
                });
            }
            private void deletePersonInProject(PersonInProject person) {
                String query = "DELETE FROM \"IMPACT Club\".memberproject WHERE projectid = ? AND memberid = (SELECT memberid FROM \"IMPACT Club\".member WHERE ssn = ?)";
                try (Connection conn = data.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, projectId);
                    stmt.setString(2, person.getMemberId());
                    stmt.executeUpdate();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            private void editPersonInProject(PersonInProject person) {
                try {
                    
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("editMemProject.fxml"));
                    Parent root = loader.load();

                    
                    EditMemberProjectFieldController controller = loader.getController();
                    controller.setPerson(person); 

                    
                    Stage stage = new Stage();
                    stage.setTitle("Edit Member in Project");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL); 
                    stage.showAndWait();

                    
                    if (controller.isSaveClicked()) {
                        
                        updatePersonInDatabase(person);
                        refresh(); 
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            private void updatePersonInDatabase(PersonInProject person) {
                String query = "UPDATE \"IMPACT Club\".memberproject SET roleinproject = ?, projectfeedback = ?, projectinvolvementlevel = ? WHERE projectid = ? AND memberid = (SELECT memberid FROM \"IMPACT Club\".member WHERE ssn = ?)";
                try (Connection conn = data.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, person.getRoleInProject());
                    stmt.setString(2, person.getFeedback());
                    stmt.setInt(3, person.getProjectInvolvementLevel());
                    stmt.setInt(4, projectId);
                    stmt.setString(5, person.getMemberId()); 

                    stmt.executeUpdate();

                } catch (SQLException ex) {
                    ex.printStackTrace();
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
        
        loadDataFromDatabase(projectId); 
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
                
                stmt.setInt(2, Integer.parseInt(value));
            } else {
                
                stmt.setString(2, "%" + value + "%");
            }
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
    @FXML
    private void handleAddMember() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addMemProject.fxml"));
            Parent root = loader.load();

           
            AddMemberProjectController controller = loader.getController();
            controller.setProjectId(projectId); 

            
            Stage stage = new Stage();
            stage.setTitle("Add Member in Project");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.showAndWait();

          
            if (controller.isSaveClicked()) {
                refresh(); 
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

            }
            
