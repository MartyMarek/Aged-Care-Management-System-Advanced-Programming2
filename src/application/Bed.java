/**
 * Class: Bed
 * Description: This class represents a single bed in a ward and holds a resident.
 * 
 * @author Marton Marek
 */

package application;

import java.io.Serializable;
import java.util.UUID;

public class Bed implements Serializable {

	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Attributes
	 */
	Resident resident;
	UUID uuid;
	String bedID;
	
	/**
	 * Default constructor
	 */
	public Bed() {
		resident = null;
		
		uuid = UUID.randomUUID();
		bedID = uuid.toString();
	}
	
	/**
	 * Constructor including resident 
	 * @param resident
	 */
	public Bed(Resident resident) {
		this.resident = resident;
		
		uuid = UUID.randomUUID();
		bedID = uuid.toString();
	}
	
	/**
	 * returns a boolean value indicated whether this bed has a resident 
	 * assigned to it
	 * @return boolean
	 */
	public boolean isVacant() {
		if (resident == null) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Getter and Setter methods
	 */
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public String getBedID() {
		return bedID;
	}

}