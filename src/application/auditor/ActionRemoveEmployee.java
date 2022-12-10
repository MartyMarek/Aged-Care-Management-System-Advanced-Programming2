/**
 * Class: ActionRemoveEmployee
 * Description: Creates an action that can be saved to file by the auditor, when a new employee is deleted from the system 
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;


public final class ActionRemoveEmployee extends AbstractAction {

	//additional objects to store
	String removedEmployee;
	
	/**
	 * Default Constructor that saves the current user and deleted employees details 
	 * @param employee
	 * @param removedEmployee
	 */
	public ActionRemoveEmployee(AbstractUser employee, AbstractUser removedEmployee) {
		super(employee);
		this.removedEmployee = removedEmployee.getEmployeeID();
		description = "Timestamp: " + time + "\n" 
				+ "User with ID: " + employee.getEmployeeID() + " removed employee with ID: " + removedEmployee.getEmployeeID() + "\n"
				+ "Usernames: " + employee.getUsername() + " removed employee " + removedEmployee.getUsername() + "\n" + "*"; //the * makes for better readibility 
	}
}
