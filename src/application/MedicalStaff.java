/**
 * Abstract Class: MedicalStaff
 * Description: Medical staff can all check resident details and they all have a shift schedule. 
 * 
 * @author Marton Marek
 */

package application;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class MedicalStaff extends AbstractUser implements Serializable {
	
	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	//the list of allowable start times for a shift 
	protected int[] allowedShiftStartTimes;
	
	//the length of the shift 
	protected int shiftLength;
	
	/**
	 * Default constructor 
	 * @param firstName
	 * @param surname
	 * @param username
	 * @param password
	 * @param dob
	 * @param job
	 */
	public MedicalStaff(String firstName, String surname, String username, String password, LocalDate dob, JobRole job) {
		super(firstName, surname, username, password, dob, job);
	}

	/**
	 * This method will return the allowed start shift times for this employee. The allowed start
	 * times depend on whether the employee is a nurse or a doctor.
	 * @return
	 */
	public String[] getAllowedShiftStartTimes() {
		String[] temp = new String[allowedShiftStartTimes.length];
		
		//set the first one to "None" instead of showing 0 - this is more intuitive for the user
		temp[0] = "None";
		
		for (int i = 1; i < allowedShiftStartTimes.length; i ++) {
			temp[i] = String.valueOf(allowedShiftStartTimes[i] + ":00");
		}
		return temp;
	}
	
	/**
	 * returns the shift length for this employee. This also depends on 
	 * whether the employee is a nurse (8 hours) or a doctor (1 hour)
	 * @return
	 */
	public int getShiftLength() {
		return shiftLength;
	}
	
	/**
	 * subclasses must implement this to initialise an employees 
	 * shift schedule based on their allowed start times. (We can't have a null schedule)
	 */
	protected abstract void initialiseStartTimes();
	

}
