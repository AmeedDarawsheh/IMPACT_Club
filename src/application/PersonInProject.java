package application;

public class PersonInProject {
    private String memberId;
    private String name;
    private String roleInProject;
    private String feedback;
    private int projectInvolvementLevel;

    public PersonInProject(String memberId, String name, String roleInProject, String feedback, int projectInvolvementLevel) {
        this.memberId = memberId;
        this.name = name;
        this.roleInProject = roleInProject;
        this.feedback = feedback;
        this.projectInvolvementLevel = projectInvolvementLevel;
    }

    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getRoleInProject() { return roleInProject; }
    public String getFeedback() { return feedback; }
    public int getProjectInvolvementLevel() { return projectInvolvementLevel; }

	public void setRoleInProject(String newValue) {

		roleInProject=newValue;
	}

	public void setFeedback(String newValue) {
		
		feedback=newValue;
	}

	public void setName(String newValue) {
		
		name=newValue;
	}

	public void setProjectInvolvementLevel(Integer newValue) {
		
		projectInvolvementLevel=newValue;
	}
}
