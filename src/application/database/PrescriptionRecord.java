/**
 * Class: PrescriptionRecord 
 * Description: Used to store the details of a new prescription record created for a resident 
 * 
 * @author Marton Marek
 */

package application.database;

import java.time.LocalDateTime;

import application.Prescription;


public class PrescriptionRecord extends Record {

	//holds the prescription details
	Prescription pres;
	String action;
	
	/**
	 * Default constructor with timestamp, current user and resident id
	 * @param timeStamp
	 * @param employeeID
	 * @param patientID
	 */
	public PrescriptionRecord(LocalDateTime timeStamp, String employeeID, 
								String patientID, Prescription pres, String action) {
		super(timeStamp, employeeID, patientID);
		this.pres = pres;
		this.action = action;
		
	}
	
	/**
	 * Getters and Setter methods
	 * @return
	 */

	public Prescription getPres() {
		return pres;
	}

	public void setPres(Prescription pres) {
		this.pres = pres;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
