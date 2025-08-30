package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * Handles JDBC operations for the College entity, including add, update,
 * delete, search, and find by primary key or name.
 * 
 * author: Yusuf Khan
 */
public class CollegeModel {

	private static Logger log = Logger.getLogger(CollegeModel.class);

	/**
	 * Returns the next primary key for college table.
	 * 
	 * @return Integer value of next primary key
	 * @throws DatabaseException if there's an error accessing the database
	 */
	public Integer nextPk() throws DatabaseException {
		log.debug("CollegeModel.nextPk() START");
		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_college");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			log.error("Exception in CollegeModel.nextPk()", e);
			throw new DatabaseException("Exception : getting pk");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("CollegeModel.nextPk() END");
		return pk + 1;
	}

	/**
	 * Adds a new college record to the database.
	 * 
	 * @param bean CollegeBean object containing college details
	 * @return long value of newly inserted record's ID
	 * @throws ApplicationException     if an application-level exception occurs
	 * @throws DuplicateRecordException if college name already exists
	 */
	public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {

		log.debug("CollegeModel.add() START");
		int pk = 0;
		Connection conn = null;

		CollegeBean existbBean = findByName(bean.getName());
		if (existbBean != null) {

			log.error("Duplicate college found in add()");
			throw new DuplicateRecordException("college name already exist....!!!");
		}

		try {

			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_college values(?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getAddress());
			pstmt.setString(4, bean.getState());
			pstmt.setString(5, bean.getCity());
			pstmt.setString(6, bean.getPhoneNo());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());

			pstmt.executeUpdate();
			conn.commit();

		} catch (Exception e) {

			log.error("Exception in CollegeModel.add()", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback failed in CollegeModel.add()", e2);
				throw new ApplicationException("Exception : Add RollBack Exception " + e2.getMessage());
			}

			throw new ApplicationException("Exception : Add college Exception " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("CollegeModel.add() END");
		return pk;
	}

	/**
	 * Updates an existing college record.
	 * 
	 * @param bean CollegeBean object with updated data
	 * @throws ApplicationException     if an application-level exception occurs
	 * @throws DuplicateRecordException if college name already exists for another
	 *                                  record
	 */
	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {

		log.debug("CollegeModel.update() START");
		Connection conn = null;

		CollegeBean beanExist = findByName(bean.getName());

		if (beanExist != null && beanExist.getId() != bean.getId()) {

			log.error("Duplicate college found in update()");
			throw new DuplicateRecordException("college name already exist....!!!");
		}

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_college set name=?,address=?,state=?,city=?,phone_no=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getAddress());
			pstmt.setString(3, bean.getState());
			pstmt.setString(4, bean.getCity());
			pstmt.setString(5, bean.getPhoneNo());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());
			pstmt.setLong(10, bean.getId());

			pstmt.executeUpdate();
			conn.commit();

		} catch (Exception e) {
			log.error("Exception in CollegeModel.update()", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback failed in CollegeModel.update()", e2);
				throw new ApplicationException("Exception : Update Rollback Exception " + e2.getMessage());
			}
			throw new ApplicationException("Exception : Update college Exception " + e.getMessage());

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("CollegeModel.update() END");
	}

	/**
	 * Deletes a college record by ID.
	 * 
	 * @param bean CollegeBean object containing the ID of the record to be deleted
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public void delete(CollegeBean bean) throws ApplicationException {
		log.debug("CollegeModel.delete() START");
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_college where id=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();

			conn.commit();

		} catch (Exception e) {
			log.error("Exception in CollegeModel.delete()", e);
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback failed in CollegeModel.delete()", e2);
				throw new ApplicationException("Exception : Delete Rollback Exception " + e2.getMessage());
			}
			throw new ApplicationException("Exception : Delete college Exception " + e.getMessage());

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("CollegeModel.delete() END");
	}

	/**
	 * Finds a college record by primary key (ID).
	 * 
	 * @param id long value representing the college ID
	 * @return CollegeBean object if found, null otherwise
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public CollegeBean findByPk(long id) throws ApplicationException {

		log.debug("CollegeModel.findByPk() START");
		Connection conn = null;
		CollegeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where id=?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}
			rs.close();

		} catch (Exception e) {
			log.error("Exception in CollegeModel.findByPk()", e);

			throw new ApplicationException("Exception : Exception in getting College by pk " + e.getMessage());

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("CollegeModel.findByPk() END");
		return bean;
	}

	/**
	 * Finds a college record by name.
	 * 
	 * @param name Name of the college
	 * @return CollegeBean object if found, null otherwise
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public CollegeBean findByName(String name) throws ApplicationException {
		log.debug("CollegeModel.findByName() START");
		Connection conn = null;
		CollegeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where name=?");
			pstmt.setString(1, name);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
			}
			rs.close();

		} catch (Exception e) {
			log.error("Exception in CollegeModel.findByName()", e);

			throw new ApplicationException("Exception : Exception in getting College by Name " + e.getMessage());
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("CollegeModel.findByName() END");
		return bean;
	}

	/**
	 * Lists all college records.
	 * 
	 * @return List of CollegeBean objects
	 * @throws ApplicationException if an application-level exception occurs
	 */

	public List list() throws ApplicationException {
		return search(null, 0, 0);

	}

	/**
	 * Searches college records based on search criteria with pagination support.
	 * 
	 * @param bean     CollegeBean object containing search criteria (name, city)
	 * @param pageNo   current page number
	 * @param pageSize number of records per page
	 * @return List of matching CollegeBean objects
	 * @throws ApplicationException if an application-level exception occurs
	 */
	public List search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {

		log.debug("CollegeModel.list() START");
		Connection conn = null;
		ArrayList list = new ArrayList();

		try {
			conn = JDBCDataSource.getConnection();
			StringBuffer sql = new StringBuffer("select * from st_college");

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				sql.append(" limit " + pageNo + "," + pageSize);
			}

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new CollegeBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setAddress(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setCity(rs.getString(5));
				bean.setPhoneNo(rs.getString(6));
				bean.setCreatedBy(rs.getString(7));
				bean.setModifiedBy(rs.getString(8));
				bean.setCreatedDatetime(rs.getTimestamp(9));
				bean.setModifiedDatetime(rs.getTimestamp(10));
				list.add(bean);
			}
			rs.close();

		} catch (Exception e) {
			log.error("Exception in CollegeModel.list()", e);

			throw new ApplicationException("Exception : Exception in college list " + e.getMessage());
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("CollegeModel.list() END");
		return list;
	}
}