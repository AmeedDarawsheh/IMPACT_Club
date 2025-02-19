package application;

public class SessionMember {
    private final int  sessionId;
    private final int memberId;
    private final String memberName;
    private final String answers;
    private final String feedback;
    private final String attendance;

    public SessionMember(int sessionId, int memberId, String memberName, String answers, String feedback, String attendance) {
        this.sessionId = (sessionId);
        this.memberId = (memberId);
        this.memberName = (memberName);
        this.answers =(answers);
        this.feedback = (feedback);
        this.attendance = (attendance);
    }

    public int getSessionId() {
        return sessionId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getAnswers() {
        return answers;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getAttendance() {
        return attendance;
    }
}
