/**
 * Class: ActionDeletePrescription
 * Description: Creates an action that can be saved to file by the auditor, when a prescription is deleted 
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;
import application.Prescription;
import application.Resident;


public final class ActionDeletePrescription extends AbstractAction {

	//additional objects to store
	String residentID;
	
	/**
	 * Default constructor that saves the current user, resident and deleted prescription into an action 
	 * @param employee
	 * @param resident
	 * @param pres
	 */
	public ActionDeletePrescription(AbstractUser employee, Resident resident, Prescription pres) {
		super(employee);
		this.residentID = resident.getID();
		
		description = "Timestamp: " + time + "\n" 
				+ "User with ID: " + employee.getEmployeeID() + " deleted prescription to resident with ID: " + resident.getID() + "\n"
				+ "Username: " + employee.getUsername() + " deleted prescription to resident with name: " + resident.getFirstName() + " "
				+ resident.getSurname() + "\n" + "Deleted prescription details: " + pres.toString() + "\n*"; 
	}
}
