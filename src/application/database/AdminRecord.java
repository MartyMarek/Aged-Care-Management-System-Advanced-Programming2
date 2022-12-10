/**
 * Class: AdminRecord
 * Description: Used to store the detail of an audit record in relation to the administration of medicine to a resident  
 * 
 * @author Marton Marek
 */

package application.database;

import java.time.LocalDateTime;


public class AdminRecord extends Record {
		
	//Specific attributes for an administration record
	String medicine;
	int dose;	
	
	/**
	 * Default Constructor 
	 * @param timeStamp
	 * @param employeeID
	 * @param patientID
	 * @param medicine
	 * @param dose
	 */
	public AdminRecord(LocalDateTime timeStamp, String employeeID, String patientID,
						String medicine, int dose) {
		
		super(timeStamp, employeeID, patientID);
		this.medicine = medicine;
		this.dose = dose;
	}

	/**
	 * Getters and Setter methods 
	 * @return
	 */
	public String getMedicine() {
		return medicine;
	}

	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}

	public int getDose() {
		return dose;
	}

	public void setDose(int dose) {
		this.dose = dose;
	}
	

}
