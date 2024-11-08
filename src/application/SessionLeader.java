package application;
public class SessionLeader {
    private Integer sessionId;
    private Integer leaderId;
    private String role;
    private String leaderNotes;
    private String attendance;

    public SessionLeader(Integer sessionId, Integer leaderId, String role, String leaderNotes, String attendance) {
        this.sessionId = sessionId;
        this.leaderId = leaderId;
        this.role = role;
        this.leaderNotes = leaderNotes;
        this.attendance = attendance;
    }

    // Getter methods
    public Integer getSessionId() {
        return sessionId;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public String getRole() {
        return role;
    }

    public String getLeaderNotes() {
        return leaderNotes;
    }

    public String getAttendance() {
        return attendance;
    }
}
