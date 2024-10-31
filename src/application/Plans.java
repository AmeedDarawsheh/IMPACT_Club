package application;
import java.sql.Date;

public class Plans extends FanancialPlan {

	private int numberOfPlan;
	private String result;
	private String goalOfPlan;
	private Date date;
	
	public int getNumberOfPlan() {
		return numberOfPlan;
	}
	public void setNumberOfPlan(int numberOfPlan) {
		this.numberOfPlan = numberOfPlan;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getGoalOfPlan() {
		return goalOfPlan;
	}
	public void setGoalOfPlan(String goalOfPlan) {
		this.goalOfPlan = goalOfPlan;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}	
}