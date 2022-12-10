/***
 * Class: Resident
 * Description: Contains all of the details for a resident in the system. also contains
 * the list of prescriptions that apply to a resident.
 * 
 * @author Marton Marek
 */

package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Resident implements Serializable {

	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	//attributes of the resident 
	String firstName;
	String surname;
	Sex gender;
	int age;

	//unique identifier for this resident 
	UUID uuid;
	String resID;
	
	//a list of all this residents prescriptions
	ArrayList<Prescription> prescriptions;
	
	//keeps track of whether this resident requires isolation
	boolean needForIso;
	
	/**
	 * Default constructor 
	 * @param firstName
	 * @param surname
	 * @param gender
	 * @param age
	 * @param needForIso
	 */
	public Resident(String firstName, String surname, Sex gender, int age, boolean needForIso) {
		super();
		this.firstName = firstName;
		this.surname = surname;
		this.gender = gender;
		this.age = age;
		this.needForIso = needForIso;
		prescriptions = new ArrayList<Prescription>();
		
		//generate unique ID for this resident 
		uuid = UUID.randomUUID();
		resID = uuid.toString();
		
	}
	
	/**
	 * Constructor including a list of prescriptions
	 * @param firstName
	 * @param surname
	 * @param gender
	 * @param age
	 * @param pres
	 * @param needForIso
	 */
	public Resident(String firstName, String surname, Sex gender, int age, 
								ArrayList<Prescription> pres, boolean needForIso) {
		super();
		this.firstName = firstName;
		this.surname = surname;
		this.gender = gender;
		this.age = age;
		
		if (pres == null) {
			//create an empty arraylist
			prescriptions = new ArrayList<Prescription>();
		}
		else {
			this.prescriptions = pres;
		}
		
		this.needForIso = needForIso;
		
		//generate unique ID for this resident 
		uuid = UUID.randomUUID();
		resID = uuid.toString();
		
	}
	
	/**
	 * adds the provided prescription to this resident 
	 * @param pres
	 * @throws NullPointerException
	 */
	public void addPrescription(Prescription pres) throws NullPointerException {
		if (pres == null) {
			throw new NullPointerException();
		}
		
		prescriptions.add(pres);
	}
	
	/**
	 * Removes the given prescription from the list of prescriptions of this resident
	 * @param pres
	 * @throws NullPointerException
	 */
	public void removePrescription(Prescription pres) throws NullPointerException {
		if (pres == null) {
			throw new NullPointerException();
		}
		prescriptions.remove(pres);
	}
	
	/**
	 * Getters and Setter methods
	 * @return
	 */
	public ArrayList<Prescription> getPrescriptions() {
		return prescriptions;
	}

	public Resident getDetails() {
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	public Sex getGender() {
		return gender;
	}

	public void setGender(Sex gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isNeedForIso() {
		return needForIso;
	}

	public void setNeedForIso(boolean needForIso) {
		this.needForIso = needForIso;
	}
	
	public String getID() {
		return resID;
	}
	
}