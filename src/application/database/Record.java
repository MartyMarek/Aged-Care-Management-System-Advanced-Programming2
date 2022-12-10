/**
 * Abstract Class: Record 
 * Description: Used to store the details of an audit record (system action), into the database by the Auditor. 
 * 
 * @author Marton Marek
 */

package application.database;

import java.time.LocalDateTime;


public abstract class Record {
	
	//these attributes will be included in all auditable resident actions. 
	protected LocalDateTime timeStamp;
	protected String employeeID;
	protected String patientID;
	
	/**
	 * Default Constructor with minimum details needed for a record object 
	 * @param timeStamp
	 * @param employeeID
	 * @param patientID
	 */
	public Record(LocalDateTime timeStamp, String employeeID, String patientID) {
		this.timeStamp = timeStamp;
		this.employeeID = employeeID;
		this.patientID = patientID;
	}

	/**
	 * Getter and Setter methods 
	 * @return
	 */
	public String getPatientID() {
		return patientID;
	}

	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

}
