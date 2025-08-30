package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * RoleModel manages CRUD operations for the Role entity. It interacts with the
 * database through JDBC for role management.
 *
 * @author Yusuf Khan
 */
public class RoleModel {

	private static Logger log = Logger.getLogger(RoleModel.class);

	/**
	 * Returns the next primary key for the st_role table.
	 *
	 * @return next primary key
	 * @throws SQLException
	 * @throws DatabaseException
	 */

	public Integer nextPk() throws SQLException, DatabaseException {
		log.debug("RoleModel.nextPk() START");
		int pk = 0;
		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_role");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			log.error("Exception in RoleModel.nextPk()", e);
			throw new DatabaseException("Exception in getting PK");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("RoleModel.nextPk() END");
		return pk + 1;
	}

	/**
	 * Adds a new role record in the database.
	 *
	 * @param bean the role data
	 * @return the primary key of the new record
	 * @throws DuplicateRecordException if role name already exists
	 * @throws ApplicationException     in case of DB error
	 */
	public long add(RoleBean bean) throws DuplicateRecordException, ApplicationException {
		log.debug("RoleModel.add() START");
		Connection conn = null;
		int pk = 0;

		RoleBean existBean = findByName(bean.getName());
		if (existBean != null) {
			log.error("Duplicate Role Exception in add()");
			throw new DuplicateRecordException("Role already exists");
		}

		try {

			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_role values(?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDatetime());
			pstmt.setTimestamp(7, bean.getModifiedDatetime());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {

			log.error("Exception in RoleModel.add()", e);
			try {

				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e2) {
				log.error("Rollback failed in RoleModel.add()", e2);
				throw new ApplicationException("Exception : Rollback in add");
			}

			throw new ApplicationException("Exception in add Role");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("RoleModel.add() END");
		return pk;
	}

	/**
	 * Updates an existing role record.
	 *
	 * @param bean the role data
	 * @throws DuplicateRecordException if updated name already exists
	 * @throws ApplicationException     in case of DB error
	 */
	public void update(RoleBean bean) throws DuplicateRecordException, ApplicationException {

		log.debug("RoleModel.update() START");
		Connection conn = null;

		RoleBean existBean = findByName(bean.getName());
		if (existBean != null && existBean.getId() != bean.getId()) {
			log.error("Duplicate Role Exception in update()");
			throw new DuplicateRecordException("Role already exists");
		}

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement(
					"update st_role set NAME=?,DESCRIPTION=?,CREATED_BY=?,MODIFIED_BY=?,CREATED_DATETIME=?,MODIFIED_DATETIME=? where ID=?");
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getCreatedBy());
			pstmt.setString(4, bean.getModifiedBy());
			pstmt.setTimestamp(5, bean.getCreatedDatetime());
			pstmt.setTimestamp(6, bean.getModifiedDatetime());
			pstmt.setLong(7, bean.getId());

			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			log.error("Exception in RoleModel.update()", e);
			try {

				if (conn != null) {
					conn.rollback();
				}
			} catch (Exception e2) {
				log.error("Rollback failed in RoleModel.update()", e2);
				throw new ApplicationException("Rollback exception in update");
			}
			throw new ApplicationException("Exception in update Role");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("RoleModel.update() END");
	}

	/**
	 * Deletes a role record from the database.
	 *
	 * @param bean the role to be deleted
	 * @throws ApplicationException in case of DB error
	 */
	public void delete(RoleBean bean) throws ApplicationException {
		log.debug("RoleModel.delete() START");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_role where ID=?");
			pstmt.setLong(1, bean.getId());
			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
		} catch (Exception e) {

			log.error("Exception in RoleModel.delete()", e);
			try {

				if (conn != null) {

					conn.rollback();
				}
			} catch (Exception e2) {

				log.error("Rollback failed in RoleModel.delete()", e2);
				throw new ApplicationException("Rollback exception in delete");
			}
			throw new ApplicationException("Exception in delete Role");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("RoleModel.delete() END");
	}

	/**
	 * Finds a role by its primary key.
	 *
	 * @param id the role ID
	 * @return the RoleBean found, or null
	 * @throws ApplicationException in case of DB error
	 */
	public RoleBean findByPk(long id) throws ApplicationException {

		log.debug("RoleModel.findByPK() START");
		Connection conn = null;
		RoleBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_role where ID=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new RoleBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDatetime(rs.getTimestamp(6));
				bean.setModifiedDatetime(rs.getTimestamp(7));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {

			log.error("Exception in RoleModel.findByPK()", e);
			throw new ApplicationException("Exception in findByPK");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("RoleModel.findByPK() END");
		return bean;
	}

	/**
	 * Finds a role by its name.
	 *
	 * @param name the role name
	 * @return the RoleBean found, or null
	 * @throws ApplicationException in case of DB error
	 */
	public RoleBean findByName(String name) throws ApplicationException {
		log.debug("RoleModel.findByName() START");
		Connection conn = null;
		RoleBean bean = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_role where NAME=?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new RoleBean();
				bean.setId(rs.getLong(1));
				bean.setName(rs.getString(2));
				bean.setDescription(rs.getString(3));
				bean.setCreatedBy(rs.getString(4));
				bean.setModifiedBy(rs.getString(5));
				bean.setCreatedDatetime(rs.getTimestamp(6));
				bean.setModifiedDatetime(rs.getTimestamp(7));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {

			log.error("Exception in RoleModel.findByName()", e);
			throw new ApplicationException("Exception in findByName");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("RoleModel.findByName() END");
		return bean;
	}

	/**
	 * Returns a list of all roles.
	 *
	 * @return list of RoleBean
	 * @throws Exception in case of DB error
	 */
	public List list() throws Exception {
		return search(null, 0, 0);
	}

	/**
	 * Searches for roles based on given criteria with pagination.
	 *
	 * @param bean     the search criteria
	 * @param pageNo   page number
	 * @param pageSize number of records per page
	 * @return list of RoleBean
	 * @throws ApplicationException in case of DB error
	 */
	public List search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {

		log.debug("RoleModel.search() START");
		StringBuffer sql = new StringBuffer("select * from st_role where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" AND id = " + bean.getId());
			}
			if (bean.getName() != null && bean.getName().length() > 0) {
				sql.append(" AND NAME like '" + bean.getName() + "%'");
			}
			if (bean.getDescription() != null && bean.getDescription().length() > 0) {
				sql.append(" AND DESCRIPTION like '" + bean.getDescription() + "%'");
			}
		}

		ArrayList list = new ArrayList();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				RoleBean rb = new RoleBean();
				rb.setId(rs.getLong(1));
				rb.setName(rs.getString(2));
				rb.setDescription(rs.getString(3));
				rb.setCreatedBy(rs.getString(4));
				rb.setModifiedBy(rs.getString(5));
				rb.setCreatedDatetime(rs.getTimestamp(6));
				rb.setModifiedDatetime(rs.getTimestamp(7));
				list.add(rb);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {

			log.error("Exception in RoleModel.search()", e);
			throw new ApplicationException("Exception in search");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("RoleModel.search() END");
		return list;
	}
}