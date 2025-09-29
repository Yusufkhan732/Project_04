package in.co.rays.bean;

import java.util.Date;

/**
 * StudentBean represents the details of a student,
 * including personal information and associated college details.
 * 
 * @author Yusuf Khan
 */
public class StudentBean extends BaseBean {

	/**
     * First name of the student.
     */
    private String firstName;

    /**
     * Last name of the student.
     */
    private String lastName;

    /**
     * Date of birth of the student.
     */
    private Date dob;

    /**
     * Gender of the student.
     */
    private String gender;

    /**
     * Mobile number of the student.
     */
    private String mobileNo;

    /**
     * Email address of the student.
     */
    private String email;

    /**
     * ID of the college associated with the student.
     */
    private long collegeId;

    /**
     * Name of the college associated with the student.
     */
    private String collegeName;

    /**
     * Returns the first name of the student.
     * 
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the student.
     * 
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the student.
     * 
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the student.
     * 
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the date of birth of the student.
     * 
     * @return dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the student.
     * 
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Returns the gender of the student.
     * 
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the student.
     * 
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the mobile number of the student.
     * 
     * @return mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the mobile number of the student.
     * 
     * @param mobileNo the mobileNo to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Returns the email address of the student.
     * 
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the student.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the college ID associated with the student.
     * 
     * @return collegeId
     */
    public long getCollegeId() {
        return collegeId;
    }

    /**
     * Sets the college ID associated with the student.
     * 
     * @param collegeId the collegeId to set
     */
    public void setCollegeId(long collegeId) {
        this.collegeId = collegeId;
    }

    /**
     * Returns the college name associated with the student.
     * 
     * @return collegeName
     */
    public String getCollegeName() {
        return collegeName;
    }

    /**
     * Sets the college name associated with the student.
     * 
     * @param collegeName the collegeName to set
     */
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    /**
     * Returns the key (ID as string) for dropdowns or identifiers.
     * 
     * @return key as String
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Returns the display value (full name) for dropdowns or lists.
     * 
     * @return full name as String
     */
    @Override
    public String getValue() {
        return firstName + " " + lastName;
    }
}