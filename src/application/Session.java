package application;
import java.time.LocalDate;

public class Session {
    private Integer sessionId;
    private LocalDate sessionDate;
    private String topic;
    private String duration;

    public Session(Integer sessionId, LocalDate sessionDate, String topic, String duration) {
        this.sessionId = sessionId;
        this.sessionDate = sessionDate;
        this.topic = topic;
        this.duration = duration;
    }

    // Getter methods
    public Integer getSessionId() {
        return sessionId;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public String getTopic() {
        return topic;
    }

    public String getDuration() {
        return duration;
    }
}
