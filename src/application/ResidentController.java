/**
 * Class: ResidentController
 * Description: This is the controller for the resident dialog that captures the details for
 * a new resident. 
 * 
 * @author Marton Marek
 */
package application;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;


public class ResidentController {
	
    @FXML
    private DialogPane dialogPaneResident;
    
	@FXML
    private TextField txtResidentFirstName;

    @FXML
    private TextField txtResidentSurname;
    
    @FXML
    private TextField txtAge;
    
    @FXML
    private RadioButton rButtonMale;

    @FXML
    private ToggleGroup optionSex;

    @FXML
    private RadioButton rButtonFemale;

    @FXML
    private CheckBox chkBoxIsolation;
    
    @FXML
    private DatePicker datePickerDob;
	
	
    /**
     * when the details have been filled out properly, this will return a resident object
     * using the details populated in the input fields.  
     * @return Resident 
     */
	public Resident getResident() {
		
		if (rButtonMale.isSelected()) {
			return new Resident(txtResidentFirstName.getText(), txtResidentSurname.getText(), 
					Sex.MALE, calculateAge(), null, chkBoxIsolation.isSelected());
			
		}
		else {
			return new Resident(txtResidentFirstName.getText(), txtResidentSurname.getText(), 
					Sex.FEMALE, calculateAge(), null, chkBoxIsolation.isSelected());
		}
	}
	
	/**
	 * Validates the input fields to make sure there are no blank or incorrect fields. 
	 */
	public void validateInput() {
				
		if( !(txtResidentFirstName.getText().isBlank()) &&
			(txtResidentFirstName.getLength() <= 30) &&
			!(txtResidentSurname.getText().isBlank()) &&
			(txtResidentSurname.getLength() <= 30) &&
			(calculateAge() > 0) &&
			(rButtonMale.isSelected() || rButtonFemale.isSelected())) {
				dialogPaneResident.lookupButton(ButtonType.OK).setDisable(false);
		}
		else {
			dialogPaneResident.lookupButton(ButtonType.OK).setDisable(true);
		}
						 
	}
	
	/**
	 * Calculates the age of a resident based on the input Date of Birth
	 * We only need to store age for simplicity 
	 * @return int 
	 */
	private int calculateAge() {
		if (datePickerDob.getValue() != null) {
			return LocalDate.now().getYear() - datePickerDob.getValue().getYear();
		}
		else {
			return 0;
		}
		
	}

}
