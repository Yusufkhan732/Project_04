package in.co.rays.bean;

import java.util.Date;

/**
 * @author admin
 *
 */
public class EmployeeBean extends BaseBean {

	private String EmployeeName;
	private String Department;
	private Date Dob;
	private String LastName;

	public String getEmployeeName() {
		return EmployeeName;
	}

	/**
	 * @param employeeName
	 */
	public void setEmployeeName(String employeeName) {
		EmployeeName = employeeName;
	}

	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		Department = department;
	}

	public Date getDob() {
		return Dob;
	}

	public void setDob(Date dob) {
		Dob = dob;
	}

	public String getLastName() {
		return LastName;
	}

	public void setLastName(String lastName) {
		LastName = lastName;
	}

	@Override
	public String getKey() {

		return Department ;
	}

	@Override
	public String getValue() {

		return Department;
	}
}
