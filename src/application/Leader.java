package application;

public class Leader {
	private String ssn;
    private String fullName;
    private String address;
    private String startDate;
    private String bod;
    private String userName;
    private String phoneNumber;
    private String gender;
    private String major;

    public Leader(String ssn, String fullName, String address, String startDate, String bod, String userName, String phoneNumber, String gender, String major ) {
        this.ssn = ssn;
        this.fullName = fullName;
        this.address = address;
        this.startDate = startDate;
        this.bod = bod;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
       
        this.major = major;
        
    }

    // Getters for each property
    public String getSsn() { return ssn; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getStartDate() { return startDate; }
    public String getBod() { return bod; }
    public String getUserName() { return userName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getGender() { return gender; }
    public String getMajor() { return major; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    
    public void setLastName(String fullName) { this.fullName = fullName; }
  
    public void setCity(String city) { this.address = city; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setBod(String bod) { this.bod = bod; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMajor(String major) { this.major = major; }
}
