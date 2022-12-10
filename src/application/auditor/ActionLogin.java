/**
 * Class: ActionLogin
 * Description: Creates an action that can be saved to file by the auditor, when a user logs into the system 
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import application.AbstractUser;


public final class ActionLogin extends AbstractAction {

	/**
	 * Default constructor that saves the action of the user that is logging in 
	 * @param employee
	 */
	public ActionLogin(AbstractUser employee) {
		super(employee);		
		description = "Timestamp: " + time + "\n" 
						+ "User with ID: " + employee.getEmployeeID() + " Username: " + employee.getUsername() 
						+ " has logged into the system.\n" + "*"; 
	}

}
