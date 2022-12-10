/**
 * Class: AdministerController
 * Description: This is the controller class for the administer medicine screen
 * 
 * @author Marton Marek
 */

package application;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import application.auditor.Auditor;
import application.database.AdminRecord;
import application.exceptions.AuditDataAccessException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;


public class AdministerController implements Initializable {
	
	@FXML
    private Label lblResidentName;
    @FXML
    private ListView<String> listPrescriptions;
    @FXML
    private TextField txtMedicine;
    @FXML
    private TextField txtDose;
    @FXML
    private Button buttonAdminister;
    
    //This is used as the alert window to provide messages to the user
    Alert userAlert = new Alert(AlertType.NONE);
    
    String selectedPrescription;
    
    //holds the selected resident 
    Resident resident; 

    
    /**
     * Implements the initialize method from the Initializable interface.
     * Similar to a constructor this method sets up all of the components 
     * required by the administer medicine screen
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//get our instance of carehome
		CareHome home = CareHome.getInstance();
		
		//then get our selected resident
		resident = home.getSelectedResident();

		//set the resident name label to the selected resident 		
		lblResidentName.setText(resident.getFirstName() + " " + resident.getSurname());
		
		//setup the list of prescriptions
		ArrayList<Prescription> presList = resident.getPrescriptions();
    	
    	Iterator<Prescription> iter = presList.iterator();
    	
    	//display each prescription in the prescription list
    	while (iter.hasNext()) {
    		Prescription pres = iter.next();
    		
    		listPrescriptions.getItems().add(pres.toString());
    	} 
    	
    	//set the administer button to disabled (until a prescription is selected)
    	buttonAdminister.setDisable(true);
    	
    	//set the dose to a numerical input only
    	txtDose.setTextFormatter(new TextFormatter<String>(InputValidator.numericValidator));
		
	}
	
	/**
	 * This method is called when the prescription list is clicked by the user. 
	 * If the user clicks a valid prescription in the list, it will be selected
	 * and the administration button enabled.
	 * 
	 * @param e
	 */
	public void selectPrescription(MouseEvent e) {
		selectedPrescription = listPrescriptions.getSelectionModel().getSelectedItem();
		
		if (selectedPrescription != null) {
			buttonAdminister.setDisable(false);
		}
		else {
			buttonAdminister.setDisable(true);
		}
	}
	
	
	/**
	 * Called when the administer button is pressed in the administer medication screen.
	 * The method will get the selected prescription, the current date, resident and employee, 
	 * then create a record that is then saved into the audit database 
	 * 
	 * @param e
	 */
	public void administer(ActionEvent e) {
		//extract the information we need from the string in the list
		String[] tokens = selectedPrescription.split(":");
		String medicine = tokens[0];
		String[] doseTokens = tokens[2].split(",", 3);
		String[] doseString = doseTokens[1].split(" ", 3);
		int dose = Integer.parseInt(doseString[1]);
		
		String employeeID = CareHome.getInstance().getCurrentUser().getEmployeeID();
		String residentID = resident.getID();
		
		//create a new adminrecord
		AdminRecord record = new AdminRecord(LocalDateTime.now(), employeeID, residentID, medicine, dose);		
		
		//then pass this new record to the Auditor
		try {
			Auditor.getInstance().saveAdministrationRecord(record);
			
			userAlert.setAlertType(AlertType.INFORMATION);
			userAlert.setContentText("Medication Administered.");
			userAlert.show();
		} catch (AuditDataAccessException e1) {
			userAlert.setAlertType(AlertType.ERROR);
    		userAlert.setContentText("Could not save administration record, please check the database");
    		userAlert.show();
		}

    }
    
	
	/**
	 * Called when the one-off medicine button is pressed. 
	 * Similar to the administer function, this method will create
	 * a record using the current date, employee, resident and take a manual input
	 * for medicine and dose. (Used for one off administration medicine that isn't
	 * listed in the prescriptions list).
	 * 
	 * @param e
	 */
    public void oneOff(ActionEvent e) {
    	
    	//check to make sure dose is a valid number and medicine isn't empty
    	if (!(txtMedicine.getText().isBlank()) && !(txtDose.getText().isBlank())) {
    		
    		//get all the information we need to built the adminrecord
    		String employeeID = CareHome.getInstance().getCurrentUser().getEmployeeID();
    		String residentID = resident.getID();
    		String medicine = txtMedicine.getText();
    		int dose = Integer.parseInt(txtDose.getText());
    		
    		//create a new adminrecord
    		AdminRecord record = new AdminRecord(LocalDateTime.now(), employeeID, residentID, medicine, dose);	
    		
    		//then pass this new record to the Auditor
    		try {
				Auditor.getInstance().saveAdministrationRecord(record);
				
				userAlert.setAlertType(AlertType.INFORMATION);
	    		userAlert.setContentText("Medication Administered.");
	    		userAlert.show();
			} catch (AuditDataAccessException e1) {
				userAlert.setAlertType(AlertType.ERROR);
	    		userAlert.setContentText("Could not save administration record, please check the database");
	    		userAlert.show();
			}

    		//clear the fields
    		txtMedicine.clear();
    		txtDose.clear();    		
    		
    	}
    	else {
    		userAlert.setAlertType(AlertType.ERROR);
    		userAlert.setContentText("Medicine and dose cannot be left blank, please fill out details.");
    		userAlert.show();
    	}
    	
    }

}
