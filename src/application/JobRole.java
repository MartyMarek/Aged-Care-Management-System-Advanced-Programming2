/**
 * Enumerator: JobRole
 * Description: Holds the names of the job roles that can be utilised in the system. 
 * The corresponding string refers to the class names. For additional job roles added, 
 * the classes must be added also.
 * 
 * @author Marton Marek
 */

package application;

public enum JobRole {

	ADMIN("Manager"),
	DOCTOR("Doctor"),
	NURSE("Nurse");
	
	String jobName;
	
	JobRole(String name) {
		jobName = name;
	}
	
}

