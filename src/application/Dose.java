/**
 * Class: Dose
 * Description: Holds the amount and date of a single dose
 * 
 * @author Marton Marek
 */

package application;

import java.io.Serializable;

import application.exceptions.*;


public class Dose implements Serializable {
	
	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	//attributes to hold a single dose and administration time
	Day day;
	int time; //24 hour time
	int dose;
	
	public Dose(Day day, int time, int dose) throws InvalidHourFormatException, InvalidDoseAmountException {
		
		//check if the time and dose are in correct format
		//dose have to be > 0
		if (dose < 1) {
			throw new InvalidDoseAmountException("Doses must be 1 or greater!");
		}
		//if time is incorrect format ie. has to be 0 - 23
		if (time < 0 || time > 23) {
			throw new InvalidHourFormatException("Time is 24 hour time: must be between 0 and 23!");
		}
		
		//if all is valid
		this.day = day;
		this.time = time;
		this.dose = dose;
	}

	/**
	 * Getters and Setters
	 * @return
	 */
	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getDose() {
		return dose;
	}

	public void setDose(int dose) {
		this.dose = dose;
	}
	
	/**
	 * Overrides toString to return a single string containing the dose details
	 */
	public String toString() {
		return day + " at " + time + ":00, " + dose + " doses";
	}
	
}