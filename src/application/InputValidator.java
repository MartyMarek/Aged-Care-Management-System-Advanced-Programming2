/**
 * Class: InputValidator 
 * Description: This can be used to enforce only numerical input for a textfield.
 * 
 * @author Marton Marek
 */
package application;

import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter.Change;


public final class InputValidator {
	
	//this will enforce only numerical values (used in a textfield)
	public static UnaryOperator<Change> numericValidator = change -> {
        String value = change.getText();
        
        //match against the numerical values 0-9
        if (value.matches("[0-9]*")) {  
            return change;
        }
        return null;
    };
    
}
