package application;

public class SessionUser {
	
	    private final int sessionId;
	    private final String sessionName;
	    private final String answers;
	    private final String attendance;
	    private final String feedback;

	    public SessionUser(int sessionId, String sessionName, String answers, String attendance, String feedback) {
	        this.sessionId = sessionId;
	        this.sessionName = sessionName;
	        this.answers = answers;
	        this.attendance = attendance;
	        this.feedback = feedback;
	    }

	    public int getSessionId() { return sessionId; }
	    public String getSessionName() { return sessionName; }
	    public String getAnswers() { return answers; }
	    public String getAttendance() { return attendance; }
	    public String getFeedback() { return feedback; }
	
}
