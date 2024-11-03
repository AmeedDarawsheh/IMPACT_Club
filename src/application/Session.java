package application;
import java.sql.Date;
import java.util.Properties;

import javax.mail.Authenticator;
public class Session {

	private  int number;
	private String name;
	private Date date;
		
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}