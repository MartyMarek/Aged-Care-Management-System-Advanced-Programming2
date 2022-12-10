/**
 * Abstract Class: AbstractAction
 * Description: Can be extended to store specific audit actions that are saved by the Auditor.
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import application.AbstractUser;


public abstract class AbstractAction {

	//who performed the action
	protected AbstractUser employee;
	
	//date and time of the action
	protected String time; 
	protected DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm");
	
	//holds the description of the action 
	protected String description;
	
	/**
	 * Default Constructor
	 * @param employee
	 */
	public AbstractAction(AbstractUser employee) {
		this.employee = employee;
		
		//create the timestamp when the action class is instantiated 
		time = timeFormat.format(LocalDateTime.now());
	}

	/**
	 * Getter and Setter Methods
	 * @return
	 */
	public String getEmployeeID() {
		return employee.getEmployeeID();
	}
	
	public String getTimeStamp() {
		return time;
	}
	
	public String getDescription() {
		return description;
	}
	
	
}
