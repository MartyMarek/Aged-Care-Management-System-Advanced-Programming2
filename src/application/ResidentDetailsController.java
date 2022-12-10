/**
 * Class: ResidentDetailsController
 * Description: This is the controller for the Resident Details screen that shows a residents
 * details when the check details button is pressed. 
 * 
 * @author Marton Marek
 */
package application;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class ResidentDetailsController {
	
	
	@FXML
    private Label lblResidentFirstNameDisplay;

    @FXML
    private Label lblResidentSurnameDisplay;

    @FXML
    private Label lblResidentAgeDisplay;

    @FXML
    private Label lblResidentSexDisplay;

    @FXML
    private Label lblResidentIsolationDisplay;

    @FXML
    private ListView<String> listResidentMedicalDisplay;

    @FXML
    private ListView<String> listResidentPrescriptionDisplay;
    
      
    /**
     * Receives a resident and uses the details to populate the on
     * screen components
     * 
     * @param resident
     */
    public void setResident(Resident resident) {
    	
    	lblResidentFirstNameDisplay.setText(resident.getFirstName());
    	lblResidentSurnameDisplay.setText(resident.getSurname());
    	lblResidentAgeDisplay.setText(Integer.toString(resident.getAge()));
    	lblResidentSexDisplay.setText(resident.getGender().toString());
    	
    	if (resident.isNeedForIso()) {
    		lblResidentIsolationDisplay.setText("Yes");
    	}
    	else {
    		lblResidentIsolationDisplay.setText("No");    	
    	}
    	
    	ArrayList<Prescription> presList = resident.getPrescriptions();
    	
    	Iterator<Prescription> iter = presList.iterator();
    	
    	//display each prescription in the prescription list
    	while (iter.hasNext()) {
    		Prescription pres = iter.next();
    		
    		listResidentPrescriptionDisplay.getItems().add(pres.toString());
    	}
    	
    }


}
