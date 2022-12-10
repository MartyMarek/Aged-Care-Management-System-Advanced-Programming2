/**
 * Class: ActionUpdateEmployee
 * Description: Creates an action that can be saved to file by the auditor, when an employee is changed in the system 
 * 
 * @author Marton Marek
 * 
 */
package application.auditor;

import application.AbstractUser;


public final class ActionUpdateEmployee extends AbstractAction {

	//additional objects to store
	String updatedEmployee;
	
	/**
	 * Default Constructor that saves the current user and updated users details
	 * @param employee
	 * @param updatedEmployee
	 */
	public ActionUpdateEmployee(AbstractUser employee, AbstractUser updatedEmployee) {
		super(employee);
		this.updatedEmployee = updatedEmployee.getEmployeeID();
		description = "Timestamp: " + time + "\n" 
				+ "User with ID: " + employee.getEmployeeID() + " updated employee with ID: " + updatedEmployee.getEmployeeID() + "\n"
				+ "Usernames: " + employee.getUsername() + " updated employee " + updatedEmployee.getUsername() + "\n" + "*"; 
	}
}
