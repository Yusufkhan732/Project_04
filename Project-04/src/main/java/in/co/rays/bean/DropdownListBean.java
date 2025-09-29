package in.co.rays.bean;

/**
 * Interface to be implemented by beans that can provide a key-value pair
 * suitable for dropdown lists.
 * 
 * Typically used to get an identifier (key) and display value.
 * 
 * @author Yusuf Khan
 */
public interface DropdownListBean {

	/**
	 * Returns the unique key of the bean, usually used as the identifier in
	 * dropdowns.
	 * 
	 * @return the unique key as String
	 */
	public String getKey();

	/**
	 * Returns the display value of the bean, usually used as the visible name in
	 * dropdowns.
	 * 
	 * @return the display value as String
	 */
	public String getValue();

}
