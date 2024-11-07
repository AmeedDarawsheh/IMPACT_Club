package application;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditMemberProjectFieldController {

    @FXML
    private TextField roleInProjectField;
    @FXML
    private TextField feedbackField;
    @FXML
    private TextField involvementLevelField;

    private PersonInProject person;
    private boolean saveClicked = false;

    public void setPerson(PersonInProject person) {
        this.person = person;

        
        roleInProjectField.setText(person.getRoleInProject());
        feedbackField.setText(person.getFeedback());
        involvementLevelField.setText(String.valueOf(person.getProjectInvolvementLevel()));
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
       
        person.setRoleInProject(roleInProjectField.getText());
        person.setFeedback(feedbackField.getText());
        person.setProjectInvolvementLevel(Integer.parseInt(involvementLevelField.getText()));

        saveClicked = true;

        
        Stage stage = (Stage) roleInProjectField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        
        Stage stage = (Stage) roleInProjectField.getScene().getWindow();
        stage.close();
    }
}
