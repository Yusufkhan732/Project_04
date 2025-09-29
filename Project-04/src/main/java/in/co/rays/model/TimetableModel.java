package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.util.JDBCDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The {@code TimetableModel} class handles CRUD operations and validations for
 * the Timetable entity in the application. It interacts with the `st_timetable`
 * table in the database using JDBC.
 *
 * Author: Yusuf Khan
 */
public class TimetableModel {

	private static Logger log = Logger.getLogger(TimetableModel.class);

	/**
	 * Returns the next primary key value for the timetable table.
	 *
	 * @return next primary key as an integer
	 * @throws Exception if any database access error occurs
	 */
	public Integer nextPk() throws Exception {
		log.debug("nextPk start");
		int pk = 0;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_timetable");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error("Exception in nextPk", e);
			throw new DatabaseException("Exception:   Exception in getting pk:" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("nextPk end");
		return pk + 1;
	}

	/**
	 * Adds a new timetable entry to the database. Sets course and subject names
	 * automatically based on their respective IDs.
	 *
	 * @param bean the TimetableBean object containing timetable details
	 * @return the generated primary key of the new timetable
	 * @throws Exception if insertion or rollback fails
	 */
	public long add(TimetableBean bean) throws Exception {
		log.debug("add start");
		int pk = 0;

		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectModel subjectModel = new SubjectModel();
		SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getName());

		Connection conn = null;
		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_timetable values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getSemester());
			pstmt.setString(3, bean.getDescription());
			pstmt.setDate(4, new java.sql.Date(bean.getExamDate().getTime()));
			pstmt.setString(5, bean.getExamTime());
			pstmt.setLong(6, bean.getCourseId());
			pstmt.setString(7, bean.getCourseName());
			pstmt.setLong(8, bean.getSubjectId());
			pstmt.setString(9, bean.getSubjectName());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			log.debug("data inserted => " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Exception in add, rolling back", e);
			try {

				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback exception in add", e2);
				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add Timetable Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("add end");
		return pk;
	}

	/**
	 * Updates an existing timetable entry. Updates course and subject names from
	 * related models.
	 *
	 * @param bean TimetableBean with updated data
	 * @throws Exception if update or rollback fails
	 */
	public void update(TimetableBean bean) throws Exception {
		log.debug("update start");

		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectModel subjectModel = new SubjectModel();
		SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getName());

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_timetable set semester = ?, description = ?, exam_date = ?, exam_time = ?, course_id = ?, course_name = ?, subject_id = ?, subject_name = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getSemester());
			pstmt.setString(2, bean.getDescription());
			pstmt.setDate(3, new java.sql.Date(bean.getExamDate().getTime()));
			pstmt.setString(4, bean.getExamTime());
			pstmt.setLong(5, bean.getCourseId());
			pstmt.setString(6, bean.getCourseName());
			pstmt.setLong(7, bean.getSubjectId());
			pstmt.setString(8, bean.getSubjectName());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
			pstmt.setLong(13, bean.getId());

			int i = pstmt.executeUpdate();
			log.debug("data updated => " + i);
			conn.commit();
		} catch (Exception e) {

			log.error("Exception in update, rolling back", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback exception in update", e2);
				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add timetable Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("update end");

	}

	/**
	 * Deletes a timetable entry from the database by ID.
	 *
	 * @param bean TimetableBean containing the ID to delete
	 * @throws ApplicationException if deletion fails
	 */
	public void delete(TimetableBean bean) throws ApplicationException {
		log.debug("delete start: ID=" + bean.getId());
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_timetable where id = ?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			log.debug("Data Deleted = " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Exception in delete, rolling back", e);
			try {

				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback exception in delete", e2);
				throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Delete Timetable Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("delete end: ID=" + bean.getId());
	}

	/**
	 * Finds a timetable record by its primary key (ID).
	 *
	 * @param id timetable ID
	 * @return TimetableBean if found, otherwise null
	 * @throws ApplicationException if a database error occurs
	 */
	public TimetableBean findByPk(long id) throws ApplicationException {
		log.debug("findByPk start: ID=" + id);
		Connection conn = null;
		TimetableBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_timetable where id = ?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
		} catch (Exception e) {
			log.error("Exception in findByPk", e);
			throw new ApplicationException("Exception : Exception getting timetable by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("findByPk end: ID=" + id);
		return bean;
	}

	/**
	 * Checks if a timetable entry already exists by course ID and exam date.
	 *
	 * @param courseId the course ID
	 * @param examDate the date of the exam
	 * @return TimetableBean if a duplicate exists, else null
	 * @throws ApplicationException if a database error occurs
	 */
	public TimetableBean checkByCourseName(Long courseId, Date examDate) throws ApplicationException {
		log.debug("checkByCourseName start: courseId=" + courseId + ", examDate=" + examDate);
		StringBuffer sql = new StringBuffer("select * from st_timetable where course_id = ? and exam_date = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setDate(2, new java.sql.Date(examDate.getTime()));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.error("Exception in checkByCourseName", e);
			throw new ApplicationException("Exception : Exception in get Timetable");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("checkByCourseName end: found=" + (bean != null));
		return bean;
	}

	/**
	 * Checks for a duplicate timetable entry using course ID, subject ID, and exam
	 * date.
	 *
	 * @param courseId  the course ID
	 * @param subjectId the subject ID
	 * @param examDate  the date of the exam
	 * @return TimetableBean if a duplicate exists, else null
	 * @throws ApplicationException if a database error occurs
	 */
	public TimetableBean checkBySubjectName(Long courseId, Long subjectId, Date examDate) throws ApplicationException {
		log.debug("checkBySubjectName start: courseId=" + courseId + ", subjectId=" + subjectId + ", examDate="
				+ examDate);
		StringBuffer sql = new StringBuffer(
				"select * from st_timetable where course_id = ? and subject_id = ? and exam_date = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setLong(2, subjectId);
			pstmt.setDate(3, new java.sql.Date(examDate.getTime()));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {

			log.error("Exception in checkBySubjectName", e);
			throw new ApplicationException("Exception : Exception in get Timetable");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("checkBySubjectName end: found=" + (bean != null));
		return bean;
	}

	/**
	 * Checks for a duplicate entry by course ID, subject ID, semester, and exam
	 * date.
	 *
	 * @param courseId  the course ID
	 * @param subjectId the subject ID
	 * @param semester  the semester name
	 * @param examDate  the exam date
	 * @return TimetableBean if found, else null
	 * @throws ApplicationException if a database error occurs
	 */
	public TimetableBean checkBySemester(Long courseId, Long subjectId, String semester, Date examDate)
			throws ApplicationException {
		log.debug("TimetableModel.checkBySemester() start: courseId=" + courseId + ", subjectId=" + subjectId
				+ ", semester=" + semester + ", examDate=" + examDate);
		StringBuffer sql = new StringBuffer(
				"select * from st_timetable where course_id = ? and subject_id = ? and semester = ? and exam_date = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setLong(2, subjectId);
			pstmt.setString(3, semester);
			pstmt.setDate(4, new java.sql.Date(examDate.getTime()));

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.error("Exception in checkBySemester", e);
			throw new ApplicationException("Exception : Exception in get Timetable");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("TimetableModel.checkBySemester() end: bean=" + bean);
		return bean;
	}

	/**
	 * Checks for a full duplicate entry with all fields.
	 *
	 * @param courseId    the course ID
	 * @param subjectId   the subject ID
	 * @param semester    the semester
	 * @param examDate    the exam date
	 * @param examTime    the exam time
	 * @param description the exam description
	 * @return TimetableBean if a full match is found, else null
	 * @throws ApplicationException if a database error occurs
	 */
	public TimetableBean checkByExamTime(Long courseId, Long subjectId, String semester, Date examDate, String examTime,
			String description) throws ApplicationException {
		log.debug("TimetableModel.checkByExamTime() start: courseId=" + courseId + ", subjectId=" + subjectId
				+ ", semester=" + semester + ", examDate=" + examDate + ", examTime=" + examTime + ", description="
				+ description);
		StringBuffer sql = new StringBuffer(
				"select * from st_timetable where course_id = ? and subject_id = ? and semester = ? and exam_date = ? and exam_time = ? and description = ?");
		TimetableBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, courseId);
			pstmt.setLong(2, subjectId);
			pstmt.setString(3, semester);
			pstmt.setDate(4, new java.sql.Date(examDate.getTime()));
			pstmt.setString(5, examTime);
			pstmt.setString(6, description);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.error("Exception in checkByExamTime", e);
			throw new ApplicationException("Exception : Exception in get Timetable");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("TimetableModel.checkByExamTime() end: bean=" + bean);
		return bean;
	}

	/**
	 * Lists all timetable entries from the database.
	 *
	 * @return list of all TimetableBean objects
	 * @throws ApplicationException if a database error occurs
	 */
	public List list() throws ApplicationException {
		return search(null, 0, 0);

	}

	/**
	 * Searches for timetable entries based on criteria and supports pagination.
	 *
	 * @param bean     TimetableBean with search parameters
	 * @param pageNo   current page number (1-based index)
	 * @param pageSize number of records per page
	 * @return list of matching TimetableBean records
	 * @throws ApplicationException if a database error occurs
	 */

	public List<TimetableBean> search(TimetableBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("TimetableModel.search() start: bean=" + bean + ", pageNo=" + pageNo + ", pageSize=" + pageSize);

		StringBuffer sql = new StringBuffer("select * from st_timetable where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
			if (bean.getCourseId() > 0) {
				sql.append(" and course_id = " + bean.getCourseId());
			}
			if (bean.getCourseName() != null && bean.getCourseName().length() > 0) {
				sql.append(" and course_name like '" + bean.getCourseName() + "%'");
			}
			if (bean.getSubjectId() > 0) {
				sql.append(" and subject_id = " + bean.getSubjectId());
			}
			if (bean.getSubjectName() != null && bean.getSubjectName().length() > 0) {
				sql.append(" and subject_name like '" + bean.getSubjectName() + "%'");
			}
			if (bean.getSemester() != null && bean.getSemester().length() > 0) {
				sql.append(" and semester like '" + bean.getSemester() + "%'");
			}
			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" and description like '" + bean.getDescription() + "%'");
			}
			if (bean.getExamDate() != null && bean.getExamDate().getDate() > 0) {
				sql.append(" and exam_date like '" + new java.sql.Date(bean.getExamDate().getTime()) + "%'");
			}
			if (bean.getExamTime() != null && bean.getExamTime().length() > 0) {
				sql.append(" and exam_time like '" + bean.getExamTime() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		ArrayList<TimetableBean> list = new ArrayList<>();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new TimetableBean();
				bean.setId(rs.getLong(1));
				bean.setSemester(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setExamDate(rs.getDate(4));
				bean.setExamTime(rs.getString(5));
				bean.setCourseId(rs.getLong(6));
				bean.setCourseName(rs.getString(7));
				bean.setSubjectId(rs.getLong(8));
				bean.setSubjectName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
				list.add(bean);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.error("Exception in TimetableModel.search()", e);
			throw new ApplicationException("Exception : Exception in search Timetable");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("TimetableModel.search() end: list.size=" + list.size());
		return list;
	}
}