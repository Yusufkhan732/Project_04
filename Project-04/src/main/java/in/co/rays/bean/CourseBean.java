package in.co.rays.bean;


/**
 * CourseBean is a data transfer object for course-related information.
 * 
 * It includes the course name, duration, and description.
 * 
 * @author Yusuf Khan
 */
public class CourseBean extends BaseBean {


    /**
     * Name of the course.
     */
    private String name;

    /**
     * Duration of the course.
     */
    private String duration;

    /**
     * Description of the course.
     */
    private String description;

    /**
     * Gets the name of the course.
     * 
     * @return the course name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the course.
     * 
     * @param name the course name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the duration of the course.
     * 
     * @return the course duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the course.
     * 
     * @param duration the course duration to set
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * Gets the description of the course.
     * 
     * @return the course description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the course.
     * 
     * @param description the course description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the key value (ID as string) for dropdown or identification use.
     * 
     * @return the ID as string
     */
    @Override
    public String getKey() {
        return id + "";
    }

    /**
     * Returns the display value (course name) for dropdowns or lists.
     * 
     * @return the course name
     */
    @Override
    public String getValue() {
        return name;
    }
}