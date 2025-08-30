package in.co.rays.exception;

/**
 * Custom exception for database errors.
 * 
 * @author Yusuf Khan
 */
public class DatabaseException extends Exception {
	/**
	 * Creates a new DatabaseException with the specified message.
	 *
	 * @param msg the error message
	 */
	public DatabaseException(String msg) {

		super(msg);
	}
}
