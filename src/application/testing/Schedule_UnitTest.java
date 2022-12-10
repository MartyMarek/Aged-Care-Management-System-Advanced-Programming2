/**
 * 
 */
package application.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.Day;
import application.Doctor;
import application.Employees;
import application.Nurse;
import application.Schedule;
import application.ShiftList;

/**
 * @author Marton Marek
 *
 */
class Schedule_UnitTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		
	}

	@Test
	void testAssignShiftList() {
		
		Schedule schedule = new Schedule();
		Nurse nurse = new Nurse("Jane", "Doe", "Jane", "Doe", LocalDate.now());
		ShiftList emptyShiftList = new ShiftList();
		
		assertEquals(schedule.size(), 0);
		schedule.updateShiftList(nurse, emptyShiftList);
		
		assertEquals(schedule.size(), 1);
		
	}
	
	@Test
	void testCompliance() {
		
		//to pass compliance we need every morning and afternoon nurse shift filled
		//by at least one nurse AND
		// One hour per day must be assigned to a doctor 
		
		Schedule schedule = new Schedule();
		ShiftList fullMorningShiftList = new ShiftList();
		ShiftList fullAfternoonShiftList = new ShiftList();
		
		fullMorningShiftList.updateShift(Day.MONDAY, "8:00", "16:00");
		fullMorningShiftList.updateShift(Day.TUESDAY, "8:00", "16:00");
		fullMorningShiftList.updateShift(Day.WEDNESDAY, "8:00", "16:00");
		fullMorningShiftList.updateShift(Day.THURSDAY, "8:00", "16:00");
		fullMorningShiftList.updateShift(Day.FRIDAY, "8:00", "16:00");
		fullMorningShiftList.updateShift(Day.SATURDAY, "8:00", "16:00");
		fullMorningShiftList.updateShift(Day.SUNDAY, "8:00", "16:00");
		
		fullAfternoonShiftList.updateShift(Day.MONDAY, "14:00", "22:00");
		fullAfternoonShiftList.updateShift(Day.TUESDAY, "14:00", "22:00");
		fullAfternoonShiftList.updateShift(Day.WEDNESDAY, "14:00", "22:00");
		fullAfternoonShiftList.updateShift(Day.THURSDAY, "14:00", "22:00");
		fullAfternoonShiftList.updateShift(Day.FRIDAY, "14:00", "22:00");
		fullAfternoonShiftList.updateShift(Day.SATURDAY, "14:00", "22:00");
		fullAfternoonShiftList.updateShift(Day.SUNDAY, "14:00", "22:00");
		
		
		Nurse nurse1 = new Nurse("Jane", "Doe", "Jane", "Doe", LocalDate.now());
		
		Nurse nurse2 = new Nurse("Jane2", "Doe2", "Jane2", "Doe2", LocalDate.now());
		
		Doctor doctor = new Doctor("John", "Doe", "John", "Doe", LocalDate.now());
		
		Employees employees = new Employees();
		employees.addEmployee(doctor);
		employees.addEmployee(nurse1);
		employees.addEmployee(nurse2);
		
		schedule.updateShiftList(nurse1, fullMorningShiftList);
		schedule.updateShiftList(nurse2, fullAfternoonShiftList);
		schedule.updateShiftList(doctor, fullAfternoonShiftList);
		
		//we should now pass compliance
		assertTrue(schedule.checkScheduleCompliance(employees));
		
		//take an employee out
		employees.deleteEmployee(doctor);
		
		//now we should fail the compliance
		assertFalse(schedule.checkScheduleCompliance(employees));
		
		
		
	}

}
