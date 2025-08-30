package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Handles JDBC operations for the Course entity, such as add, update, delete,
 * find, and search with pagination support.
 * 
 * Author: Yusuf Khan
 */
public class CourseModel {

	private static Logger log = Logger.getLogger(CourseModel.class);

	/**
	 * Returns the next available primary key for course table.
	 * 
	 * @return next primary key as Integer
	 * @throws Exception if a database error occurs
	 */
	public Integer nextPk() throws Exception {

		log.debug("CourseModel.nextPk() START");
		int pk = 0;
		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_course");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			log.error("Exception in CourseModel.nextPk()", e);
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel.nextPk() END");
		return pk + 1;
	}

	/**
	 * Adds a new course record to the database.
	 * 
	 * @param bean the CourseBean containing course details
	 * @return the generated primary key
	 * @throws ApplicationException     if application-level error occurs
	 * @throws DuplicateRecordException if the course already exists
	 * @throws Exception                if a database error occurs
	 */

	public long add(CourseBean bean) throws ApplicationException, DuplicateRecordException, Exception {

		log.debug("CourseModel.add() START");
		CourseBean exsitbBean = findByName(bean.getName());

		if (exsitbBean != null) {

			log.error("Duplicate course found in add()");
			throw new DuplicateRecordException("Course Name Already Exist.....!!!");
		}

		int pk = 0;
		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			pk = nextPk();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_course values(?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDuration());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());

			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			log.error("Exception in CourseModel.add()", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback failed in CourseModel.add()", e2);
				throw new ApplicationException("Exception Add RollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add Course Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel.add() END");
		return pk;
	}

	/**
	 * Updates an existing course in the database.
	 * 
	 * @param bean the CourseBean with updated details
	 * @throws ApplicationException     if application-level error occurs
	 * @throws DuplicateRecordException if course name already exists
	 * @throws Exception                if a database error occurs
	 */
	public void update(CourseBean bean) throws DuplicateRecordException, ApplicationException, Exception {
		log.debug("CourseModel.update() START");
		Connection conn = null;
		CourseBean existBean = findByName(bean.getName());

		if (existBean != null && existBean.getId() != bean.getId()) {
			log.error("Duplicate course found in update()");
			throw new DuplicateRecordException("Course already exist.....!!!");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_course set NAME=?,DURATION=?,DESCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? where ID=?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDuration());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDatetime());
			pstmt.setTimestamp(7, bean.getModifiedDatetime());
			pstmt.setLong(8, bean.getId());

			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			
			log.error("Exception in CourseModel.update()", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback failed in CourseModel.update()", e2);
				throw new ApplicationException("Exception : Delete Rollback Exception " + e2.getMessage());
			}
			throw new ApplicationException("Exception in updating course");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel.update() END");
	}

	/**
	 * Deletes a course record from the database.
	 * 
	 * @param bean the CourseBean containing the ID to delete
	 * @throws Exception if a database error occurs
	 */
	public void delete(CourseBean bean) throws Exception {

		log.debug("CourseModel.delete() START");
		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_course where ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			
			log.error("Exception in CourseModel.delete()", e);
			
			try {

				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback failed in CourseModel.delete()", e2);

				throw new ApplicationException("Exception : Delete rollback exception " + e2.getMessage());
			}
			throw new ApplicationException("Exception in delete course");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel.delete() END");
	}

	/**
	 * Finds a course by its primary key.
	 * 
	 * @param id the course ID
	 * @return CourseBean if found, otherwise null
	 * @throws Exception if a database error occurs
	 */

	public CourseBean findByPk(long id) throws Exception {
		log.debug("CourseModel.findByPk() START");
		Connection conn = null;
		CourseBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_course where ID=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
			}
			rs.close();
		} catch (Exception e) {
			log.error("Exception in CourseModel.findByPk()", e);

			throw new ApplicationException("Exception : Exception in getting course by pk");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel.findByPk() END");
		return bean;
	}

	/**
	 * Finds a course by its name.
	 * 
	 * @param name the course name
	 * @return CourseBean if found, otherwise null
	 * @throws Exception if a database error occurs
	 */
	public CourseBean findByName(String name) throws Exception {

		log.debug("CourseModel.findByName() START");
		Connection conn = null;
		CourseBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_course where NAME=?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CourseBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
			}
			rs.close();
		} catch (Exception e) {

			log.error("Exception in CourseModel.findByName()", e);
			throw new ApplicationException("Exception : Exception in getting course by name");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel.findByName() END");
		return bean;
	}

	/**
	 * Returns a list of all courses.
	 * 
	 * @return List of CourseBean
	 * @throws Exception if a database error occurs
	 */

	public List list() throws Exception {
		log.debug("CourseModel.list() START");
		List list = search(null, 0, 0);
		log.debug("CourseModel.list() END");
		return list;
	}

	/**
	 * Searches for courses based on criteria in the CourseBean. Supports
	 * pagination.
	 * 
	 * @param bean     the search criteria
	 * @param pageNo   the page number
	 * @param pageSize number of records per page
	 * @return List of matching CourseBean
	 * @throws Exception if a database error occurs
	 */

	public List search(CourseBean bean, int pageNo, int pageSize) throws Exception {

		log.debug("CourseModel.search() START");
		StringBuffer sql = new StringBuffer("select * from st_course where 1=1");

		if (bean != null) {
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" and name like '" + bean.getName() + "%'");
			}
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		log.debug("SQL in CourseModel.search(): " + sql.toString());

		Connection conn = null;
		List list = new ArrayList();

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CourseBean();

				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDuration(rs.getString(3));
				bean.setDescription(rs.getString(4));
				bean.setCreatedBy(rs.getString(5));
				bean.setModifiedBy(rs.getString(6));
				bean.setCreatedDatetime(rs.getTimestamp(7));
				bean.setModifiedDatetime(rs.getTimestamp(8));
				list.add(bean);

			}
		} catch (Exception e) {
			log.error("Exception in CourseModel.search()", e);
			throw new ApplicationException("Exception: Exception in serach course" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("CourseModel.search() END");
		return list;
	}
}
