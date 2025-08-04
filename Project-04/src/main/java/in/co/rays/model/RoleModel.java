package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;
/**
* RoleModel manages CRUD operations for the Role entity.
* It interacts with the database through JDBC for role management.
*
* @author Yusuf Khan
*/
public class RoleModel {
	  /**
     * Returns the next primary key for the st_role table.
     *
     * @return next primary key
     * @throws SQLException
     * @throws DatabaseException
     */
	
	public Integer nextPk() throws SQLException, DatabaseException {

		int pk = 0;

		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select Max(id)from st_role");

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				pk = rs.getInt(1);

			}
		} catch (Exception e) {

			throw new DatabaseException("Exception: Exception Gettinh bp nextpk" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;
	}

    /**
     * Adds a new role record in the database.
     *
     * @param bean the role data
     * @return the primary key of the new record
     * @throws DuplicateRecordException if role name already exists
     * @throws ApplicationException in case of DB error
     */
	public long add(RoleBean bean) throws DuplicateRecordException, ApplicationException {

		Connection conn = null;
		int pk = 0;

		RoleBean existBean = findByName(bean.getName());

		if (existBean != null) {

			throw new DuplicateRecordException("role name already exist...");

		}

		try {

			pk = nextPk();
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_role values(?,?,?,?,?,?,?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDatetime());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());

			int i = pstmt.executeUpdate();
			System.out.println("Data Inserted:=" + i);
			conn.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();

			} catch (Exception e2) {
				throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
			}

			throw new ApplicationException("Exception:Add Role Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}
	  /**
     * Updates an existing role record.
     *
     * @param bean the role data
     * @throws DuplicateRecordException if updated name already exists
     * @throws ApplicationException in case of DB error
     */
	public void update(RoleBean bean) throws DuplicateRecordException, ApplicationException {

		RoleBean existbBean = findByName(bean.getName());

		if (existbBean != null && bean.getId() != existbBean.getId()) {

//          existbean != admin    &&				 1            !=   1			

			throw new DuplicateRecordException("role name already exist!!....");

		}

		Connection conn = null;

		try {

			JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_role set name = ?,description = ?,created_by = ?,modified_by = ?,created_datetime = ?,modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDescription());
			pstmt.setString(3, bean.getCreatedBy());
			pstmt.setString(4, bean.getModifiedBy());
			pstmt.setTimestamp(5, bean.getCreatedDatetime());
			pstmt.setTimestamp(6, bean.getCreatedDatetime());
			pstmt.setLong(7, bean.getId());

			int i = pstmt.executeUpdate();

			conn.commit();

			System.out.println("Data updated=:" + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
			}

			throw new ApplicationException("Exception:Update Role Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}
	 /**
     * Deletes a role record from the database.
     *
     * @param bean the role to be deleted
     * @throws ApplicationException in case of DB error
     */
	public void delete(RoleBean bean) throws ApplicationException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_role where id = ?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();

			conn.commit();

			System.out.println("data deleted => " + i);

		} catch (Exception e) {

			try {

				conn.rollback();

			} catch (Exception ex) {

				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}

			throw new ApplicationException("Exception : Exception in delete Role " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}
	/**
     * Finds a role by its primary key.
     *
     * @param id the role ID
     * @return the RoleBean found, or null
     * @throws ApplicationException in case of DB error
     */
	public RoleBean findByPk(long id) throws ApplicationException {

		Connection conn = null;
		RoleBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_role where id = ?");

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
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception getting role by pk" + e.getMessage());

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
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
		Connection conn = null;
		RoleBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_role where name = ?");

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
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception getting role by name" + e.getMessage());

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
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
     * @param bean the search criteria
     * @param pageNo page number
     * @param pageSize number of records per page
     * @return list of RoleBean
     * @throws ApplicationException in case of DB error
     */
	public List search(RoleBean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_role where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {
				sql.append(" and id = " + bean.getId());
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}

		System.out.println("sql ==>> " + sql.toString());

		Connection conn = null;
		List list = new ArrayList();

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

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
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();

			throw new ApplicationException("Exception: Exception in serach role" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
}
