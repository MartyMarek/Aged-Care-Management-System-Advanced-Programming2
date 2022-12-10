/**
 * Enumerator: Day
 * Description: Custom day enumerator that also includes a corresponding index value for each day.
 * 
 * @author Marton Marek
 */

package application;

public enum Day {

	MONDAY(0),
	TUESDAY (1),
	WEDNESDAY (2),
	THURSDAY (3),
	FRIDAY (4), 
	SATURDAY (5),
	SUNDAY (6);
	
	int index;
	
	Day (int i) {
		this.index = i;
	}
}
