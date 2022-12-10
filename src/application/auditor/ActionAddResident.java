/**
 * Class: ActionAddResident
 * Description: Creates an action that can be saved to file by the auditor, when a new resident is created in the system
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;
import application.Resident;


public final class ActionAddResident extends AbstractAction {

	//additional objects to store
	String residentID;
	
	/**
	 * Default constructor that creates the save description of the current user of the system and the created resident 
	 * @param employee
	 * @param resident
	 */
	public ActionAddResident(AbstractUser employee, Resident resident) {
		super(employee);
		this.residentID = resident.getID();
		description = "Timestamp: " + time + "\n" 
				+ "Action: User with ID: " + employee.getEmployeeID() + " has added a resident with the ID: " + resident.getID() + "\n"
				+ "Username: " + employee.getUsername() + " has added a resident: " + resident.getFirstName() + " " + resident.getSurname() + "\n" + "*"; 
		
	}
	
}
