package application;

public class ProjectField {
    private final int projectId;
    private final String projectName;
    private final String status;
    private final String roleInProject;
    private  String projectFeedback;
    private final int projectInvolvementLevel;

    public ProjectField(int projectId, String projectName, String status, String roleInProject, String projectFeedback, int projectInvolvementLevel) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.status = status;
        this.roleInProject = roleInProject;
        this.projectFeedback = projectFeedback;
        this.projectInvolvementLevel = projectInvolvementLevel;
    }

    public int getProjectId() { return projectId; }
    public String getProjectName() { return projectName; }
    public String getStatus() { return status; }
    public String getRoleInProject() { return roleInProject; }
    public String getProjectFeedback() { return projectFeedback; }
    public int getProjectInvolvementLevel() { return projectInvolvementLevel; }
    public void setFeedback(String Fee) {  projectFeedback=Fee; }
}
