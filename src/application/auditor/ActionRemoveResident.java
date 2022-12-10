/**
 * Class: ActionRemoveResident
 * Description: Creates an action that can be saved to file by the auditor, when a resident is deleted from the system 
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;
import application.Resident;


public final class ActionRemoveResident extends AbstractAction {

	//additional objects to store
	String residentID;
	
	/**
	 * Default Constructor that saves the current user and the deleted residents details
	 * @param employee
	 * @param resident
	 */
	public ActionRemoveResident(AbstractUser employee, Resident resident) {
		super(employee);
		this.residentID = resident.getID();
		description = "Timestamp: " + time + "\n" 
				+ "Action: User with ID: " + employee.getEmployeeID() + " has removed resident with the ID: " + resident.getID() + "\n"
				+ "Username: " + employee.getUsername() + " has removed resident: " + resident.getFirstName() + " " + resident.getSurname() + "\n" + "*"; 
	}
}
