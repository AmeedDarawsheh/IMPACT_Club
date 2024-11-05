package application;

public class ProjectLeader {
    private int id;
    private String leaderName;
    private String role;
    private String feedback;

    public ProjectLeader(int id, String leaderName, String role, String feedback) {
        this.id = id;
        this.leaderName = leaderName;
        this.role = role;
        this.feedback = feedback;
    }

    public int getId() {
        return id;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getRole() {
        return role;
    }

    public String getFeedback() {
        return feedback;
    }

	public void setFeedback(String feedback2) {
		
		feedback=feedback2;
	}

	public void setRole(String role2) {
		
		role=role2;
	}
}
