package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * The {@code SubjectModel} class provides methods to perform CRUD operations
 * and search functionality for the Subject entity using JDBC. It interacts with
 * the `st_subject` table in the database.
 *
 * Author: Yusuf Khan
 */
public class SubjectModel {

	private static Logger log = Logger.getLogger(SubjectModel.class);

	/**
	 * Gets the next available primary key value for the subject table.
	 *
	 * @return the next primary key as an Integer
	 * @throws Exception if a database error occurs
	 */
	public Integer nextPk() throws Exception {
		log.debug("SubjectModel.nextPk() start");
		int pk = 0;
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_subject");
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

		log.debug("SubjectModel.nextPk() end");
		return pk + 1;
	}

	/**
	 * Adds a new subject to the database.
	 *
	 * @param bean a SubjectBean object containing the subject data
	 * @return the generated primary key (ID) of the newly inserted subject
	 * @throws Exception if a database or duplicate error occurs
	 */
	public long add(SubjectBean bean) throws Exception {
		log.debug("SubjectModel.add() start: " + bean);
		int pk = 0;
		Connection conn = null;

		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectBean existBean = findByName(bean.getName());
		if (existBean != null) {
			log.error("Duplicate subject name: " + bean.getName());
			throw new DuplicateRecordException("Subject name already exists!");
		}

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("insert into st_subject values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setLong(3, bean.getCourseId());
			pstmt.setString(4, bean.getCourseName());
			pstmt.setString(5, bean.getDescription());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			log.debug("Data Inserted: " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Exception in adding subject", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback failed", e2);
			}
			throw new ApplicationException("Exception in adding subject: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("SubjectModel.add() end");
		return pk;
	}

	/**
	 * Updates an existing subject in the database.
	 *
	 * @param bean the SubjectBean containing updated subject data
	 * @throws Exception if a database or duplicate error occurs
	 */
	public void update(SubjectBean bean) throws Exception {
		log.debug("SubjectModel.update() start: " + bean);
		Connection conn = null;

		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectBean existBean = findByName(bean.getName());
		if (existBean != null && bean.getId() != existBean.getId()) {
			log.error("Duplicate subject name for update: " + bean.getName());
			throw new DuplicateRecordException("Subject name already exists!");
		}

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_subject set name = ?, course_id = ?, course_name = ?, description = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getName());
			pstmt.setLong(2, bean.getCourseId());
			pstmt.setString(3, bean.getCourseName());
			pstmt.setString(4, bean.getDescription());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());

			int i = pstmt.executeUpdate();
			log.debug("Data Updated: " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Exception in updating subject", e);
			try {
				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback failed", e2);
			}
			throw new ApplicationException("Exception in updating subject: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("SubjectModel.update() end");
	}

	/**
	 * Deletes a subject from the database using the subject ID.
	 *
	 * @param bean a SubjectBean object containing the subject ID
	 * @throws ApplicationException if a database error occurs
	 */
	public void delete(SubjectBean bean) throws ApplicationException {
		log.debug("SubjectModel.delete() start: " + bean);
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_subject where id = ?");
			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			log.debug("Data Deleted: " + i);
			conn.commit();
		} catch (Exception e) {
			log.error("Exception in deleting subject", e);
			try {
				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback failed", e2);
			}
			throw new ApplicationException("Exception in deleting subject: " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

		log.debug("SubjectModel.delete() end");
	}

	/**
	 * Finds a subject by its primary key (ID).
	 *
	 * @param id the subject ID
	 * @return the SubjectBean object if found, or null
	 * @throws ApplicationException if a database error occurs
	 */
	public SubjectBean findByPk(long id) throws ApplicationException {
		log.debug("SubjectModel.findByPk() start: ID=" + id);
		Connection conn = null;
		SubjectBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_subject where id = ?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
		} catch (Exception e) {
			log.error("Exception in findByPk", e);
			throw new ApplicationException("Exception in getting subject by pk: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("SubjectModel.findByPk() end");
		return bean;
	}

	/**
	 * Finds a subject by its name.
	 *
	 * @param name the name of the subject
	 * @return the SubjectBean object if found, or null
	 * @throws ApplicationException if a database error occurs
	 */
	public SubjectBean findByName(String name) throws ApplicationException {
		log.debug("SubjectModel.findByName() start: name=" + name);
		Connection conn = null;
		SubjectBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_subject where name = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new SubjectBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
		} catch (Exception e) {
			log.error("Exception in findByName", e);
			throw new ApplicationException("Exception in getting subject by name: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("SubjectModel.findByName() end");
		return bean;
	}

	/**
	 * Returns a list of all subjects.
	 *
	 * @return a List of SubjectBean objects
	 * @throws ApplicationException if a database error occurs
	 */
	public List<SubjectBean> list() throws ApplicationException {
		log.debug("SubjectModel.list() start");
		List<SubjectBean> list = search(null, 0, 0);
		log.debug("SubjectModel.list() end");
		return list;
	}

	/**
	 * Searches for subjects based on search criteria and supports pagination.
	 *
	 * @param bean     the search criteria (can be null for all records)
	 * @param pageNo   the page number (1-based)
	 * @param pageSize the number of records per page (0 for all records)
	 * @return a List of matching SubjectBean objects
	 * @throws ApplicationException if a database error occurs
	 */
	public List<SubjectBean> search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("SubjectModel.search() start: " + bean);

		StringBuffer sql = new StringBuffer("select * from st_subject where 1=1");
		if (bean != null) {
			if (bean.getCourseId() > 0) {
				sql.append(" and course_id = " + bean.getCourseId());
			}
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		log.debug("SQL Query: " + sql.toString());

		Connection conn = null;
		List<SubjectBean> list = new ArrayList<>();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				SubjectBean sbean = new SubjectBean();
				sbean.setId(rs.getLong(1));
				sbean.setName(rs.getString(2));
				sbean.setCourseId(rs.getLong(3));
				sbean.setCourseName(rs.getString(4));
				sbean.setDescription(rs.getString(5));
				sbean.setCreatedBy(rs.getString(6));
				sbean.setModifiedBy(rs.getString(7));
				sbean.setCreatedDatetime(rs.getTimestamp(8));
				sbean.setModifiedDatetime(rs.getTimestamp(9));
				list.add(sbean);
			}
		} catch (Exception e) {
			log.error("Exception in searching subjects", e);
			throw new ApplicationException("Exception in searching subjects: " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("SubjectModel.search() end");
		return list;
	}
}