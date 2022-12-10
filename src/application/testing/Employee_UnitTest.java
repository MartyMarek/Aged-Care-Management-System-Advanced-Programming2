/**
 * 
 */
package application.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.Employees;
import application.Manager;
import application.Nurse;

/**
 * @author Marton Marek
 *
 */
class Employee_UnitTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testAddEmployee() {
		
		Employees employees = new Employees();
		
		Manager manager = new Manager("John", "Doe", "John", "Doe", LocalDate.now());
		
		//we should have no employees to start
		assertEquals(employees.getEmployeeList().size(), 0);
		
		employees.addEmployee(manager);
		
		//we should now have 1 employee
		assertEquals(employees.getEmployeeList().size(), 1);
		
	}
	
	@Test
	void testDeleteEmployee() {
		Employees employees = new Employees();
		
		Manager manager = new Manager("John", "Doe", "John", "Doe", LocalDate.now());
		
		Nurse nurse1 = new Nurse("Jane", "Doe", "Jane", "Doe", LocalDate.now());
		
		employees.addEmployee(manager);
		employees.addEmployee(nurse1);
				
		// the last manager should be true
		assertTrue(employees.isLastManager());
		
		//we should have two employees
		assertEquals(employees.getEmployeeList().size(), 2);
		
		//delete one
		employees.deleteEmployee(nurse1);
		
		//we should now have only 1 employee 
		assertEquals(employees.getEmployeeList().size(), 1);
		
	}

}
