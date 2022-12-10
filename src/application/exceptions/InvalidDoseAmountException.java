/**
 * Class: InvalidDoseAmountException
 * Description: Used to throw an exception when an invalid dose amount is input by the user 
 * 
 * @author Marton Marek
 */

package application.exceptions;


public class InvalidDoseAmountException extends Exception {

	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;

	public InvalidDoseAmountException(String message) {
		super(message);
	}
}
