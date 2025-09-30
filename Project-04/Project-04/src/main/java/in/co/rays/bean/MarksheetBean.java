package in.co.rays.bean;


/**
 * MarksheetBean represents the marksheet details for a student.
 * It includes roll number, student ID, name, and marks in subjects.
 * 
 * @author Yusuf Khan
 */
public class MarksheetBean extends BaseBean {

	  /**
     * Roll number of the student.
     */
    private String rollNo;

    /**
     * Student ID associated with the marksheet.
     */
    private long studentId;

    /**
     * Name of the student.
     */
    private String name;

    /**
     * Marks obtained in Physics.
     */
    private Integer physics;

    /**
     * Marks obtained in Chemistry.
     */
    private Integer chemistry;

    /**
     * Marks obtained in Maths.
     */
    private Integer maths;

    /**
     * Returns the roll number of the student.
     * 
     * @return the rollNo
     */
    public String getRollNo() {
        return rollNo;
    }

    /**
     * Sets the roll number of the student.
     * 
     * @param rollNo the rollNo to set
     */
    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    /**
     * Returns the student ID.
     * 
     * @return the studentId
     */
    public long getStudentId() {
        return studentId;
    }

    /**
     * Sets the student ID.
     * 
     * @param studentId the studentId to set
     */
    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    /**
     * Returns the student name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the student name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns marks in Physics.
     * 
     * @return the physics marks
     */
    public Integer getPhysics() {
        return physics;
    }

    /**
     * Sets marks in Physics.
     * 
     * @param physics the physics marks to set
     */
    public void setPhysics(Integer physics) {
        this.physics = physics;
    }

    /**
     * Returns marks in Chemistry.
     * 
     * @return the chemistry marks
     */
    public Integer getChemistry() {
        return chemistry;
    }

    /**
     * Sets marks in Chemistry.
     * 
     * @param chemistry the chemistry marks to set
     */
    public void setChemistry(Integer chemistry) {
        this.chemistry = chemistry;
    }

    /**
     * Returns marks in Maths.
     * 
     * @return the maths marks
     */
    public Integer getMaths() {
        return maths;
    }

    /**
     * Sets marks in Maths.
     * 
     * @param maths the maths marks to set
     */
    public void setMaths(Integer maths) {
        this.maths = maths;
    }

    /**
     * Returns the key (ID as string) for dropdown or identification.
     * 
     * @return the key as String
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Returns the display value combining student name and roll number.
     * 
     * @return the value as String
     */
    @Override
    public String getValue() {
        return name + " " + rollNo + "";
    }
}
