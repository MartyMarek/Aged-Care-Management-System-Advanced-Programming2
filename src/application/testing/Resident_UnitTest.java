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
import application.exceptions.InvalidBedIndexException;

/**
 * @author marto
 *
 */
class Resident_UnitTest {

	Resident residentMale;
	Resident resident;
	CareHome home;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		//resident who needs isolation
		residentMale = new Resident("John", "Doe", Sex.MALE, 55, null, false);
		
		//resident who does not need isolation
		resident = new Resident("Jane", "Doe", Sex.FEMALE, 78, null, false);
		
		home = CareHome.getInstance();
		
		//we also need a current manager to be logged in for adding/deleting residents
		Manager manager = new Manager("manager", "manager", "manager", "manager", LocalDate.now());
		
		home.setCurrentUser(manager);
	}

	@Test
	void testNullPrescription() {
		//if a null value is provided to add or delete prescription 
		//a nullpointerexception should be thrown
		
		assertThrows(NullPointerException.class, () -> resident.addPrescription(null));
		
		assertThrows(NullPointerException.class, () -> resident.removePrescription(null));
	
	}
	
	@Test
	void testAddingResident() {
		
		//try to set a resident to an incorrect bed and ward id
		assertThrows(IndexOutOfBoundsException.class, () -> home.setResident(-1, -1, resident));
		assertThrows(IndexOutOfBoundsException.class, () -> home.setResident(0, 0, resident));
		assertThrows(IndexOutOfBoundsException.class, () -> home.setResident(100, 100, resident));
		
		try {
			home.setResident(1, 2, resident);
			home.setResident(2, 2, resident);
		} catch (IndexOutOfBoundsException | AuditDataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(home.getResident("bed_1_2"));
		assertNotNull(home.getResident("bed_2_2"));

	}
	
	@Test
	void testDeleteResident() {
		home.deleteResident(1, 2);
		home.deleteResident(2, 2);
		
		assertNull(home.getResident("bed_1_2"));
		assertNull(home.getResident("bed_2_2"));	
	}
	
	@Test 
	void testReplacingResident() {
		
		try {
			home.setResident(1, 2, resident);
			assertEquals(home.getResident("bed_1_2").getFirstName(), "Jane");
			
			home.replaceResident(1, 2, residentMale);
			
			assertEquals(home.getResident("bed_1_2").getFirstName(), "John");
			
			home.deleteResident(1, 2);
			
		} catch (IndexOutOfBoundsException | AuditDataAccessException | InvalidBedIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
