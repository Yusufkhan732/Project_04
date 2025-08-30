package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.bean.FacultyBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * The FacultyModel class handles all JDBC operations related to Faculty
 * records, including add, update, delete, find, and search.
 * 
 * It also sets related College, Course, and Subject names based on their IDs.
 * 
 * Author: Yusuf Khan
 */
public class FacultyModel {

	private static Logger log = Logger.getLogger(FacultyModel.class);

	/**
	 * Returns the next available primary key for the st_faculty table.
	 * 
	 * @return next primary key as Integer
	 * @throws Exception if a database error occurs
	 */
	public Integer nextPk() throws Exception {
		log.debug("FacultyModel.nextPk() START");
		int pk = 0;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_faculty");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error("Exception in FacultyModel.nextPk()", e);
			throw new DatabaseException("Exception: Exception in getting pk:" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel.nextPk() END");
		return pk + 1;
	}

	/**
	 * Adds a new Faculty record in the database after resolving related names.
	 * 
	 * @param bean the FacultyBean containing faculty details
	 * @return generated primary key
	 * @throws Exception if any error occurs (including duplicates)
	 */
	public long add(FacultyBean bean) throws Exception {
		log.debug("FacultyModel.add() START");

		CollegeModel collegeModel = new CollegeModel();
		CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());

		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectModel subjectModel = new SubjectModel();
		SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getName());

		FacultyBean existBean = findByEmail(bean.getEmail());

		if (existBean != null) {
			throw new DuplicateRecordException("email already exist..!!");
		}

		int pk = 0;
		Connection conn = null;

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"insert into st_faculty values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getGender());
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setString(7, bean.getEmail());
			pstmt.setLong(8, bean.getCollegeId());
			pstmt.setString(9, bean.getCollegeName());
			pstmt.setLong(10, bean.getCourseId());
			pstmt.setString(11, bean.getCourseName());
			pstmt.setLong(12, bean.getSubjectId());
			pstmt.setString(13, bean.getSubjectName());
			pstmt.setString(14, bean.getCreatedBy());
			pstmt.setString(15, bean.getModifiedBy());
			pstmt.setTimestamp(16, bean.getCreatedDatetime());
			pstmt.setTimestamp(17, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();

			log.debug("Data inserted => " + i);

			conn.commit();
		} catch (Exception e) {
			log.error("Exception in FacultyModel.add()", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add faculty Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel.add() END");
		return pk;
	}

	/**
	 * Updates an existing Faculty record in the database.
	 * 
	 * @param bean the FacultyBean containing updated data
	 * @return the updated record's primary key
	 * @throws Exception if a duplicate email or database error occurs
	 */
	public long update(FacultyBean bean) throws Exception {
		log.debug("FacultyModel.update() START");

		CollegeModel collegeModel = new CollegeModel();
		CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());

		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectModel subjectModel = new SubjectModel();
		SubjectBean subjectBean = subjectModel.findByPk(bean.getSubjectId());
		bean.setSubjectName(subjectBean.getName());

		FacultyBean existBean = findByEmail(bean.getEmail());

		if (existBean != null && bean.getId() != existBean.getId()) {
			throw new DuplicateRecordException("email already exist..!!");
		}

		int pk = 0;
		Connection conn = null;

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_faculty set first_name = ?, last_name = ?, dob = ?, gender = ?, mobile_no = ?, email = ?, college_id = ?, college_name = ?, course_id = ?, course_name = ?, subject_id = ?, subject_name = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ?  where id = ?");

			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getGender());
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setString(6, bean.getEmail());
			pstmt.setLong(7, bean.getCollegeId());
			pstmt.setString(8, bean.getCollegeName());
			pstmt.setLong(9, bean.getCourseId());
			pstmt.setString(10, bean.getCourseName());
			pstmt.setLong(11, bean.getSubjectId());
			pstmt.setString(12, bean.getSubjectName());
			pstmt.setString(13, bean.getCreatedBy());
			pstmt.setString(14, bean.getModifiedBy());
			pstmt.setTimestamp(15, bean.getCreatedDatetime());
			pstmt.setTimestamp(16, bean.getModifiedDatetime());
			pstmt.setLong(17, bean.getId());

			int i = pstmt.executeUpdate();

			log.debug("Data updated => " + i);

			conn.commit();
		} catch (Exception e) {
			log.error("Exception in FacultyModel.update()", e);
			try {
				conn.rollback();
			} catch (Exception e2) {

				throw new ApplicationException("Exception : Update rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Update faculty Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel.update() END");
		return pk;
	}

	/**
	 * Deletes a Faculty record by ID.
	 * 
	 * @param bean the FacultyBean containing the ID to delete
	 * @throws ApplicationException if a database error occurs
	 */
	public void delete(FacultyBean bean) throws ApplicationException {
		log.debug("FacultyModel.delete() START");

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_faculty where id = ?");
			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();

			log.debug("Data Deleted => " + i);

			conn.commit();
		} catch (Exception e) {
			log.error("Exception in FacultyModel.delete()", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				throw new ApplicationException("Exception: Delete RollBack " + e2.getMessage());
			}
			throw new ApplicationException("Exception : faculty Delete Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel.delete() END");
	}

	/**
	 * Finds a Faculty record by its primary key (ID).
	 * 
	 * @param id the faculty ID
	 * @return FacultyBean if found, otherwise null
	 * @throws ApplicationException if a database error occurs
	 */
	public FacultyBean findByPk(long id) throws ApplicationException {
		log.debug("FacultyModel.findByPk() START");

		Connection conn = null;
		FacultyBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_faculty where id = ?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCourseId(rs.getLong(10));
				bean.setCourseName(rs.getString(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
			}
		} catch (Exception e) {
			log.error("Exception in FacultyModel.findByPk()", e);

			throw new ApplicationException("Exception : Exception getting faculty by pk");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel.findByPk() END");
		return bean;
	}

	/**
	 * Finds a Faculty record by email address.
	 * 
	 * @param email the faculty email
	 * @return FacultyBean if found, otherwise null
	 * @throws ApplicationException if a database error occurs
	 */
	public FacultyBean findByEmail(String email) throws ApplicationException {
		log.debug("FacultyModel.findByEmail() START");

		Connection conn = null;
		FacultyBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_faculty where email = ?");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCourseId(rs.getLong(10));
				bean.setCourseName(rs.getString(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
			}
		} catch (Exception e) {
			log.error("Exception in FacultyModel.findByEmail()", e);

			throw new ApplicationException("Exception : Exception getting faculty by email");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel.findByEmail() END");
		return bean;
	}

	/**
	 * Returns a list of all Faculty records.
	 * 
	 * @return List of FacultyBean
	 * @throws ApplicationException if a database error occurs
	 */
	public List list() throws ApplicationException {
		log.debug("FacultyModel.list() START");
		List list = search(null, 0, 0);
		log.debug("FacultyModel.list() END");
		return list;
	}

	/**
	 * Searches for Faculty records based on the search criteria. Supports
	 * pagination.
	 * 
	 * @param bean     optional search criteria in FacultyBean
	 * @param pageNo   current page number (starting from 1)
	 * @param pageSize number of records per page
	 * @return List of matching FacultyBean records
	 * @throws ApplicationException if a database error occurs
	 */
	public List search(FacultyBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("FacultyModel.search() START");

		Connection conn = null;
		List list = new ArrayList();
		try {

			conn = JDBCDataSource.getConnection();
			StringBuffer sql = new StringBuffer("select * from st_faculty where 1=1");

			if (bean != null) {
				if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
					sql.append(" and first_name like '" + bean.getFirstName() + "%'");
				}
				if (bean.getLastName() != null && bean.getLastName().length() > 0) {
					sql.append(" and last_name like '" + bean.getLastName() + "%'");
				}
				if (bean.getEmail() != null && bean.getEmail().length() > 0) {
					sql.append(" and email like '" + bean.getEmail() + "%'");
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				sql.append(" limit " + pageNo + ", " + pageSize);
			}

			log.debug("SQL => " + sql.toString());

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new FacultyBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCourseId(rs.getLong(10));
				bean.setCourseName(rs.getString(11));
				bean.setSubjectId(rs.getLong(12));
				bean.setSubjectName(rs.getString(13));
				bean.setCreatedBy(rs.getString(14));
				bean.setModifiedBy(rs.getString(15));
				bean.setCreatedDatetime(rs.getTimestamp(16));
				bean.setModifiedDatetime(rs.getTimestamp(17));
				list.add(bean);
			}
		} catch (Exception e) {
			log.error("Exception in FacultyModel.search()", e);

			throw new ApplicationException("Exception : Exception in search faculty " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("FacultyModel.search() END");
		return list;
	}
}
