/**
 * Class: AuditDatabaseService
 * Description: Implements all of the database specific functions to save records into the database. 
 * This is part of the Data Access Object design pattern
 * 
 * @author Marton Marek
 */

package application.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import application.AbstractUser;
import application.Prescription;
import application.Resident;
import application.exceptions.AuditDataAccessException;


public class AuditorDatabaseService implements AuditDatabaseInterface {

	//Tables names for this database implementation
	final static String EMPLOYEE_TABLE = "EMPLOYEE";
	final static String RESIDENT_TABLE = "RESIDENT";
	final static String ADMIN_TABLE = "ADMINMEDICINE";
	final static String PRES_TABLE = "PRESCRIPTION";
	final static String EDITPRES_TABLE = "EDITPRESCRIPTION";
	
	//used to set the correct format for timestamps in the database
	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
	
	/**
	 * Default constructor 
	 */
	public AuditorDatabaseService() {
	}
	
	
	/**
	 * Saves the details of the given employee to the database 
	 * @Override
	 * @throws AuditDataAccessException
	 * @param user 
	 */
	public void saveEmployee(AbstractUser user) throws AuditDataAccessException {
		
		try {

			//check if the database is already setup..
			if (!checkTables()) {
				//this will setup the tables in a new database
				initDatabase();
			}
			
			Connection con = Database.getInstance().getNewConnection();
			Statement stmt = con.createStatement();
			
			String query = "INSERT INTO " + EMPLOYEE_TABLE + " (employeeUUID, firstname, surname) " +
							" VALUES ('" + user.getEmployeeID() + "', '" + 
							user.getFirstName() + "' ,'" + user.getSurname() + "')";
			
			int success = stmt.executeUpdate(query);
			
			if (success == 0) {
				throw new AuditDataAccessException("Critical Database Error.");
			}
			
			con.commit();
			
			//then close the database
			con.close();			
			
		}
		catch (NullPointerException ne) {
			System.out.println("exception");
			throw new AuditDataAccessException("Critical Database Error.");
		} 
		catch (SQLException e) {
			System.out.println("sql exception");
			throw new AuditDataAccessException("Critical Database Error.");
		}
	}
	
	/**
	 * Saves the details of the given resident to the database  
	 * @Override
	 * @throws AuditDataAccessException
	 * @param resident 
	 */
	public void saveResident(Resident resident) throws AuditDataAccessException {
		
		try {
			//check if the database is already setup..
			if (!checkTables()) {
				//this will setup the tables in a new database
				initDatabase();
			}
			
			Connection con = Database.getInstance().getNewConnection();
			Statement stmt = con.createStatement();
			
			//first check if the resident already exists
			String checkQuery = "SELECT * FROM " + RESIDENT_TABLE +
								" WHERE residentUUID = '" + resident.getID() + "'";
			
			ResultSet result = stmt.executeQuery(checkQuery);

			//getrow will be 0 if there are no rows in our result, therefore we can add the resident
			if (rowSize(result) == 0) {			
				String insertQuery = "INSERT INTO " + RESIDENT_TABLE + " (residentUUID, firstname, surname) " +
									" VALUES ('" + resident.getID() + "', '" + 
									resident.getFirstName() + "' ,'" + resident.getSurname() + "')";
			
				int success = stmt.executeUpdate(insertQuery);
				if (success == 0) {
					throw new AuditDataAccessException("Critical Database Error.");
				}
				
				con.commit();
				con.close();		
			}
			
		}
		catch (NullPointerException ne) {
			System.out.println("exception");
			throw new AuditDataAccessException("Critical Database Error.");
		} 
		catch (SQLException e) {
			System.out.println("sql exception");
			throw new AuditDataAccessException("Critical Database Error.");
		}
		
	}
	
	/**
	 * Saves the details of the given administration record to the database
	 * @Override
	 * @throws AuditDataAccessException
	 * @param record 
	 */
	public void saveAdministrationRecord(Record record) throws AuditDataAccessException {

		try {

			//check if the database is already setup..
			if (!checkTables()) {
				//if not setup the initialise and setup the database tables
				initDatabase();
			}
			
			Connection con = Database.getInstance().getNewConnection();
			Statement stmt = con.createStatement();
			
			//get the database ID for employee and resident
			String getEmployeeQuery = "SELECT * FROM " + EMPLOYEE_TABLE +
					" WHERE employeeUUID = '" + record.getEmployeeID() + "'";

			ResultSet employeeResult = stmt.executeQuery(getEmployeeQuery);
			
			employeeResult.next();
			
			int employeeID = employeeResult.getInt("id");
											
			String getResidentQuery = "SELECT ID FROM " + RESIDENT_TABLE +
					" WHERE residentUUID = '" + record.getPatientID() + "'";

			ResultSet residentResult = stmt.executeQuery(getResidentQuery);
			
			residentResult.next();
			
			int residentID = residentResult.getInt("id");			
			
			//create the query to insert record details into the database
			String insertQuery = "INSERT INTO " + ADMIN_TABLE + " (tstamp, employeeID, residentID, medicine, dose) " +
					" VALUES ('" + record.getTimeStamp().format(timeFormat) + "', '" + employeeID + "' ,'" + residentID + "' ,'" +
					((AdminRecord)record).getMedicine() + "' ,'" + ((AdminRecord)record).getDose() + "')";
			
			int success = stmt.executeUpdate(insertQuery);
					
			if (success == 0) {
				throw new AuditDataAccessException("Critical Database Error.");
			}
						
			con.commit();
			con.close();

		}
		catch (NullPointerException ne) {
			throw new AuditDataAccessException("Critical Database Error.");
		} 
		catch (SQLException e) {
			throw new AuditDataAccessException("Critical Database Error. " + e.getMessage());
		}
		
		
	}

	/**
	 * Saves the details of the given prescription change to the database 
	 * @Override
	 * @throws AuditDataAccessException
	 * @param record 
	 */
	public void savePrescriptionRecord(Record record) throws AuditDataAccessException {
		
		try {

			//check if the database is already setup..
			if (!checkTables()) {
				//if not setup the initialise and setup the database tables
				initDatabase();
			}
			
			Connection con = Database.getInstance().getNewConnection();
			Statement stmt = con.createStatement();
			
			
			//get the database ID for employee
			String getEmployeeQuery = "SELECT * FROM " + EMPLOYEE_TABLE +
					" WHERE employeeUUID = '" + record.getEmployeeID() + "'";

			ResultSet employeeResult = stmt.executeQuery(getEmployeeQuery);
			
			employeeResult.next();
			
			int employeeID = employeeResult.getInt("id");
			
			//get the database ID for resident
			String getResidentQuery = "SELECT ID FROM " + RESIDENT_TABLE +
					" WHERE residentUUID = '" + record.getPatientID() + "'";

			ResultSet residentResult = stmt.executeQuery(getResidentQuery);
			
			residentResult.next();
			
			int residentID = residentResult.getInt("id");
			
			//extract prescription from the record to make it easier to work with
			Prescription pres = ((PrescriptionRecord)record).getPres();
			
			//get the change type 
			String change = ((PrescriptionRecord)record).getAction();
			
			// If this is a newly added prescription we also need to save
			// it to the prescription table. 
			if (change.equals("Added")) {
				
				String insertQuery = "INSERT INTO " + PRES_TABLE + " (prescriptionUUID, day, time, medicine, dose, recurrence) " +
						" VALUES ('" + pres.getPrescriptionID() + "', '" + pres.getDoseDay() + "' ,'" + pres.getDoseTime() + "' ,'" +
						pres.getMedicineName() + "' ,'" + pres.getDoseAmount() + "' ,'" + pres.getRecurrence() + "')";
				
				int success = stmt.executeUpdate(insertQuery);
		
				if (success == 0) {
					throw new AuditDataAccessException("Critical Database Error.");
				}
				
			}
			
			//get the database ID for the prescription
			String getPresQuery = "SELECT ID FROM " + PRES_TABLE +
					" WHERE prescriptionUUID = '" + pres.getPrescriptionID() + "'";

			ResultSet presResult = stmt.executeQuery(getPresQuery);
			
			presResult.next();
			
			int presID = presResult.getInt("id");

			//We now have all of the information we need to insert the prescription change
		
			String insertQuery = "INSERT INTO " + EDITPRES_TABLE + " (tstamp, employeeID, residentID, prescriptionID, changetype) " +
					" VALUES ('" + record.getTimeStamp().format(timeFormat) + "', '" + employeeID + "' ,'" + residentID + "' ,'" +
					presID + "' ,'" + change + "')";
			
			int success = stmt.executeUpdate(insertQuery);
	
			if (success == 0) {
				throw new AuditDataAccessException("Critical Database Error.");
			}
			
			con.commit();
			con.close();

		}
		catch (NullPointerException ne) {
			throw new AuditDataAccessException("Critical Database Error.");
		} 
		catch (SQLException e) {
			throw new AuditDataAccessException("Critical Database Error. " + e.getMessage());
		}
		
	}
	
	/**
	 * Internal helper function that gets the row count from a resultset
	 * This is used to determine if the result set contains a result
	 * @param result
	 * @return
	 */
	private int rowSize(ResultSet result) {
		int count = 0;
		
		try {
			while (result.next()) {
				count++;
			}
		} catch (SQLException e) {
			return 0;
		}
		return count;
	}
	
	//
	/**
	 * check if tables exists in the database. The first time the application
	 * runs the database tables have not been created yet. 
	 * @return boolean 
	 */
	private boolean checkTables() {
		
		Connection con;
		
		try {
			con = Database.getInstance().getNewConnection();
			
			DatabaseMetaData meta = con.getMetaData();
			ResultSet tables = meta.getTables(null, null, "EMPLOYEE".toUpperCase(), null);
			
			//check the database already contains table and return true if it does
			if (tables.next()) {
				con.close();
				return true;
			}
			else {
				con.close();
				return false;
			}

		}
		catch (NullPointerException ne) {
			return false;
		} 
		catch (SQLException e) {
			return false;
		}

	}
	
	/**
	 * When the application is run for the first time (or when the database has been deleted 
	 * and re-created). This will create the necessary tables in the database to be used
	 * by the auditor. 
	 */
	public void initDatabase() {
		
		try {
			Connection con = Database.getInstance().getNewConnection();
			
			Statement sql = con.createStatement();
			
			//create the tables 
			int success = sql.executeUpdate("CREATE TABLE employee ("
													+ "id INTEGER IDENTITY PRIMARY KEY,"
													+ "employeeUUID VARCHAR(36) NOT NULL,"
													+ "firstname VARCHAR(30) NOT NULL,"
													+ "surname VARCHAR(30) NOT NULL)");
			
			success += sql.executeUpdate("CREATE TABLE resident ("
													+ "id INTEGER IDENTITY PRIMARY KEY,"
													+ "residentUUID VARCHAR(36) NOT NULL,"
													+ "firstname VARCHAR(30) NOT NULL,"
													+ "surname VARCHAR(30) NOT NULL)");
			
			success += sql.executeUpdate("CREATE TABLE prescription ("
													+ "id INTEGER IDENTITY PRIMARY KEY,"
													+ "prescriptionUUID VARCHAR(36) NOT NULL,"
													+ "day VARCHAR(10) NOT NULL,"
													+ "time VARCHAR(10) NOT NULL,"
													+ "medicine VARCHAR(30) NOT NULL,"
													+ "dose VARCHAR(10) NOT NULL,"
													+ "recurrence VARCHAR(10) NOT NULL)");
																
			success += sql.executeUpdate("CREATE TABLE adminmedicine ("
													+ "id INTEGER IDENTITY PRIMARY KEY,"
													+ "tstamp TIMESTAMP NOT NULL,"
													+ "employeeID INT NOT NULL,"
													+ "residentID INT NOT NULL,"
													+ "medicine VARCHAR(30) NOT NULL,"
													+ "dose INT NOT NULL,"
													+ "FOREIGN KEY (employeeID) REFERENCES Employee(id),"
													+ "FOREIGN KEY (residentID) REFERENCES Resident(id) )"); 
			
			success += sql.executeUpdate("CREATE TABLE editprescription ("
													+ "id INTEGER IDENTITY PRIMARY KEY,"
													+ "tstamp TIMESTAMP NOT NULL,"
													+ "employeeID INT NOT NULL,"
													+ "residentID INT NOT NULL,"
													+ "prescriptionID INT NOT NULL,"
													+ "changetype VARCHAR(30) NOT NULL,"
													+ "FOREIGN KEY (employeeID) REFERENCES Employee(id),"
													+ "FOREIGN KEY (residentID) REFERENCES Resident(id),"
													+ "FOREIGN KEY (prescriptionID) REFERENCES Prescription(id) )");
			
			if (success != 0) {
				//we have problems
				//TODO alert user 
				
			}
			
			con.close();
			
		}
		catch (NullPointerException ne) {
			//if this is thrown we didn't get a connection. need to check the database for issues
			//TODO: message to user
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	/**
	 * Loads all of the employee names and IDS from the database and returns them 
	 * as a list of strings
	 * 
	 * @override 
	 * @return ArrayList<String>
	 * @throws AuditDataAccessException
	 */
	public ArrayList<String> loadEmployees() throws AuditDataAccessException {
		
		//create a new arraylist to store each employee name (surname, firstname)
		ArrayList<String> nameList = new ArrayList<String>();
		
		try {
			//check if the database is already setup..
			if (!checkTables()) {
				//if not setup the initialise and setup the database tables
				initDatabase();
			}
			
			Connection con = Database.getInstance().getNewConnection();
			Statement stmt = con.createStatement();
			
			//get the database ID for employee
			String getEmployeeQuery = "SELECT * FROM " + EMPLOYEE_TABLE + " ORDER BY SURNAME";

			ResultSet employeeResult = stmt.executeQuery(getEmployeeQuery);
			
			while(employeeResult.next()) {
				nameList.add(new String(employeeResult.getString("surname") + ", " 
										+ employeeResult.getString("firstname") + " : "
										+ employeeResult.getString("employeeUUID") ));
			}	
			
			con.commit();
			con.close();
			
			return nameList;
		}
		catch (NullPointerException ne) {
			throw new AuditDataAccessException("Critical Database Error.");
		} 
		catch (SQLException e) {
			throw new AuditDataAccessException("Critical Database Error. " + e.getMessage());
		}
	}


	/**
	 * Loads all of the resident names and IDS from the database and returns them 
	 * as a list of strings
	 * 
	 * @override 
	 * @return ArrayList<String>
	 * @throws AuditDataAccessException
	 */
	public ArrayList<String> loadResidents() throws AuditDataAccessException {
		//create a new arraylist to store each employee name (surname, firstname)
		ArrayList<String> nameList = new ArrayList<String>();
		
		try {
			//check if the database is already setup..
			if (!checkTables()) {
				//if not setup the initialise and setup the database tables
				initDatabase();
			}
			
			Connection con = Database.getInstance().getNewConnection();
			Statement stmt = con.createStatement();
			
			//get the database ID for employee
			String getResidentQuery = "SELECT * FROM " + RESIDENT_TABLE + " ORDER BY SURNAME";

			ResultSet residentResult = stmt.executeQuery(getResidentQuery);
			
			while(residentResult.next()) {
				nameList.add(new String(residentResult.getString("surname") + ", " 
										+ residentResult.getString("firstname") + " : " 
										+ residentResult.getString("residentUUID") ));
			}	
			
			con.commit();
			con.close();
			
			return nameList;
		}
		catch (NullPointerException ne) {
			throw new AuditDataAccessException("Critical Database Error.");
		} 
		catch (SQLException e) {
			throw new AuditDataAccessException("Critical Database Error. " + e.getMessage());
		}
	}


	/**
	 * Loads all of the auditing details from the database and returns them as a 
	 * list of strings to be output into a report. This method always requires
	 * a start and end date, however null can be passed for employee and resident. 
	 * If nulls are passed, the data will be loaded for all employees and residents. 
	 * 
	 * @override
	 * @return ArrayList<String>
	 * @throws AuditDataAccessException
	 */
	public ArrayList<String> loadAuditReport(LocalDateTime start, LocalDateTime end, 
												String eUUID, String rUUID) throws AuditDataAccessException {
		
		//create a new arraylist to store each line of the audit report
		ArrayList<String> auditList = new ArrayList<String>();
		
		try {
			//check if the database is already setup..
			if (!checkTables()) {
				//if not setup the initialise and setup the database tables
				initDatabase();
			}
			
			Connection con = Database.getInstance().getNewConnection();
			Statement stmt = con.createStatement();
			
			String adminQuery;
			String presQuery;
			
			//run the query depending on the information that was given. 
			
			//if no user or resident is specified, we get all 
			if(eUUID == null && rUUID == null) {
				
				adminQuery = "SELECT a.*, EMPLOYEE.*, RESIDENT.*\r\n"
						+ "FROM ADMINMEDICINE a \r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON a.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON a.residentID = RESIDENT.id "
						+ "WHERE a.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'";
				
				presQuery = "SELECT p.*, EMPLOYEE.*, RESIDENT.*, PRESCRIPTION.*\r\n"
						+ "FROM EDITPRESCRIPTION p \r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON p.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON p.residentID = RESIDENT.id "
						+ "INNER JOIN PRESCRIPTION\r\n"
						+ "ON p.prescriptionID = PRESCRIPTION.id\r\n"
						+ "WHERE p.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'";
				
				
			}
			else if (eUUID == null && rUUID != null) {
				adminQuery = "SELECT a.*, EMPLOYEE.*, RESIDENT.*\r\n"
						+ "FROM ADMINMEDICINE a\r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON a.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON a.residentID = RESIDENT.id "
						+ "WHERE a.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'\r\n"
						+ "AND rUUID = '" + rUUID +"'";
				
				presQuery = "SELECT p.*, EMPLOYEE.*, RESIDENT.*, PRESCRIPTION.*\r\n"
						+ "FROM EDITPRESCRIPTION p \r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON p.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON p.residentID = RESIDENT.id "
						+ "INNER JOIN PRESCRIPTION\r\n"
						+ "ON p.prescriptionID = PRESCRIPTION.id\r\n"
						+ "WHERE p.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'\r\n"
						+ "AND rUUID = '" + rUUID +"'";
			}
			else if (eUUID != null && rUUID == null) {
				adminQuery = "SELECT a.*, EMPLOYEE.*, RESIDENT.*\r\n"
						+ "FROM ADMINMEDICINE a\r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON a.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON a.residentID = RESIDENT.id "
						+ "WHERE a.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'\r\n"
						+ "AND employeeUUID = '" + eUUID + "'";
				
				presQuery = "SELECT p.*, EMPLOYEE.*, RESIDENT.*, PRESCRIPTION.*\r\n"
						+ "FROM EDITPRESCRIPTION p \r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON p.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON p.residentID = RESIDENT.id "
						+ "INNER JOIN PRESCRIPTION\r\n"
						+ "ON p.prescriptionID = PRESCRIPTION.id\r\n"
						+ "WHERE p.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'\r\n"
						+ "AND employeeUUID = '" + eUUID + "'";
			}
			else {
				adminQuery = "SELECT a.*, EMPLOYEE.*, RESIDENT.*\r\n"
						+ "FROM ADMINMEDICINE a\r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON a.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON a.residentID = RESIDENT.id "
						+ "WHERE a.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'\r\n"
						+ "AND rUUID = '" + rUUID +"'\r\n"
						+ "AND employeeUUID = '" + eUUID + "'";
				
				presQuery = "SELECT p.*, EMPLOYEE.*, RESIDENT.*, PRESCRIPTION.*\r\n"
						+ "FROM EDITPRESCRIPTION p \r\n"
						+ "INNER JOIN EMPLOYEE\r\n"
						+ "ON p.employeeID = EMPLOYEE.id\r\n"
						+ "INNER JOIN (SELECT id, residentUUID AS rUUID, firstname AS FNAME, surname AS SNAME FROM RESIDENT) RESIDENT\r\n"
						+ "ON p.residentID = RESIDENT.id "
						+ "INNER JOIN PRESCRIPTION\r\n"
						+ "ON p.prescriptionID = PRESCRIPTION.id\r\n"
						+ "WHERE p.tstamp BETWEEN '" + start.format(timeFormat) + "' AND '" + end.format(timeFormat) + "'\r\n"
						+ "AND rUUID = '" + rUUID +"'\r\n"
						+ "AND employeeUUID = '" + eUUID + "'";
			}
					
			ResultSet auditResult = stmt.executeQuery(adminQuery);
			
			ResultSet presResult = stmt.executeQuery(presQuery);
			
			//add the header to the report
			auditList.add(new String("*********************************************************************************"));
			auditList.add(new String("Medicine Administration Records"));
			auditList.add(new String("*********************************************************************************\n"));
			
			while(auditResult.next()) {
				auditList.add(new String("TimeStamp: \t"
										+ auditResult.getTimestamp("tStamp").toString() + " \n" 
										+"Medicine/Dose: \t"
										+ auditResult.getString("medicine") + ", "
										+ String.valueOf(auditResult.getInt("dose")) + " \n"
										+ "Given By: \t" 
										+ auditResult.getString("firstname") + " "
										+ auditResult.getString("surname") + " " 
										+ " ID: " 
										+ auditResult.getString("employeeUUID") + " \n"
										+ "To Resident: \t" 
										+ auditResult.getString("fname") + " " 
										+ auditResult.getString("sname") + " ID: "
										+ auditResult.getString("rUUID") + "\n" ));
			}	
			
			auditList.add(new String("*********************************************************************************"));
			auditList.add(new String("Prescription Records"));
			auditList.add(new String("*********************************************************************************\n"));
			//The second part of the report contains all of the prescription assignments by doctors
			while(presResult.next()) {
				auditList.add(new String("TimeStamp: \t"
										+ presResult.getTimestamp("tstamp").toString() + "\n" 
										+ "Medicine/Dose: \t"
										+ presResult.getString("medicine") + ", "
										+ presResult.getString("dose") + ", "
										+ presResult.getString("recurrence") +" \n"
										+ "Given by: \t"
										+ presResult.getString("firstname") + " "
										+ presResult.getString("surname") + " "
										+ " ID: "
										+ presResult.getString("employeeUUID") + " \n"
										+ "To Resident: \t"
										+ presResult.getString("fname") + " "
										+ presResult.getString("sname") + " "
										+ " ID: "
										+ presResult.getString("rUUID") + "\n" ));
			}

			con.commit();
			con.close();
			
			return auditList;
		}
		catch (NullPointerException ne) {
			throw new AuditDataAccessException("Critical Database Error." + ne.getMessage());
		} 
		catch (SQLException e) {
			throw new AuditDataAccessException("Critical Database Error. " + e.getMessage());
		}
	}


}
