package application;

public class Member {
    private String name;
    private String phone;
    private String email;
    private Integer points;

    public Member(String name, String phone, String email, Integer points) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.points = points;
    }

    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public Integer getPoints() { return points; }
}
