package in.co.rays.bean;

/**
 * RoleBean represents different roles within the system. It includes constants
 * for predefined roles and properties like name and description for each role.
 * 
 * @author Yusuf Khan
 */
public class RoleBean extends BaseBean {

	/**
	 * Role constant for Admin.
	 */
	public static final int ADMIN = 1;

	/**
	 * Role constant for Student.
	 */
	public static final int STUDENT = 2;

	/**
	 * Role constant for Faculty.
	 */
	public static final int FACULTY = 3;

	/**
	 * Role constant for Kiosk.
	 */
	public static final int KIOSK = 4;

	/**
	 * Role constant for  College.
	 */
	public static final int COLLEGE = 5;

	/**
	 * Name of the role.
	 */
	private String name;

	/**
	 * Description of the role.
	 */
	private String description;

	/**
	 * Returns the name of the role.
	 * 
	 * @return the role name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the role.
	 * 
	 * @param name the role name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the role.
	 * 
	 * @return the role description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the role.
	 * 
	 * @param description the role description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the key (ID as string) used in dropdowns or identifiers.
	 * 
	 * @return the key as String
	 */
	@Override
	public String getKey() {
		return id + "";
	}

	/**
	 * Returns the display value (role name) used in dropdowns or lists.
	 * 
	 * @return the role name
	 */
	@Override
	public String getValue() {
		return name;
	}
}