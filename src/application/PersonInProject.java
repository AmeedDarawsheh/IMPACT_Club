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
		// TODO Auto-generated method stub
		roleInProject=newValue;
	}

	public void setFeedback(String newValue) {
		// TODO Auto-generated method stub
		feedback=newValue;
	}

	public void setName(String newValue) {
		// TODO Auto-generated method stub
		name=newValue;
	}

	public void setProjectInvolvementLevel(Integer newValue) {
		// TODO Auto-generated method stub
		projectInvolvementLevel=newValue;
	}
}
