/**
 * Class: Employees
 * Description: Holds an Arraylist of all employees in the system. Will return employees as a whole list or
 * as individual users based on a given id.
 * 
 * @author Marton Marek
 */
package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


public class Employees implements Serializable {
	
	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	//stores a list of employees
	ArrayList<AbstractUser> employees;
	
	/**
	 * Default constructor
	 */
	public Employees () {
		employees = new ArrayList<AbstractUser>();
	}
	

	/**
	 * returns an array of all employee types in the system
	 * @return String[]
	 */
	public static String[] getEmployeeTypes() {
		ArrayList<String> types = new ArrayList<String>();
		
		JobRole allJobs [] = JobRole.values();
		
		for(JobRole j : allJobs) {
			types.add(j.jobName);		
		}
		
		//return as a primitive string array
		String[] jobTypes = new String[types.size()];
		jobTypes = types.toArray(jobTypes);
		
		return jobTypes;
	}
	
	/**
	 * adds a new employee to the employees list
	 * @param user
	 */
	public void addEmployee(AbstractUser user) {
		employees.add(user);
	}
	
	/**
	 * deletes an employee based on the employee object given
	 * @param user
	 */
    public void deleteEmployee(AbstractUser user) {
		employees.remove(user);
	}
    

    /**
     * returns true if the username given does NOT already exist. false if it does exist. 
     * 
     * @param username
     * @return boolean
     */
    public boolean validateUsername(String username) {
    	Iterator<AbstractUser> iter = employees.iterator();
    	
    	while (iter.hasNext()) {
    		AbstractUser user = iter.next();
    		
    		if (user.getUsername().equals(username)) {
    			return false;    			
    		}
    	}
    	return true;
    }
    
    /**
     * returns the employees arraylist
     * @return ArrayList<AbstractUser>
     */
    public ArrayList<AbstractUser> getEmployeeList() {
    	return employees;
    }
    
    /**
     * returns an arraylist of all medical staff
     * @return ArrayList<MedicalStaff>
     */
    public ArrayList<MedicalStaff> getMedicalEmployeeList() {
    	
    	ArrayList<MedicalStaff> temp = new ArrayList<MedicalStaff>();
    	
    	//iterate through our employees and check which ones are medical staff
    	Iterator<AbstractUser> iter = employees.iterator();
    	
    	while (iter.hasNext()) {
    		AbstractUser user = iter.next();
    		
    		if (user instanceof MedicalStaff) {
    			temp.add((MedicalStaff)user);
    		}
    	}
    	return temp;
    }
	
    /**
     * returns a user object based on the given id. if the user 
     * with given ID does not exist null is returned 
     * @param id
     * @return
     */
    public AbstractUser getEmployee(String id) {
    	
    	//iterate through the employee array and return the employee with the matching ID
    	Iterator<AbstractUser> iter = employees.iterator();
    	
    	while (iter.hasNext()) {
    		AbstractUser user = iter.next();
    		if (user.getEmployeeID().equals(id)) {
    			return user;
    		}
    	}
    	return null;
    }
    
    /**
     * checks to see if we only have one manager left in the system
     * 
     * @return boolean
     */
	public boolean isLastManager() {
		Iterator<AbstractUser> iter = employees.iterator();
    	int count = 0;
    	
    	while (iter.hasNext()) {
    		AbstractUser user = iter.next();
    		if (user instanceof Manager) {
    			count++;
    		}
    	}
    	
    	if(count > 1) {
    		return false;
    	}
    	else {
    		return true;
    	}
    	
	}

}
