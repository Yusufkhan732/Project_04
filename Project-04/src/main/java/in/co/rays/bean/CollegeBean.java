package in.co.rays.bean;

/**
 * CollegeBean represents the data transfer object for a College.
 * It contains basic details such as name, address, state, city, and phone number.
 * 
 * @author Yusuf Khan
 */
public class CollegeBean extends BaseBean {

	private String name;
	private String address;
	private String state;
	private String city;
	private String phoneNo;

	/**
	 * Gets the name of the college.
	 * 
	 * @return the name of the college
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the college.
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the address of the college.
	 * 
	 * @return the address of the college
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address of the college.
	 * 
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the state where the college is located.
	 * 
	 * @return the state of the college
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state where the college is located.
	 * 
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Gets the city where the college is located.
	 * 
	 * @return the city of the college
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city where the college is located.
	 * 
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the phone number of the college.
	 * 
	 * @return the phone number of the college
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * Sets the phone number of the college.
	 * 
	 * @param phoneNo the phone number to set
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * Returns the unique key of the CollegeBean, which is the ID as a String.
	 * 
	 * @return the key as String
	 */
	@Override
	public String getKey() {
		return id + "";
	}

	/**
	 * Returns the value of the CollegeBean, which is the name of the college.
	 * 
	 * @return the name of the college
	 */
	@Override
	public String getValue() {
		return name;
	}
}