package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * The {@code StudentModel} class handles database operations related to
 * students. It includes methods for adding, updating, deleting, and retrieving
 * student records. It also supports pagination, search, and checking for
 * duplicate emails.
 *
 * Author: Yusuf Khan
 */
public class StudentModel {

	private static Logger log = Logger.getLogger(StudentModel.class);

	/**
	 * Gets the next available primary key for the student table.
	 *
	 * @return the next primary key as an Integer
	 * @throws Exception if a database access error occurs
	 */
	public Integer nextPk() throws Exception {
		log.debug("StudentModel.nextPk() start");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_student");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			log.debug("Next PK retrieved: " + pk);
		} catch (Exception e) {

			log.error("Exception in getting next PK", e);
			throw new DatabaseException("Exception in getting pk: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("StudentModel.nextPk() end");
		return pk + 1;
	}

	/**
	 * Adds a new student record to the database.
	 *
	 * @param bean the StudentBean containing student details
	 * @return the generated primary key (ID) of the student
	 * @throws ApplicationException     if a database error occurs
	 * @throws DuplicateRecordException if a student with the same email already
	 *                                  exists
	 */
	public long add(StudentBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("StudentModel.add() start: " + bean);
		int pk = 0;
		Connection conn = null;

		CollegeModel collegemodel = new CollegeModel();
		CollegeBean collegebean = collegemodel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegebean.getName());

		StudentBean existbBean = findByEmail(bean.getEmail());
		if (existbBean != null) {
			log.error("Duplicate email found: " + bean.getEmail());
			throw new DuplicateRecordException("Email already exists!");
		}

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_student values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getGender());
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setString(7, bean.getEmail());
			pstmt.setLong(8, bean.getCollegeId());
			pstmt.setString(9, bean.getCollegeName());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			log.debug("Data Inserted: " + i);
			conn.commit();
		} catch (Exception e) {

			log.error("Exception in adding student", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback failed", e2);
			}
			throw new ApplicationException("Exception in adding student: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("StudentModel.add() end");
		return pk;
	}

	/**
	 * Updates an existing student record in the database.
	 *
	 * @param bean the updated StudentBean object
	 * @throws ApplicationException     if a database error occurs
	 * @throws DuplicateRecordException if email already exists for another student
	 */
	public void update(StudentBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("StudentModel.update() start: " + bean);
		Connection conn = null;

		CollegeModel collegeModel = new CollegeModel();
		CollegeBean collegeBean = collegeModel.findByPk(bean.getCollegeId());
		bean.setCollegeName(collegeBean.getName());

		StudentBean existbBean = findByEmail(bean.getEmail());
		if (existbBean != null && bean.getId() != existbBean.getId()) {
			log.error("Duplicate email found for update: " + bean.getEmail());
			throw new DuplicateRecordException("Email already exists!");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_student set first_name = ?, last_name = ?, dob = ?, gender = ?, mobile_no = ?, email = ?, college_id = ?, college_name = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setDate(3, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(4, bean.getGender());
			pstmt.setString(5, bean.getMobileNo());
			pstmt.setString(6, bean.getEmail());
			pstmt.setLong(7, bean.getCollegeId());
			pstmt.setString(8, bean.getCollegeName());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
			pstmt.setLong(13, bean.getId());

			int i = pstmt.executeUpdate();
			log.debug("Data Updated: " + i);
			conn.commit();
		} catch (Exception e) {

			log.error("Exception in updating student", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback failed", e2);
			}
			throw new ApplicationException("Exception in updating student: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("StudentModel.update() end");
	}

	/**
	 * Deletes a student record from the database based on ID.
	 *
	 * @param bean the StudentBean containing the ID to delete
	 * @throws ApplicationException if a database error occurs
	 */
	public void delete(StudentBean bean) throws ApplicationException {
		log.debug("StudentModel.delete() start: " + bean);
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_student where id = ?");
			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			log.debug("Data Deleted: " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Exception in deleting student", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback failed", e2);
			}
			throw new ApplicationException("Exception in deleting student: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("StudentModel.delete() end");
	}

	/**
	 * Finds a student by their primary key (ID).
	 *
	 * @param id the student ID
	 * @return the StudentBean if found, or null
	 * @throws ApplicationException if a database error occurs
	 */
	public StudentBean findByPk(long id) throws ApplicationException {
		log.debug("StudentModel.findByPk() start: ID=" + id);
		Connection conn = null;
		StudentBean bean = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_student where id = ?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StudentBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
		} catch (Exception e) {
			log.error("Exception in findByPk", e);
			throw new ApplicationException("Exception in getting student by pk: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("StudentModel.findByPk() end");
		return bean;
	}

	/**
	 * Finds a student by their email address.
	 *
	 * @param email the email to search
	 * @return the StudentBean if found, or null
	 * @throws ApplicationException if a database error occurs
	 */
	public StudentBean findByEmail(String email) throws ApplicationException {
		log.debug("StudentModel.findByEmail() start: email=" + email);
		Connection conn = null;
		StudentBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_student where email = ?");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StudentBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDob(rs.getDate(4));
				bean.setGender(rs.getString(5));
				bean.setMobileNo(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setCollegeId(rs.getLong(8));
				bean.setCollegeName(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
		} catch (Exception e) {
			log.error("Exception in findByEmail", e);
			throw new ApplicationException("Exception in getting student by email: " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("StudentModel.findByEmail() end");
		return bean;
	}

	/**
	 * Retrieves a list of all student records.
	 *
	 * @return a list of StudentBean objects
	 * @throws ApplicationException if a database error occurs
	 */
	public List<StudentBean> list() throws ApplicationException {
		log.debug("StudentModel.list() start");
		List<StudentBean> list = search(null, 0, 0);
		log.debug("StudentModel.list() end");
		return list;
	}

	/**
	 * Searches student records based on provided filters and supports pagination.
	 *
	 * @param bean     optional search criteria (firstName, lastName, email)
	 * @param pageNo   the page number (1-based)
	 * @param pageSize the number of records per page (0 means all records)
	 * @return a list of matching StudentBean objects
	 * @throws ApplicationException if a database error occurs
	 */
	public List<StudentBean> search(StudentBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("StudentModel.search() start: " + bean);
		Connection conn = null;
		List<StudentBean> list = new ArrayList<>();
		try {
			conn = JDBCDataSource.getConnection();
			StringBuffer sql = new StringBuffer("select * from st_student where 1=1");

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

			log.debug("SQL Query: " + sql.toString());
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				StudentBean sbean = new StudentBean();
				sbean.setId(rs.getLong(1));
				sbean.setFirstName(rs.getString(2));
				sbean.setLastName(rs.getString(3));
				sbean.setDob(rs.getDate(4));
				sbean.setGender(rs.getString(5));
				sbean.setMobileNo(rs.getString(6));
				sbean.setEmail(rs.getString(7));
				sbean.setCollegeId(rs.getLong(8));
				sbean.setCollegeName(rs.getString(9));
				sbean.setCreatedBy(rs.getString(10));
				sbean.setModifiedBy(rs.getString(11));
				sbean.setCreatedDatetime(rs.getTimestamp(12));
				sbean.setModifiedDatetime(rs.getTimestamp(13));
				list.add(sbean);
			}

		} catch (Exception e) {
			log.error("Exception in searching student", e);
			throw new ApplicationException("Exception in searching student: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("StudentModel.search() end");
		return list;
	}
}
