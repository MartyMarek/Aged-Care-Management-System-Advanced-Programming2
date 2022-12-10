/**
 * Class: Auditor
 * Description: This Auditor class uses the Singleton design pattern to ensure there is only one instance. 
 * Depending on the type of action to audit, it can either store an audit action into a database or the log file. 
 * User actions are all stored into the audit log file and important (And externally auditable and reportable actions 
 * are all stored into the database). Access to the database is given by the AuditDatabaseInterace that this class 
 * implements. To save records to the database the AuditorDatabaseService must be used. (this is the Data Access Object 
 * Design pattern).
 * 
 * @author Marton Marek
 * 
 */

package application.auditor;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import application.AbstractUser;
import application.Resident;
import application.database.*;
import application.database.Record;
import application.exceptions.AuditDataAccessException;


public class Auditor implements AuditDatabaseInterface {

	//this is the file that all audit information will be saved to
	private static final String AUDIT_FILE = "actionlog.txt";
	
	//The AuditorService implements the database specific functions for our Data Access Object Pattern
	AuditorDatabaseService auditDBService;
	
	//This is the singleton instance of the auditor
	private static volatile Auditor auditorInstance;
		
	/**
	 * Private constructor (singleton pattern)
	 */
	private Auditor() {
		auditDBService = new AuditorDatabaseService();
	}
	
	/**
	 * Returns the singleton instance of this auditor 
	 * @return Auditor 
	 */
	public static Auditor getInstance() {
		//if no instance yet exists..
		if (auditorInstance == null) {
			
			//create syncronized so multiple threads can't create and access at the same time..
			synchronized (Auditor.class) {
				if (auditorInstance == null) {
					auditorInstance = new Auditor();
				}
			}
		}
		
		return auditorInstance;
	}
	
	
	/**
	 * Saves the provided action into the default audit log file
	 * @param action
	 */
	public void saveToAudit(AbstractAction action) {
		
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(AUDIT_FILE, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		    PrintWriter printWriter = new PrintWriter(bufferedWriter);
		    printWriter.println(action.getDescription());
		    printWriter.close();
	    
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * these functions save records into the database (implementing AuditDatabaseInterface)
	 */
	
	@Override
	public void saveEmployee(AbstractUser user) throws AuditDataAccessException {
		//call  the implementation through auditdatabaseservice 
		auditDBService.saveEmployee(user);
	}
	
	@Override
	public void saveResident(Resident resident) throws AuditDataAccessException {
		//call  the implementation through auditdatabaseservice 
		auditDBService.saveResident(resident);		
	}

	@Override 
	public void saveAdministrationRecord(Record record) throws AuditDataAccessException {
		//call  the implementation through auditdatabaseservice 
		auditDBService.saveAdministrationRecord(record);
	}

	@Override
	public void savePrescriptionRecord(Record record) throws AuditDataAccessException {
		//call  the implementation through auditdatabaseservice 
		auditDBService.savePrescriptionRecord(record);		
	}

	@Override
	public ArrayList<String> loadEmployees() throws AuditDataAccessException {
		return auditDBService.loadEmployees();
	}

	@Override
	public ArrayList<String> loadResidents() throws AuditDataAccessException {
		return auditDBService.loadResidents();
	}

	@Override
	public ArrayList<String> loadAuditReport(LocalDateTime start, LocalDateTime end, String eUUID,
			String rUUID) throws AuditDataAccessException {

		return auditDBService.loadAuditReport(start, end, eUUID, rUUID);
	}

	
	
	
	
}
