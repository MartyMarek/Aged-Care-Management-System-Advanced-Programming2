/**
 * 
 */
package application.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import application.*;
import application.exceptions.InvalidBedIndexException;

/**
 * @author marto
 *
 */
class Ward_UnitTest {
	
	Ward ward;
	//residents are assigned to beds that are located in the wards
	Resident residentMale; 
	Resident residentFemale;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		
		ward = new Ward();
		residentMale = new Resident("John", "Doe", Sex.MALE, 55, null, true);
		residentFemale = new Resident("Jane", "Doe", Sex.FEMALE, 65, null, false);
	}

	@Test
	void testAddResident() {
		int bedIndex = 1;
		ward.setResident(bedIndex, residentFemale);
		
		assertEquals(ward.residentTotal(), 1);
		
		//get the resident back and make sure it is the right one
		Resident test = ward.getResident(bedIndex);
		
		assertEquals(test, residentFemale); //test should be the same our added resident from earlier
		
		
	}
	
	@Test
	void testDeleteResident() {
		int bedIndex = 1;
		ward.setResident(bedIndex, residentFemale);
		
		assertEquals(ward.residentTotal(), 1);
		
		//now delete and check the size again
		ward.deleteResident(bedIndex);
		assertEquals(ward.residentTotal(), 0);
		
	}
	
	@Test
	void testReplaceResident() {
		
		assertThrows(InvalidBedIndexException.class, () -> ward.replaceResident(1, residentMale));
		
		ward.setResident(1, residentMale);
		
		assertNotNull(ward.getResident(1));
		
		assertEquals(ward.getResident(1).getFirstName(), "John");
		
		//now replace this resident with another
		try {
			ward.replaceResident(1, residentFemale);
			
			assertEquals(ward.getResident(1).getFirstName(), "Jane");
			
		} catch (InvalidBedIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
