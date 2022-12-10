/**
 * 
 */
package application.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.*;
import application.exceptions.AuditDataAccessException;

/**
 * @author marto
 *
 */
class CareHome_UnitTest {
	
	CareHome home;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		home = CareHome.getInstance();
	}

	@Test
	void testWardSize() {
		//first we test whether the wards were initialised correctly
		assertEquals(home.getWardSize(), 2); //this should always equal to two for this application
	}
	
	@Test
	void testEmployeeAdd() {
		
		//get the original list size 
		int employeeListSize = home.getEmployees().getEmployeeList().size();
		
		//test adding a nurse to the employee list
		Nurse nurse = new Nurse("Jane", "Doe", "Jane", "Doe", LocalDate.now());
		try {
			home.addEmployee(nurse);
		} catch (AuditDataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int listSizeAfterAdd = home.getEmployees().getEmployeeList().size();

		//the list should at this stage be equal to size 4
		assertEquals(employeeListSize + 1, listSizeAfterAdd);

	}
	
	@Test
	void testEmployeeDelete() {
		Nurse nurse = new Nurse("Jane", "Doe", "Jane", "Doe", LocalDate.now());
		
		Manager manager = new Manager("manager", "manager", "manager", "manager", LocalDate.now());
		try {
			home.addEmployee(nurse);
			home.setCurrentUser(manager);
		} catch (AuditDataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int listSize = home.getEmployees().getEmployeeList().size();
		
		//now delete
		home.deleteEmployee(nurse);
		
		int listSizeAfterDelete = home.getEmployees().getEmployeeList().size();
		
		//test
		assertEquals(listSize - 1, listSizeAfterDelete);

	}
	
	@Test
	void testUpdateEmployee() {
		Nurse nurse = new Nurse("Jane", "Doe", "Jane", "Doe", LocalDate.now());
		try {
			home.addEmployee(nurse);
		} catch (AuditDataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//update the name of the employee
		
		nurse.setFirstName("JaneUpdated");

		
		//get the first item (We only have one at this list) and compare the first name to the udpated
		assertEquals(nurse.getFirstName(), "JaneUpdated");
	}
	
	@Test
	void testAddResident() {
		
	}

}
