/**
 * Class: PrescriptionController
 * Description: This is the controller for the edit prescription screen 
 * 
 * @author Marton Marek
 */

package application;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.auditor.ActionAddPrescription;
import application.auditor.ActionDeletePrescription;
import application.auditor.Auditor;
import application.database.PrescriptionRecord;
import application.exceptions.AuditDataAccessException;
import application.exceptions.InvalidDoseAmountException;
import application.exceptions.InvalidHourFormatException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


public class PrescriptionController implements Initializable {

	@FXML
    private TextField txtMedicine;

    @FXML
    private TextField txtDose;

    @FXML
    private ChoiceBox<String> cmbDay;

    @FXML
    private ChoiceBox<String> cmbHour;

    @FXML
    private ToggleGroup recurring;

    @FXML
    private RadioButton rButtonDaily;

    @FXML
    private RadioButton rButtonWeekly;

    @FXML
    private RadioButton rButtonMonthly;

    @FXML
    private Button buttonAdd;

    @FXML
    private TableView<Prescription> tblViewPrescription;

    @FXML
    private TableColumn<Prescription, String> medicineCol;

    @FXML
    private TableColumn<Prescription, String> dayCol;

    @FXML
    private TableColumn<Prescription, String> timeCol;

    @FXML
    private TableColumn<Prescription, String> doseCol;

    @FXML
    private TableColumn<Prescription, String> recurrenceCol;
	
    //holds the resident that we are prescribing for 
	Resident resident;
	
	//holds the days of the week
	private String[] days;
	
	//holds the hours of the day
	private String[] hours = {"00","01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
								"13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
	
	//This holds the prescription list
	ObservableList<Prescription> prescriptionList = FXCollections.observableArrayList();
	
	//This is used as the alert window to provide messages to the user
    Alert userAlert = new Alert(AlertType.NONE);
	
    /**
     * Implements the initialize method from the Initializable interface. This is similar
     * to using a constructor and will be called as the class is created to setup the
     * components that will be displayed on the screen.
     * 
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		days = getDays();
		cmbDay.getItems().addAll(days);
		cmbHour.getItems().addAll(hours);
		
		//initialise the tableview
		medicineCol.setCellValueFactory(new PropertyValueFactory<Prescription, String>("medicineName"));
		dayCol.setCellValueFactory(new PropertyValueFactory<Prescription, String>("doseDay"));
		timeCol.setCellValueFactory(new PropertyValueFactory<Prescription, String>("doseTime"));
		doseCol.setCellValueFactory(new PropertyValueFactory<Prescription, String>("doseAmount"));
		recurrenceCol.setCellValueFactory(new PropertyValueFactory<Prescription, String>("recurrence"));
		
		//get the currently selected resident
		resident = CareHome.getInstance().getSelectedResident();
		
		//get residents prescriptoins
		List<Prescription> list = (List<Prescription>) resident.getPrescriptions();
		
		//display those prescriptions in the prescriptions table for the resident 
		prescriptionList.addAll(FXCollections.observableList(list));
		tblViewPrescription.setItems(prescriptionList); 
		
		//set the dose to a numerical input only
    	txtDose.setTextFormatter(new TextFormatter<String>(InputValidator.numericValidator));
	}
	
	/**
	 * returns the Day enumerator values as a string array
	 * @return String[]
	 */
	public String[] getDays() {
		ArrayList<String> types = new ArrayList<String>();
		Day days [] = Day.values();
		
		for(Day j : days) {
			types.add(j.name());		
		}
		
		//return as a primitive string array
		String[] dayList = new String[types.size()];
		dayList = types.toArray(dayList);
		
		return dayList;
	}
	
	
	/**
	 * Called when the add button is pressed. it will add a new prescription to the list
	 * of prescription of the currently selected resident.
	 * This action is also saved into the audit database for long term record keeping
	 */
	public void add() {
		//create all of the objects that go into the prescription
		Dose dose;
		
		try {
			
			//check to make sure the user hasn't entered a medicine that's too long
			if (txtMedicine.getLength() > 30) {
				userAlert.setAlertType(AlertType.WARNING);
	    		userAlert.setContentText("Medicine names must be less than 30 characters in length!");
	    		userAlert.show();
	    		return;
			}
			
			Medicine medicine = new Medicine(txtMedicine.getText());
			
			//create a dose object from this information
			int doseAmount = Integer.parseInt(txtDose.getText());
			Day day = Day.valueOf(cmbDay.getValue());
			int time = Integer.parseInt(cmbHour.getValue());
			String radioValue = recurring.getSelectedToggle().toString();
			
			String[] token = radioValue.split("'");
			String recurringValue = token[1];
		
			//now using this information create a dose object 
			dose = new Dose(day, time, doseAmount);
			
			//create new prescription
			Prescription newPrescription = new Prescription(medicine, dose, recurringValue);
			
			try {
				//set the prescription to the resident
				resident.addPrescription(newPrescription);
				
				//record in the log file
				Auditor.getInstance().saveToAudit(
						new ActionAddPrescription(CareHome.getInstance().getCurrentUser(), resident, newPrescription));
				
				//Also record in the audit database, using a prescription record
				PrescriptionRecord record = new PrescriptionRecord (
												LocalDateTime.now(), 
												CareHome.getInstance().getCurrentUser().getEmployeeID(), 
												resident.getID(), 
												newPrescription,
												"Added" );		
				
				//then pass this new record to the Auditor Database Service 
				try {
					Auditor.getInstance().savePrescriptionRecord(record);
					
					//no need for confirmation here as the user can already see the prescription 
					//being added to the prescription list

				} catch (AuditDataAccessException e1) {
					userAlert.setAlertType(AlertType.ERROR);
		    		userAlert.setContentText("Could not save prescription record, please check the database");
		    		userAlert.show();
				}

			}
			catch (NullPointerException npe) {
				userAlert.setAlertType(AlertType.ERROR);
				userAlert.setContentText("Could not add prescription to resident!");
				userAlert.show();
			}
			
			//now add the prescription to the table view
			prescriptionList.add(newPrescription);

		} 
		catch (InvalidHourFormatException | InvalidDoseAmountException e) {

			userAlert.setAlertType(AlertType.ERROR);
			userAlert.setContentText("Invalid Prescription or Dose Amount.");
			userAlert.show();
		}
		catch (NullPointerException npe) {
			userAlert.setAlertType(AlertType.ERROR);
			userAlert.setContentText("Could not add prescription to resident!");
			userAlert.show();
		}
		catch (NumberFormatException nfe) {
			userAlert.setAlertType(AlertType.ERROR);
			userAlert.setContentText("Incorrect number format, try again please");
			userAlert.show();
		}
		
		clearAll();
	}
	

	/**
	 * Called when the delete button is pressed. deletes a prescription that is selected in the list
	 * This action is also saved into the audit database for long term record keeping
	 * @param e
	 */
	public void delete(ActionEvent e) {
		Prescription selectedPres = tblViewPrescription.getSelectionModel().getSelectedItem();
		
		if (selectedPres != null) {
			tblViewPrescription.getItems().remove(selectedPres);
			tblViewPrescription.refresh();
			
			try {
				//record in audit
				Auditor.getInstance().saveToAudit(
						new ActionDeletePrescription(CareHome.getInstance().getCurrentUser(), resident, selectedPres));
				
				//set the prescription to the resident
				resident.removePrescription(selectedPres);
			}
			catch (NullPointerException npe) {
				userAlert.setAlertType(AlertType.ERROR);
				userAlert.setContentText("Could not remove prescription from resident!");
				userAlert.show();
			}
			clearAll();
		}
		else {
			userAlert.setAlertType(AlertType.ERROR);
			userAlert.setContentText("No prescription selected!");
			userAlert.show();
		}
		
	}
	
	/**
	 * called when a new prescription has been added to clear all of the components
	 * on the screen 
	 */
	public void clearAll() {
		
		txtMedicine.clear();
		txtDose.clear();
		cmbDay.setValue(null);
		cmbHour.setValue(null);
		rButtonDaily.setSelected(true);
	}
	
	
}
