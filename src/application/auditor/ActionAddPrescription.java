/**
 * Class: ActionAddPrescription
 * Description: Creates an action that can be saved to file by the auditor, when a new prescription is created for a resident
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;
import application.Prescription;
import application.Resident;


public final class ActionAddPrescription extends AbstractAction {

	//additional objects to store
	String residentID;
	
	/**
	 * Default constructor that saves the resident, prescription and current user details into this action
	 * @param employee
	 * @param resident
	 * @param pres
	 */
	public ActionAddPrescription(AbstractUser employee, Resident resident, Prescription pres) {
		super(employee);
		this.residentID = resident.getID();
		
		description = "Timestamp: " + time + "\n" 
				+ "User with ID: " + employee.getEmployeeID() + " added prescription to resident with ID: " + resident.getID() + "\n"
				+ "Username: " + employee.getUsername() + " added prescription to resident with name: " + resident.getFirstName() + " "
				+ resident.getSurname() + "\n" + "Prescription details: " + pres.toString() + "\n*"; 

	}
}
