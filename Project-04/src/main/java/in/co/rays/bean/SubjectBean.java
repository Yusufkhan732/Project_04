package in.co.rays.bean;

/**
 * SubjectBean represents the details of a subject,
 * including its name, associated course details, and description.
 * 
 * @author Yusuf Khan
 */
public class SubjectBean extends BaseBean {

	   /**
     * Name of the subject.
     */
    private String name;

    /**
     * ID of the course this subject belongs to.
     */
    private long courseId;

    /**
     * Name of the course this subject belongs to.
     */
    private String courseName;

    /**
     * Description of the subject.
     */
    private String description;

    /**
     * Returns the name of the subject.
     * 
     * @return subject name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the subject.
     * 
     * @param name subject name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the course ID associated with the subject.
     * 
     * @return course ID
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets the course ID associated with the subject.
     * 
     * @param courseId course ID to set
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Returns the course name associated with the subject.
     * 
     * @return course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name associated with the subject.
     * 
     * @param courseName course name to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Returns the description of the subject.
     * 
     * @return subject description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the subject.
     * 
     * @param description subject description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the key (ID as string) used for dropdowns or identifiers.
     * 
     * @return key as String
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Returns the display value (subject name) used in dropdowns or lists.
     * 
     * @return subject name
     */
    @Override
    public String getValue() {
        return name;
    }
}