/**
 * Class: CareHome (using Singleton design pattern)
 * Description: This is the heart of the application and the model in the MVC design. 
 * It uses the Singleton design patter so there can only be a single instance. For this
 * reason, the main constructor loads the previous state of this object from a binary file. It will also 
 * be saved on exit. 
 * 
 * @author Marton Marek
 */
package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

import application.auditor.*;
import application.exceptions.AuditDataAccessException;
import application.exceptions.InvalidBedIndexException;
import application.exceptions.OutsideOfShiftTimeException;
import application.exceptions.ScheduleOutOfComplianceException;

public class CareHome implements Serializable {
	
	/**
	 *  Auto-generated 
	 */
	private static final long serialVersionUID = 1L;
	
	//file that stores the CareHome instance details
	public static final String SAVE_FILE = "care.ser";
	
	//this stores the details of the user that is currently logged in to the system
	private AbstractUser currentUser;
	
	//keeps track of the currently selected resident 
	private Resident selectedResident;
	
	//stores the wards
	private ArrayList<Ward> wards = new ArrayList<Ward>();
	
	//stores employees
	private Employees employees = new Employees();
	
	//stores the schedule
	private Schedule schedule = new Schedule();
	
	/**
	 * SINGLETON Pattern for CareHome
	 */
	private static volatile CareHome homeInstance;
	
	/**
	 * This method returns the instance of CareHome. If an instance
	 * doesn't yet exist it will create it 
	 * @return CareHome instance 
	 */
	public static CareHome getInstance() {
		//if no instance yet exists..
		if (homeInstance == null) {
			
			//create syncronized so multiple threads can't create and access at the same time..
			synchronized (CareHome.class) {
				if (homeInstance == null) {
					homeInstance = new CareHome();
				}
			}
		}
		
		return homeInstance;
	}
	
	
	/**
	 * Default private constructor means that this class can only be instantiated
	 * by the use of the getInstance method. 
	 */
	private CareHome() {
		
		//prevent creation by reflection (singleton)
		if (homeInstance != null) {
			throw new RuntimeException("This is a singleton - need to use getInstance()");
		}
		
		//load the saved file to create the ONLY instance of CareHome through de-serialization 
		loadFile();
	}
	
	/**
	 * Internal helper function that is used to load the saved file for the CareHome instance
	 */
	@SuppressWarnings("unchecked")
	private void loadFile() {
		
		//when CareHome in created, it will immediately load its instance details from a file. 
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(SAVE_FILE);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			wards = (ArrayList<Ward>) in.readObject();
			employees = (Employees) in.readObject();
			schedule = (Schedule) in.readObject();
			in.close();
			fileIn.close();

		} 
		catch (FileNotFoundException e1) {
			
			//if the file is not found, we start with a new care home instance 
			System.out.println("File was not found!");
			
			//create new wards
			Ward wardOne = new Ward();
			Ward wardTwo = new Ward();
			
			//add the wards to our wards list
			wards.add(wardOne);
			wards.add(wardTwo);
			
			//we also need to create a first manager so they can log in
			// creates a manager that can login initially with user:admin pws: admin
			Manager admin = new Manager("DefaultManager", "DefaultManager"); 
			
			try {
				addEmployee(admin);
			} catch (AuditDataAccessException e) {
				
			}

		}
		catch (IOException ioe) {
			
			//something is wrong with the file and we cannot read it
			System.out.println("IOException in reading the file");
		}
		catch (ClassNotFoundException cfe) {
			
			//critical error: a class attempted to load could not be found. 
			System.out.println("Class could not be found while reading the file");
		} 
		
	}
	
	/**
	 * This method saves the carehome instance using serialization to a file
	 */
	public void saveToFile() {
		
		try {	
			//save application after exit
			FileOutputStream fileOut = new FileOutputStream(SAVE_FILE);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(CareHome.getInstance().getWards());
			out.writeObject(CareHome.getInstance().getEmployees());
			out.writeObject(CareHome.getInstance().getSchedule());
			out.close();
			fileOut.close(); 
		}
		catch (IOException ioe) {
			System.out.println("IOException in reading the file");
		}
	}
	
	/**
	 * sets the current user to the user that is logged in. It will also
	 * save the login action to the audit file
	 * @param user
	 */
	public void setCurrentUser(AbstractUser user) {
		this.currentUser = user;
		
		Auditor.getInstance().saveToAudit(new ActionLogin(user));
	}
	
	/**
	 * Adds the provided new user to the employees list and to the database
	 * This action is also saved to the audit file
	 * 	
	 * @param newUser
	 * @throws AuditDataAccessException
	 */
	public void addEmployee(AbstractUser newUser) throws AuditDataAccessException {
		employees.addEmployee(newUser);
		Auditor.getInstance().saveToAudit(new ActionAddEmployee(currentUser, newUser));
		
		//now save the new employee to the database
		Auditor.getInstance().saveEmployee(newUser);
	}
	
	/**
	 * Deletes the specified user from the employees list. If the user was a medical staff
	 * their schedule will also be cleansed from the system. 
	 * 
	 * Employee information is no deleted from the audit database as that may be required for
	 * historical audit use.
	 * 
	 * The action (user deletion) is recorded in the audit file
	 * 
	 * @param user
	 */
	public void deleteEmployee(AbstractUser user) {
		//save to audit file first...
		Auditor.getInstance().saveToAudit(new ActionRemoveEmployee(currentUser, user));
		
		//then delete employee
		employees.deleteEmployee(user);
		
		//if the employee was a medical staff we also delete their schedule
		if (user instanceof MedicalStaff) {
			schedule.deleteShiftList((MedicalStaff)user);
		}

	}
	
	/**
	 * returns true only if the given username does not already exists
	 * Is used when creating new users to ensure we don't end up with identical 
	 * usernames for any users.
	 * 
	 * @param username
	 * @return
	 */
	public boolean validateNewEmployeeUsername(String username) {
		return employees.validateUsername(username);
	}

	/**
	 * Updates the shift belonging to the given staff member with the new
	 * information located in the given shift list
	 * 
	 * @param staff
	 * @param sList
	 */
	public void updateShift(MedicalStaff staff, ShiftList sList) {
		schedule.updateShiftList(staff, sList);
	}
	
	
	/**
	 * will return true if the given user is within the bounds of their shift 
	 * start and end time right now. used to validate whether a user can login
	 * at the attempted time
	 * 
	 * @param user
	 * @throws OutsideOfShiftTimeException
	 */
	public void validateShift(AbstractUser user) throws OutsideOfShiftTimeException {
    	
		if (user instanceof MedicalStaff) {
			if (!schedule.hasShift(user)) {
				throw new OutsideOfShiftTimeException("User does not currently have a shift.");
			}
		}
    }

	/**
	 * Will return true if the current logged in user is within the bounds of their shift times.
	 * This is used to check whether an action can be performed by a user, that logged in during their shift
	 * but their shift has expired while they were logged in. 
	 * 
	 * @throws OutsideOfShiftTimeException
	 */
	public void validateShift() throws OutsideOfShiftTimeException {
    	
		if (currentUser instanceof MedicalStaff) {
			if (!schedule.hasShift(currentUser)) {
				throw new OutsideOfShiftTimeException("User does not currently have a shift.");
			}
		}
    } 
		
	/**
	 * returns the resident object based on the provided bed ID. If the bed does not
	 * contain a resident, it returns null
	 * 
	 * @param bedID
	 * @return Resident 
	 */
	public Resident getResident(String bedID) {
		
		String [] tokens = bedID.split("_");
		
		int wardIndex = Integer.parseInt(tokens[1]);
		int bedIndex = Integer.parseInt(tokens[2]);
		
		//need to adjust ward and bed index (As arrays begin with 0 not 1)
		wardIndex--;
		bedIndex--;
		
		try {
			
			if (wards.get(wardIndex) != null) {
				return wards.get(wardIndex).getResident(bedIndex);
			}
		}
		catch (IndexOutOfBoundsException ie) {
			System.out.println(ie.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Will assign the given resident to a bed given the ward ID and bed ID
	 * 
	 * @param wardIndex
	 * @param bedIndex
	 * @param resident
	 * @throws AuditDataAccessException
	 * @throws IndexOutOfBoundsException
	 */
	public void setResident(int wardIndex, int bedIndex, Resident resident) throws AuditDataAccessException, IndexOutOfBoundsException{
		//need to adjust ward and bed index (As arrays begin with 0 not 1)
		wardIndex--;
		bedIndex--;
		
		try {
			if (wards.get(wardIndex) != null) {
				wards.get(wardIndex).setResident(bedIndex, resident);
				
				//need to save this action to the audit log
				Auditor.getInstance().saveToAudit(new ActionAddResident(currentUser, resident));
				
				//now save the new resident to the database
				Auditor.getInstance().saveResident(resident);
			}
		}
		catch (IndexOutOfBoundsException ie) {
			throw new IndexOutOfBoundsException("Critical error, could not add resident.");
		}
	}
	
	/**
	 * Used to keep track of which resident is currently selected by the user
	 * @param selected
	 */
	public void setSelectedResident(Resident selected) {
		this.selectedResident = selected;
	}
	
	/**
	 * Returns the currently selected resident 
	 * @return Resident 
	 */
	public Resident getSelectedResident() {
		return selectedResident;
	}
	
	/**
	 * Used to swap out a resident for another in the provided ward and bed
	 * 
	 * @param wardIndex
	 * @param bedIndex
	 * @param resident
	 */
	public void replaceResident(int wardIndex, int bedIndex, Resident resident) 
													throws InvalidBedIndexException {
		
		//need to adjust ward and bed index (As arrays begin with 0 not 1)
		wardIndex--;
		bedIndex--;
	
		if (wards.get(wardIndex) != null) {
			wards.get(wardIndex).replaceResident(bedIndex, resident);
		}
		
	}
	
	/**
	 * Deletes a resident at the provided ward and bed ID
	 * (the resident will be deleted from the system but not from the audit database)
	 * 
	 * @param wardIndex
	 * @param bedIndex
	 */
	public void deleteResident(int wardIndex, int bedIndex) {
		//need to adjust ward and bed index (As arrays begin with 0 not 1)
		wardIndex--;
		bedIndex--;
		
		try {
			
			if (wards.get(wardIndex) != null) {
				
				//save action to audit log
				Auditor.getInstance().saveToAudit(new ActionRemoveResident(currentUser, wards.get(wardIndex).getResident(bedIndex)));
				
				wards.get(wardIndex).deleteResident(bedIndex);
	
			}
		}
		catch (IndexOutOfBoundsException ie) {
			System.out.println(ie.getMessage());
		}
	}

	/**
	 * checks whether the schedule is compliant (every nurse shift filled and at least 
	 * one doctor shift per day)
	 * 
	 * @throws ScheduleOutOfComplianceException
	 */
	public void checkCompliance() throws ScheduleOutOfComplianceException {
		
		//if we are not compliant then throw out of compliance exception
		if(!schedule.checkScheduleCompliance(employees)) {
			throw new ScheduleOutOfComplianceException("Schedule is out of compliance.");
		}

	}
	
	/**
	 * returns the list of all historical and current employees from the database
	 * for the drop down selection in the audit menu
	 */
	public ArrayList<String> getAllEmployeesList() throws AuditDataAccessException {
		return Auditor.getInstance().loadEmployees();
	}
	
	/**
	 * returns a list of all historical and current residents from the database
	 * for the drop down selection in the audit menu
	 */
	public ArrayList<String> getAllResidentsList() throws AuditDataAccessException {
		return Auditor.getInstance().loadResidents();
	}
	
	public ArrayList<String> getAuditReport(LocalDateTime start, LocalDateTime end, 
												String eUUID, String rUUID) throws AuditDataAccessException {
		
		return Auditor.getInstance().loadAuditReport(start, end, eUUID, rUUID);
	}
	
	/**
	 * checks to see if there is only one last manager left in the system
	 * (this is to make sure we don't allow the deletion of the last manager)
	 * 
	 * @return boolean
	 */
	public boolean isLastManager() {
		return employees.isLastManager();
	}
	

	/**
	 * Getter methods
	 * @return
	 */
	
	public AbstractUser getCurrentUser() {
		return currentUser;
	}
	
	public int getWardSize() {
		return wards.size();
	}
	
	public ArrayList<Ward> getWards() {
		return wards;
	}
	
	public Employees getEmployees() {
		return employees;
	}
	
	public Schedule getSchedule() {
		return schedule;
	}
	
	public ShiftList getEmployeeShiftList(MedicalStaff staff) {
		return schedule.getShiftList(staff);
	}

}
