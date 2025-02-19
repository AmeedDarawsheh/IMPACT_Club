package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.postgresql.util.PGobject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;

public class SessionController implements Initializable {
    
    @FXML private Button searchBtn, saveBtn, editBtn, deleteBtn, addBtn;
    @FXML private TableView<Session> sessionTable;
    @FXML private TableView<SessionLeader> sessionLeaderTable;
    @FXML private TableView<SessionMember> sessionMemberTable;
    @FXML private ToggleButton switchBtn;
    @FXML private TextField IN, F1, F2,F3,F4;
    @FXML private DatePicker date;
    @FXML private Label nameTable;
    @FXML private TableColumn<Session, Integer> IdCol;
    @FXML private TableColumn<Session, LocalDate> dateCol;
    @FXML private TableColumn<Session, String> topicCol;
    @FXML private TableColumn<Session, String> durationCol;
    @FXML private TableColumn<SessionLeader, Integer> sessionIdCol;
    @FXML private TableColumn<SessionLeader, Integer> leaderIdColumn;
    @FXML private TableColumn<SessionLeader, String> roleColumn;
    @FXML private TableColumn<SessionLeader, String> leaderNotesColumn;
    @FXML private TableColumn<SessionLeader, String> attendanceColumn;
    @FXML private TableColumn<SessionMember, Integer> sessionIdCol1;
    @FXML private TableColumn<SessionMember, Integer> memberIdCol;
    @FXML private TableColumn<SessionMember, String> memberNameCol;
    @FXML private TableColumn<SessionMember, String> answersCol;
    @FXML private TableColumn<SessionMember, String> attendanceCol;
    @FXML private TableColumn<SessionMember, String> feedbackCol;


    private Boolean isLeader = false,isMember=false,isSession=true;
    private String ID;
    
    private ObservableList<Session> sessionData = FXCollections.observableArrayList();
    private ObservableList<SessionLeader> leaderData = FXCollections.observableArrayList();
    ObservableList<SessionMember> sessionMembers = FXCollections.observableArrayList();
    DatabaseConnection database = new DatabaseConnection();
    Connection connection = database.getConnection();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	sessionMemberTable.setVisible(false);
        sessionTable.setVisible(true);
        sessionLeaderTable.setVisible(false);
        saveBtn.setDisable(true);
        editBtn.setDisable(false);
        deleteBtn.setDisable(false);
        searchBtn.setDisable(false);
        F2.setDisable(true);
        F1.setDisable(true);
        
        F3.setVisible(false);
        F3.setDisable(true);
        F4.setVisible(false);
        F4.setDisable(true);
        date.setDisable(true);
        nameTable.setText("Session");

        setupSessionTable();
        setupLeaderTable();
        setupMemberTable();
        loadSessionData();
    }

    private void setupSessionTable() {
       
      
        IdCol.setCellValueFactory(new PropertyValueFactory<>("sessionId"));

        
        dateCol.setCellValueFactory(new PropertyValueFactory<>("sessionDate"));

    
        topicCol.setCellValueFactory(new PropertyValueFactory<>("topic"));

        
        durationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

       
    }

    private void setupLeaderTable() {
    	 sessionIdCol.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        
        leaderIdColumn.setCellValueFactory(new PropertyValueFactory<>("leaderId"));

        
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        
        leaderNotesColumn.setCellValueFactory(new PropertyValueFactory<>("leaderNotes"));

        
        attendanceColumn.setCellValueFactory(new PropertyValueFactory<>("attendance"));

       
    }
    private void setupMemberTable() {
   	 sessionIdCol1.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
       
       memberIdCol.setCellValueFactory(new PropertyValueFactory<>("memberId"));

       
       answersCol.setCellValueFactory(new PropertyValueFactory<>("answers"));

       
       feedbackCol.setCellValueFactory(new PropertyValueFactory<>("feedback"));

       
       attendanceCol.setCellValueFactory(new PropertyValueFactory<>("attendance"));
       memberNameCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
       

      
   }
    private void loadSessionData() {
        sessionData.clear();
        String query = "SELECT * FROM \"IMPACT Club\".session ORDER BY sessionid ASC";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sessionData.add(new Session(rs.getInt("sessionid"), rs.getDate("sessiondate").toLocalDate(),
                        rs.getString("topic"), rs.getString("duration")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sessionTable.setItems(sessionData);
    }

    private void loadLeaderData() {
        leaderData.clear();
        String query = "SELECT * FROM \"IMPACT Club\".sessionleader ORDER BY sessionid ASC";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                leaderData.add(new SessionLeader(rs.getInt("sessionid"), rs.getInt("leaderid"),
                        rs.getString("role"), rs.getString("leadernotes"), rs.getString("attendance")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sessionLeaderTable.setItems(leaderData);
    }
    
    private void loadMemberData() {
       sessionMembers.clear();

        String query = "SELECT s.sessionid, m.memberid, p.first_name || ' ' || p.last_name AS member_name, " +
                       "sm.answers, sm.feedback, sm.attendance " +
                       "FROM \"IMPACT Club\".sessionmember sm " +
                       "JOIN \"IMPACT Club\".member m ON sm.memberid = m.memberid " +
                       "JOIN \"IMPACT Club\".person p ON m.ssn = p.ssn " +
                       "JOIN \"IMPACT Club\".session s ON sm.sessionid = s.sessionid";

        try (
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int sessionId = rs.getInt("sessionid");
                int memberId = rs.getInt("memberid");
                String memberName = rs.getString("member_name");
                String answers = rs.getString("answers");
                String feedback = rs.getString("feedback");
                String attendance = rs.getString("attendance");

                sessionMembers.add(new SessionMember(sessionId, memberId, memberName, answers, feedback, attendance));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sessionMemberTable.setItems(sessionMembers);
    }
    @FXML
    public void switchbtn(ActionEvent event) {
        if (isSession) {
            sessionTable.setVisible(false);
            sessionLeaderTable.setVisible(true);
            sessionMemberTable.setVisible(false);
            nameTable.setText("Leader");
            isLeader = true;
            isMember=false;
            isSession=false;
            date.setVisible(false);
            F1.setPromptText("Role");
            F2.setPromptText("Attendance");
            F3.setPromptText("Notes");
            F3.setVisible(true);
            clearFields();
            loadLeaderData();
        } else if (isLeader){
            sessionMemberTable.setVisible(true);
            sessionLeaderTable.setVisible(false);
            sessionTable.setVisible(false);
            nameTable.setText("Mamber");
            isLeader = false;
            isMember=true;
            isSession=false;
            clearFields();
            date.setVisible(true);
            date.setVisible(false);
            F1.setPromptText("Answer");
            F2.setPromptText("Attendance");
            F3.setVisible(true);
            F3.setPromptText("Feedback");
            F4.setVisible(false);
            F4.setDisable(false);
            loadMemberData();
        }
        else if(isMember) {
        	isLeader = false;
            isMember=false;
            isSession=true;
        	sessionTable.setVisible(true);
            sessionLeaderTable.setVisible(false);
            sessionMemberTable.setVisible(false);
            nameTable.setText("Session");
            isLeader = false;
            clearFields();
            date.setVisible(true);
            F1.setPromptText("Topic");
            F2.setPromptText("Time");
            F3.setVisible(false);
            F4.setVisible(false);
            F4.setDisable(false);
            loadSessionData();
        }
    }
    @FXML
    void searchbtn(ActionEvent event) {
        String inputId = IN.getText().trim();
        
        if (inputId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter an ID to search.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        
        try {
            int id = Integer.parseInt(inputId);  
            
            if (isLeader) {
              
                ObservableList<SessionLeader> filteredData = FXCollections.observableArrayList();
                for (SessionLeader leader : leaderData) {
                    if (leader.getLeaderId() == id) {
                        filteredData.add(leader);
                          
                    }
                }
                if (filteredData.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No leader found with ID: " + id, ButtonType.OK);
                    alert.showAndWait();
                } else {
                    sessionLeaderTable.setItems(filteredData);
                }
            }  if (isMember) {
              
                ObservableList<SessionMember> filteredData = FXCollections.observableArrayList();
                for (SessionMember Member : sessionMembers) {
                    if (Member.getMemberId() == id) {
                        filteredData.add(Member);
                          
                    }
                }
                if (filteredData.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No leader found with ID: " + id, ButtonType.OK);
                    alert.showAndWait();
                } else {
                    sessionMemberTable.setItems(filteredData);
                }
            } else {
                // Search in the sessionTable
                ObservableList<Session> filteredData = FXCollections.observableArrayList();
                for (Session session : sessionData) {
                    if (session.getSessionId() == id) {
                        filteredData.add(session);
                        break;  
                    }
                }
                if (filteredData.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No session found with ID: " + id, ButtonType.OK);
                    alert.showAndWait();
                } else {
                    sessionTable.setItems(filteredData);
                }
            }
            
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid ID format. Please enter a numeric ID.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    public void addbtn(ActionEvent event) {
        saveBtn.setDisable(true);
        F2.setDisable(false);
        F1.setDisable(false);
        date.setDisable(false);
        date.setVisible(true);
        F4.setVisible(false);
        F3.setVisible(false);
        if (isSession) {  
            try {
              
                String topic = F1.getText().trim();
                String durationString = F2.getText().trim();
                LocalDate sessionDate = date.getValue();

                
                if (topic.isEmpty() || durationString.isEmpty() || sessionDate == null) {
                    showAlert("Please fill all fields for the session entry.");
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Time duration;
                try {
                    duration = new Time(sdf.parse(durationString).getTime());
                } catch (ParseException e) {
                    showAlert("Invalid time format. Please enter duration as HH:mm:ss.");
                    return;
                }

                String query = "INSERT INTO \"IMPACT Club\".session (sessiondate, topic, duration) VALUES (?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setDate(1, java.sql.Date.valueOf(sessionDate));
                    ps.setString(2, topic);
                    ps.setTime(3, duration);

                    int result = ps.executeUpdate();
                    if (result > 0) {
                        showAlert("Session entry added successfully.");
                        loadSessionData(); 
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("An error occurred while adding the entry.");
            }
        }
        else if(isLeader) {    saveBtn.setDisable(true);
        F2.setDisable(false);
        F1.setDisable(false);
        IN.setDisable(false);
        F4.setVisible(true);
        F3.setVisible(true);
        F3.setDisable(false);
        F4.setDisable(false);
        
        try {
          
            int leaderId;
            try {
                leaderId = Integer.parseInt(IN.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid numeric Leader ID.");
                return;
            }   
            String role = F1.getText().trim();
            String attendance = F2.getText().trim();
            String notes = F3.getText().trim(); 
            String id=IN.getText().trim();
            String ids=F4.getText().trim();
         
            if (ids.isEmpty()||id.isEmpty()||role.isEmpty() || attendance.isEmpty() || notes.isEmpty()) {
                showAlert("Please fill all fields for the leader entry.");
                return;
            }
            
            if (!attendance.equals("Present") && !attendance.equals("Absent")) {
                showAlert("Attendance must be either 'Present' or 'Absent'.");
                return;
            }
            PGobject attendanceObj = new PGobject();
            attendanceObj.setType("\"IMPACT Club\".isattendence"); 
            attendanceObj.setValue(attendance); 
            leaderId = Integer.parseInt(id);
           int  sessionId = Integer.parseInt(ids);

           
            String query = "INSERT INTO \"IMPACT Club\".sessionleader (sessionid, leaderid, role, attendance, leadernotes) " +
                    "VALUES (?, ?, ?, ?, ?) ON CONFLICT (sessionid, leaderid) DO NOTHING";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
            	ps.setInt(1, sessionId);
                ps.setInt(2, leaderId);
                ps.setString(3, role);
                ps.setObject(4, attendanceObj);
                ps.setString(5, notes);

                int result = ps.executeUpdate();
              
                if (result > 0) {
                    showAlert("Leader entry added successfully.");
                    loadLeaderData();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("An error occurred while adding the leader entry.");
        }
    }
        else if(isMember) {
            saveBtn.setDisable(true);
            F2.setDisable(false);
            F1.setDisable(false);
            IN.setDisable(false);
            F4.setVisible(true);
            F3.setVisible(true);
            F3.setDisable(false);
            F4.setDisable(false);
date.setVisible(false);
            try {
              
                int memberId;
                try {
                    memberId = Integer.parseInt(IN.getText().trim());
                } catch (NumberFormatException e) {
                    showAlert("Please enter a valid numeric Member ID.");
                    return;
                }
                String answers = F1.getText().trim();
                String attendance = F2.getText().trim();
                String feedback = F3.getText().trim(); 
                String id = IN.getText().trim();
                String sessionIdStr = F4.getText().trim();

                
                if (sessionIdStr.isEmpty() || id.isEmpty() || attendance.isEmpty()  ) {
                    showAlert("Please fill all fields for the member entry.");
                    return;
                }

                
                if (!attendance.equals("Present") && !attendance.equals("Absent")) {
                    showAlert("Attendance must be either 'Present' or 'Absent'.");
                    return;
                }

                PGobject attendanceObj = new PGobject();
                attendanceObj.setType("\"IMPACT Club\".isattendence");
                attendanceObj.setValue(attendance); 

                memberId = Integer.parseInt(id);
                int sessionId = Integer.parseInt(sessionIdStr);

             
                String query = "INSERT INTO \"IMPACT Club\".sessionmember (sessionid, memberid, attendance, answers, feedback) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setInt(1, sessionId);
                    ps.setInt(2, memberId);
                    ps.setObject(3, attendanceObj);
                    ps.setString(4, answers);
                    ps.setString(5, feedback);

                    int result = ps.executeUpdate();
                    if (result > 0) {
                        showAlert("Member entry added successfully.");
                        loadMemberData(); 
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("An error occurred while adding the member entry.");
            }
        }
        F1.clear();
        F2.clear();
        F3.clear();
        F4.clear();
        IN.clear();
       
        }
    
    
    @FXML
    public void editbtn(ActionEvent event) {
       
        saveBtn.setDisable(false);
        F2.setDisable(false);
        F1.setDisable(false);	
         
        if (isLeader) {
          
        	F4.setVisible(true);
        	F4.setDisable(false);
            date.setVisible(false);
            F3.setDisable(false);
           
            SessionLeader selectedLeader = sessionLeaderTable.getSelectionModel().getSelectedItem();
            if (selectedLeader != null) {
                IN.setText(String.valueOf(selectedLeader.getLeaderId()));
                F1.setText(selectedLeader.getRole()); 
                F2.setText(selectedLeader.getAttendance()); 
                F3.setText(selectedLeader.getLeaderNotes());
                F4.setText(String.valueOf(selectedLeader.getSessionId()));
            
            } else {
                showAlert("Please select a leader entry to edit.");
            }

        } else if(isSession) {
          
            date.setVisible(true);
            date.setDisable(false);

         
            Session selectedSession = sessionTable.getSelectionModel().getSelectedItem();
            if (selectedSession != null) {
                IN.setText(String.valueOf(selectedSession.getSessionId())); 
                F1.setText(selectedSession.getTopic()); 
                F2.setText(selectedSession.getDuration()); 
                date.setValue(selectedSession.getSessionDate()); 
            } else {
                showAlert("Please select a session entry to edit.");
            }
        }
        else if (isMember) {
         
            F1.setDisable(false);  
            F2.setDisable(false);
            F3.setDisable(false);  
            F4.setVisible(true);
            saveBtn.setDisable(false);

           
            SessionMember selectedMember = sessionMemberTable.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                IN.setText(String.valueOf(selectedMember.getMemberId())); 
                F1.setText(selectedMember.getAnswers());                  
                F2.setText(selectedMember.getAttendance());              
                F3.setText(selectedMember.getFeedback());                
                F4.setText(String.valueOf(selectedMember.getSessionId())); 
            } else {
                showAlert("Please select a member entry to edit.");
            }
        }
    }
    @FXML
    public void savebtn(ActionEvent event) {
       
        saveBtn.setDisable(true);
        F1.setDisable(true);
        F2.setDisable(true);
        date.setDisable(true);

        if (isLeader) {

        	date.setVisible(true);
        	F3.setVisible(true);
            int leaderId;
            try {
                leaderId = Integer.parseInt(IN.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid numeric Leader ID.");
                return;
            }

            String role = F1.getText().trim();
            String attendance = F2.getText().trim();
            String notes = F3.getText().trim(); 
             leaderId=Integer.parseInt(IN.getText());
             int sessionId=Integer.parseInt(F4.getText());
            if (role.isEmpty() || attendance.isEmpty() || notes.isEmpty()) {
                showAlert("Please fill all fields before saving.");
                return;
            }

           
            String query = "UPDATE \"IMPACT Club\".sessionleader SET role = ?, attendance = ?, leadernotes = ? WHERE leaderid = ? and sessionid=?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, role);
                
                PGobject attendanceObj = new PGobject();
                attendanceObj.setType("\"IMPACT Club\".isattendence");
                attendanceObj.setValue(attendance);
                ps.setObject(2, attendanceObj);

                ps.setString(3, notes);
                ps.setInt(4, leaderId);
                ps.setInt(5, sessionId);
                
                int result = ps.executeUpdate();
                if (result > 0) {
                    showAlert("Leader entry updated successfully.");
                    loadLeaderData(); // Reload leader table data
                } else {
                    showAlert("Leader ID not found or no changes made.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("An error occurred while updating the leader entry.");
            }

        } else if (isSession) {
          
            int sessionId;
            try {
                sessionId = Integer.parseInt(IN.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Please enter a valid numeric Session ID.");
                return;
            }

            String topic = F1.getText().trim();
            String durationString = F2.getText().trim();
            LocalDate sessionDate = date.getValue();

            if (topic.isEmpty() || durationString.isEmpty() || sessionDate == null) {
                showAlert("Please fill all fields before saving.");
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Time duration;
            try {
                duration = new Time(sdf.parse(durationString).getTime());
            } catch (ParseException e) {
                showAlert("Invalid time format. Please enter duration as HH:mm:ss.");
                return;
            }

            String query = "UPDATE \"IMPACT Club\".session SET topic = ?, duration = ?, sessiondate = ? WHERE sessionid = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, topic);
                ps.setTime(2, duration); 
                ps.setDate(3, java.sql.Date.valueOf(sessionDate));
                ps.setInt(4, sessionId);

                int result = ps.executeUpdate();
                if (result > 0) {
                    showAlert("Session entry updated successfully.");
                    loadSessionData(); 
                } else {
                    showAlert("Session ID not found or no changes made.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("An error occurred while updating the session entry.");
            }
        }
        else if (isMember) {
            try {
                int memberId = Integer.parseInt(IN.getText().trim());
                int sessionId = Integer.parseInt(F4.getText().trim()); 
                String answers = F1.getText().trim();
                String attendance = F2.getText().trim();
                String feedback = F3.getText().trim();

             
                if (answers.isEmpty() || attendance.isEmpty() || feedback.isEmpty()) {
                    showAlert("Please fill all fields for the member entry.");
                    return;
                }

             
                if (!attendance.equals("Present") && !attendance.equals("Absent")) {
                    showAlert("Attendance must be either 'Present' or 'Absent'.");
                    return;
                }

                PGobject attendanceObj = new PGobject();
                attendanceObj.setType("\"IMPACT Club\".isattendence"); 
                attendanceObj.setValue(attendance);

               
                String query = "UPDATE \"IMPACT Club\".sessionmember " +
                               "SET answers = ?, attendance = ?, feedback = ? " +
                               "WHERE memberid = ? AND sessionid = ?";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setString(1, answers);
                    ps.setObject(2, attendanceObj);
                    ps.setString(3, feedback);
                    ps.setInt(4, memberId);
                    ps.setInt(5, sessionId); 

                    int result = ps.executeUpdate();
                    if (result > 0) {
                        showAlert("Member entry updated successfully.");
                        loadMemberData(); 
                    } else {
                        showAlert("Update failed. No changes were made.");
                    }
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                showAlert("An error occurred while updating the member entry.");
            }
        }
        
        clearFields();
    }

   
    private void clearFields() {
        IN.clear();
        F1.clear();
        F2.clear();
        F3.clear();
        F4.clear();
        date.setValue(null);
    }
    @FXML
    public void deletebtn(ActionEvent event) {
       
        saveBtn.setDisable(false);
        F2.setDisable(false);
        F1.setDisable(false);	
         
        if (isLeader) {
           
        	F4.setVisible(true);
        	F4.setDisable(false);
            date.setVisible(false);
            F3.setDisable(false);
            SessionLeader selectedLeader = sessionLeaderTable.getSelectionModel().getSelectedItem();
            if (selectedLeader != null) {
                IN.setText(String.valueOf(selectedLeader.getLeaderId())); 
                
                F4.setText(String.valueOf(selectedLeader.getSessionId()));
                String query = "delete from \"IMPACT Club\".sessionleader   WHERE sessionid = ? and leaderid=?";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                	ps.setInt(1, selectedLeader.getSessionId());
                    ps.setInt(2, selectedLeader.getLeaderId());
                   
                    

                    int result = ps.executeUpdate();
                    if (result > 0) {
                        showAlert("leader entry deleted successfully.");
                        loadLeaderData(); 
                    } else {
                        showAlert("leader ID not found or no changes made.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("An error occurred while deleting the leader entry.");
                }
                
               
            } else {
                showAlert("Please select a leader entry to edit.");
            }
           
        } else if(isSession){
           
            date.setVisible(true);
            date.setDisable(false);

            
            Session selectedSession = sessionTable.getSelectionModel().getSelectedItem();
            if (selectedSession != null) {
                IN.setText(String.valueOf(selectedSession.getSessionId()));
                F1.setText(selectedSession.getTopic()); 
                F2.setText(selectedSession.getDuration()); 
                date.setValue(selectedSession.getSessionDate());
                String query = "delete from \"IMPACT Club\".session  WHERE sessionid = ? ";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                	ps.setInt(1, selectedSession.getSessionId());
                 
                   
                    

                    int result = ps.executeUpdate();
                    if (result > 0) {
                        showAlert("Session entry deleted successfully.");
                        loadSessionData();
                    } else {
                        showAlert("Session ID not found or no changes made.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("An error occurred while deleting the Session entry.");
                }
            } else {
                showAlert("Please select a session entry to delete.");
            }
        }
        else if (isMember) {
          
            F4.setVisible(true);
            F4.setDisable(false);
            date.setVisible(false);
            F3.setDisable(false);

           
            SessionMember selectedMember = sessionMemberTable.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                IN.setText(String.valueOf(selectedMember.getMemberId()));
                F4.setText(String.valueOf(selectedMember.getSessionId()));

                String query = "DELETE FROM \"IMPACT Club\".sessionmember WHERE sessionid = ? AND memberid = ?";
                try (PreparedStatement ps = connection.prepareStatement(query)) {
                    ps.setInt(1, selectedMember.getSessionId());
                    ps.setInt(2, selectedMember.getMemberId());

                    int result = ps.executeUpdate();
                    if (result > 0) {
                        showAlert("Member entry deleted successfully.");
                        loadMemberData(); 
                    } else {
                        showAlert("Member ID not found or no changes made.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("An error occurred while deleting the member entry.");
                }
            } else {
                showAlert("Please select a member entry to edit.");
            }
        }    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
