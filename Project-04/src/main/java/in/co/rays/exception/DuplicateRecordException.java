package in.co.rays.exception;

/**
 * Custom exception to indicate that a record already exists in the database.
 * 
 * @author Yusuf Khan
 */
public class DuplicateRecordException extends Exception {

	/**
	 * Constructs a new DuplicateRecordException with the specified message.
	 *
	 * @param msg the error message describing the duplicate record
	 */
	public DuplicateRecordException(String msg) {

		super(msg);
	}
}
