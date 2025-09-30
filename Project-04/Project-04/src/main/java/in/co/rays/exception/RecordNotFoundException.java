package in.co.rays.exception;

/**
 * Custom exception to indicate that a requested record was not found in the
 * database.
 * 
 * @author Yusuf Khan
 */
public class RecordNotFoundException extends Exception {

	/**
	 * Creates a new RecordNotFoundException with the specified message.
	 *
	 * @param msg the error message describing the missing record
	 */
	public RecordNotFoundException(String msg) {
		super(msg);

	}

}
