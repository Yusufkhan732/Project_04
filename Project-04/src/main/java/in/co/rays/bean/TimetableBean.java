package in.co.rays.bean;

import java.util.Date;


/**
 * TimetableBean represents the timetable details for exams,
 * including semester, exam date/time, course and subject information.
 * 
 * @author Yusuf Khan
 */
public class TimetableBean extends BaseBean {


    /**
     * Semester for which the timetable is applicable.
     */
    private String semester;

    /**
     * Description related to the timetable.
     */
    private String description;

    /**
     * Date of the exam.
     */
    private Date examDate;

    /**
     * Time of the exam.
     */
    private String examTime;

    /**
     * ID of the course related to the timetable.
     */
    private long courseId;

    /**
     * Name of the course related to the timetable.
     */
    private String courseName;

    /**
     * ID of the subject related to the timetable.
     */
    private long subjectId;

    /**
     * Name of the subject related to the timetable.
     */
    private String subjectName;

    /**
     * Returns the semester.
     * 
     * @return semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Sets the semester.
     * 
     * @param semester the semester to set
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * Returns the description.
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     * 
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the exam date.
     * 
     * @return examDate
     */
    public Date getExamDate() {
        return examDate;
    }

    /**
     * Sets the exam date.
     * 
     * @param examDate the examDate to set
     */
    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    /**
     * Returns the exam time.
     * 
     * @return examTime
     */
    public String getExamTime() {
        return examTime;
    }

    /**
     * Sets the exam time.
     * 
     * @param examTime the examTime to set
     */
    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    /**
     * Returns the course ID.
     * 
     * @return courseId
     */
    public long getCourseId() {
        return courseId;
    }

    /**
     * Sets the course ID.
     * 
     * @param courseId the courseId to set
     */
    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    /**
     * Returns the course name.
     * 
     * @return courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name.
     * 
     * @param courseName the courseName to set
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * Returns the subject ID.
     * 
     * @return subjectId
     */
    public long getSubjectId() {
        return subjectId;
    }

    /**
     * Sets the subject ID.
     * 
     * @param subjectId the subjectId to set
     */
    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Returns the subject name.
     * 
     * @return subjectName
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Sets the subject name.
     * 
     * @param subjectName the subjectName to set
     */
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * Returns the key (ID as String) used in dropdowns or identifiers.
     * Currently returns null and can be overridden to provide proper key.
     * 
     * @return key as String or null
     */
    @Override
    public String getKey() {
        // TODO: Provide meaningful key if needed
        return null;
    }

    /**
     * Returns the display value used in dropdowns or lists.
     * Currently returns null and can be overridden to provide proper value.
     * 
     * @return display value or null
     */
    @Override
    public String getValue() {
        // TODO: Provide meaningful value if needed
        return null;
    }
}