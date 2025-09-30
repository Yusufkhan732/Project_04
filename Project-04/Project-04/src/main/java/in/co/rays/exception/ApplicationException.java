package in.co.rays.exception;

/**
 * Custom exception to indicate a general application-level error.
 * 
 * @author Yusuf Khan
 */
public class ApplicationException extends Exception {
	/**
	 * Constructs a new ApplicationException with the specified message.
	 *
	 * @param msg the error message describing the application error
	 */
	public ApplicationException(String msg) {
		super(msg);
	}

}
