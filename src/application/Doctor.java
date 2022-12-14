/**
 * Class: Doctor
 * Description: Doctor is a type of medical staff and has a specific shift length and schedule
 * 
 * @author Marton Marek
 */

package application;

import java.time.LocalDate;

public class Doctor extends MedicalStaff {

	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	//doctors shifts are all one hour in length
	public static final int DOCTOR_SHIFT_LENGTH = 1;  
	
	/**
	 * Default constructor 
	 * @param firstName
	 * @param surname
	 * @param username
	 * @param password
	 * @param dob
	 */
	public Doctor(String firstName, String surname, String username, String password, LocalDate dob) {
		super(firstName, surname, username, password, dob, JobRole.DOCTOR);
		initialiseStartTimes();
		shiftLength = DOCTOR_SHIFT_LENGTH;
	}
	
	/**
	 * Doctors can bescheduled for one hour anytime between 8am - 10pm
	 * When a new doctors is created this will initialise their allowed shift times
	 */
	protected void initialiseStartTimes() {
		allowedShiftStartTimes = new int[16];
		int earliestStartTime = 8;
		
		//no shift is 0
		allowedShiftStartTimes[0] = 0;
		
		for (int i = 1; i < allowedShiftStartTimes.length; i++) {
			allowedShiftStartTimes[i] = earliestStartTime++;
		}
	}
	
}
