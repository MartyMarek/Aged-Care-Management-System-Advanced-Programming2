/**
 * Class: ActionAddEmployee
 * Description: Creates an action that can be saved to file by the auditor, when a new employee is created in the system
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;


public final class ActionAddEmployee extends AbstractAction {

	//additional objects to store
	String newEmployeeID;
	
	/**
	 * Default constructor that creates the action. The details of the new employee and the details of the 
	 * user who created the new employee are both saved. 
	 * @param employee
	 * @param newEmployee
	 */
	public ActionAddEmployee(AbstractUser employee, AbstractUser newEmployee) {
		super(employee);
		//the first time the program is run employee will be null (as the new employee is added automatically)
		try {
			this.newEmployeeID = newEmployee.getEmployeeID();
			description = "Timestamp: " + time + "\n" 
							+ "User with ID: " + employee.getEmployeeID() + " added new employee with ID: " + newEmployee.getEmployeeID() + "\n"
							+ "Usernames: " + employee.getUsername() + " added new employee " + newEmployee.getUsername() + "\n" + "*"; //the * makes for better readibility 
		}
		catch (NullPointerException ne) {
			description = "Timestamp: " + time + "\n" + "Application run for the first time\n*";
		}
	}
}
