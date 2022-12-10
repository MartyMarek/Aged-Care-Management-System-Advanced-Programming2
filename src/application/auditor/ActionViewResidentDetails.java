/**
 * Class: ActionViewResident 
 * Description: Creates an action that can be saved to file by the auditor, when a residents details are viewed 
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;
import application.Resident;


public final class ActionViewResidentDetails extends AbstractAction {

	//additional objects to store
	String residentID;
	
	/**
	 * Default constructor that saves the current user and resident who was viewed 
	 * @param employee
	 * @param resident
	 */
	public ActionViewResidentDetails(AbstractUser employee, Resident resident) {
		super(employee);
		this.residentID = resident.getID();
		description = "Timestamp: " + time + "\n" 
						+ "Action: User with ID: " + employee.getEmployeeID() + " has viewed details of resident with the ID: " + resident.getID() + "\n"
						+ "Username: " + employee.getUsername() + " viewed resident details of: " + resident.getFirstName() + " " + resident.getSurname() + "\n" + "*"; 
	}
}
