/**
 * Class: AuditDataAccessException
 * Description: Used to throw database access exceptions by the AuditorService  
 * 
 * @author Marton Marek
 */

package application.exceptions;


public class AuditDataAccessException extends Exception {

	/**
	 * Autogenerated default serial version ID
	 */
	private static final long serialVersionUID = 1L;
	
	public AuditDataAccessException(String message) {
		super(message);
	}

}