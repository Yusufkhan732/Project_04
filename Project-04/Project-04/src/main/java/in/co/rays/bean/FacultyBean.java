package in.co.rays.bean;

import java.util.Date;

/**
 * FacultyBean is a data transfer object representing faculty members.
 * It includes personal details and associations with college, course, and subject.
 * 
 * @author Yusuf Khan
 */
public class FacultyBean extends BaseBean {

    /**
     * First name of the faculty.
     */
    private String firstName;

    /**
     * Last name of the faculty.
     */
    private String lastName;

    /**
     * Date of birth of the faculty.
     */
    private Date dob;

    /**
     * Gender of the faculty.
     */
    private String gender;

    /**
     * Mobile number of the faculty.
     */
    private String mobileNo;

    /**
     * Email address of the faculty.
     */
    private String email;

    /**
     * ID of the associated college.
     */
    private long collegeId;

    /**
     * Name of the associated college.
     */
    private String collegeName;

    /**
     * ID of the associated course.
     */
    private long courseId;

    /**
     * Name of the associated course.
     */
    private String courseName;

    /**
     * ID of the associated subject.
     */
    private long subjectId;

    /**
     * Name of the associated subject.
     */
    private String subjectName;

    /**
     * Returns the first name of the faculty.
     * 
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the faculty.
     * 
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the faculty.
     * 
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the faculty.
     * 
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the date of birth of the faculty.
     * 
     * @return the dob
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Sets the date of birth of the faculty.
     * 
     * @param dob the dob to set
     */
    public void setDob(Date dob) {
        this.dob = dob;
    }

    /**
     * Returns the gender of the faculty.
     * 
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the faculty.
     * 
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the mobile number of the faculty.
     * 
     * @return the mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * Sets the mobile number of the faculty.
     * 
     * @param mobileNo the mobileNo to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * Returns the email address of the faculty.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the faculty.
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the college ID associated with the faculty.
     * 
     * @return the collegeId
     */
    public long getCollegeId() {
        return collegeId;
    }

    /**
     * Sets the college ID associated with the faculty.
     * 
     * @param collegeId the collegeId to set
     */
    public void setCollegeId(long collegeId) {
        this.collegeId = collegeId;
    }

    /**
     * Returns the college name associated with the faculty.
     * 
     * @return the collegeName
     */
    public String getCollegeName() {
        return collegeName;
    }

    /**
     * Sets the college name associated with the faculty.
     * 
     * @param collegeName the collegeName to set
     */
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    /**
     * Returns the course ID associated with the faculty.
     * 
     * @return the courseId
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets the course ID associated with the faculty.
     * 
     * @param courseId the courseId to set
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Returns the course name associated with the faculty.
     * 
     * @return the courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name associated with the faculty.
     * 
     * @param courseName the courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Returns the subject ID associated with the faculty.
     * 
     * @return the subjectId
     */
    public long getSubjectId() {
        return subjectId;
    }

    /**
     * Sets the subject ID associated with the faculty.
     * 
     * @param subjectId the subjectId to set
     */
    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Returns the subject name associated with the faculty.
     * 
     * @return the subjectName
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Sets the subject name associated with the faculty.
     * 
     * @param subjectName the subjectName to set
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * Returns the key used for dropdown lists.
     * Here it returns the string representation of the ID.
     * 
     * @return the key as a String
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Returns the value used for dropdown lists.
     * Here it returns the first name of the faculty.
     * 
     * @return the value as a String
     */
    @Override
    public String getValue() {
        return firstName + "";
    }
}