/**
 * Interface: AuditDatabaseInterface
 * Description: Used to create the Data Access Object design pattern. The Auditor object implements
 * this interface and calls on a service object that also implements this interface and supplies the Auditor 
 * with the implementation details for accessing the data. 
 * 
 * @author Marton Marek
 */

package application.database;

import java.time.LocalDateTime;
import java.util.ArrayList;

import application.AbstractUser;
import application.Resident;
import application.exceptions.AuditDataAccessException;


public interface AuditDatabaseInterface {
	
	public void saveEmployee(AbstractUser user) throws AuditDataAccessException;
	public void saveResident(Resident resident) throws AuditDataAccessException;
	public void saveAdministrationRecord(Record record) throws AuditDataAccessException;
	public void savePrescriptionRecord(Record record) throws AuditDataAccessException;
	
	public ArrayList<String> loadEmployees() throws AuditDataAccessException;
	public ArrayList<String> loadResidents() throws AuditDataAccessException;
	
	public ArrayList<String> loadAuditReport(LocalDateTime start, LocalDateTime end, 
												String eUUID, String rUUID) throws AuditDataAccessException;

}
