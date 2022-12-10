/**
 * Class: LoginController
 * Description: This is the controller class for the login screen. It validates
 * the user for both correct username and password and shift allocation (can't
 * login in if not currently in shift time).
 * 
 * @author Marton Marek
 */
package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import application.exceptions.OutsideOfShiftTimeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;


public class LoginController {

	@FXML
    private Label lblLoginError;

    @FXML
    private TextField txtLoginUsername;

    @FXML
    private PasswordField txtLoginPassword;
    
    //used to change the scene and connect to the database
    Main main;
    
    //This is used as the alert window to provide messages to the user
    Alert userAlert = new Alert(AlertType.NONE);
    
    /**
     * Default Constructor
     */
    public LoginController() {
    	main = new Main();
    }
    
    /**
     * Called when the login button is pressed. Will validate that the username
     * and passwords are correct and whether the user is currently in shift
     * 
     * @param e
     */
    public void validateLogin(ActionEvent e) {

    	//check if the username input has a value
    	if (txtLoginUsername.getText().isEmpty()) {
    		//then set the error label and return false
    		lblLoginError.setVisible(true);
    		
    	}
    	else if (txtLoginPassword.getText().isEmpty()) {
    		//then set the error label and return false
    		lblLoginError.setVisible(true);
    	}
    	else {
    	
	    	//if there has been some input into the fields
	    	//get the list of all employees
	    	ArrayList<AbstractUser> users = CareHome.getInstance().getEmployees().getEmployeeList();
	    	
	    	//first we look for the username..
	    	Iterator<AbstractUser> iter = users.iterator();
	    	
	    	while (iter.hasNext()) {
	    		AbstractUser user = iter.next();
	    		if (txtLoginUsername.getText().trim().equals(user.getUsername())) {
	    			//we have found a match for username, so check the password
	    			if (txtLoginPassword.getText().trim().equals(user.getPassword())) {
	    				//check if this user has a shift on now..
	    				
	    				try { 
	    					CareHome.getInstance().validateShift(user);
	    					//we can login, so set the current user to user
		    				loginUser(user);
		    				return;
	    				}
	    				catch (OutsideOfShiftTimeException oe) {
	    					userAlert.setAlertType(AlertType.WARNING);
	    					userAlert.setContentText("Cannot Login. You are utside of valid shift time!");
	    					userAlert.show();
	    					return;
	    				} 
	    				
	    			}
	    		}
	    	}
	    	
	    	//if we didn't find a username and matching password, set the error label
	    	lblLoginError.setVisible(true);
    	}
    }
    
    /**
     * sets the current user of the system and loads the main screen. The main screen initialiser
     * will determine what functions are enabled based on the type of the current user.
     * @param user
     */
    private void loginUser(AbstractUser user) {
    	CareHome.getInstance().setCurrentUser(user);

		//and change the screen 
		try {
			main.sceneChange("MainScreen.fxml", "Resi-Care v0.1", 712, 500);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    
    
	
}
