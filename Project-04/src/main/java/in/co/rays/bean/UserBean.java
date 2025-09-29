package in.co.rays.bean;

import java.util.Date;

/**
 * UserBean represents the user entity with personal, login, and role details.
 * It extends BaseBean and implements methods for dropdown display.
 * 
 * @author Yusuf Khan
 */
public class UserBean extends BaseBean {

    /**
     * User's first name.
     */
    private String firstName;

    /**
     * User's last name.
     */
    private String lastName;

    /**
     * User's login ID.
     */
    private String login;

    /**
     * User's password.
     */
    private String password;

    /**
     * User's password confirmation.
     */
    private String confirmPassword;

    /**
     * User's date of birth.
     */
    private Date dob;

    /**
     * User's mobile number.
     */
    private String mobileNo;

    /**
     * Role ID associated with the user.
     */
    private long roleId;

    /**
     * User's gender.
     */
    private String gender;

    /**
     * Returns the user's first name.
     * 
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     * 
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the user's last name.
     * 
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     * 
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the user's login ID.
     * 
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Sets the user's login ID.
     * 
     * @param login the login ID to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Returns the user's password.
     * 
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     * 
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the user's password confirmation.
     * 
     * @return confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Sets the user's password confirmation.
     * 
     * @param confirmPassword the confirm password to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Returns the user's date of birth.
     * 
     * @return dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the user's date of birth.
     * 
     * @param dob the date of birth to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Returns the user's mobile number.
     * 
     * @return mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the user's mobile number.
     * 
     * @param mobileNo the mobile number to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Returns the role ID of the user.
     * 
     * @return roleId
     */
    public long getRoleId() {
        return roleId;
    }

    /**
     * Sets the role ID of the user.
     * 
     * @param roleId the role ID to set
     */
    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    /**
     * Returns the user's gender.
     * 
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the user's gender.
     * 
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the unique key for this bean as a String.
     * 
     * @return id as String
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Returns the display value for this bean.
     * 
     * @return firstName + " " + lastName
     */
    @Override
    public String getValue() {
        return firstName + " " + lastName;
    }
}