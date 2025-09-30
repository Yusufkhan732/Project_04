package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.EmployeeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.util.JDBCDataSource;

public class EmployeeModel {

	public Integer nextPk() throws SQLException, DatabaseException {
		int pk = 0;
		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_employee");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {

			throw new DatabaseException("Exception in getting PK");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;
	}

	public long add(EmployeeBean bean) throws ApplicationException {
		Connection conn = null;
		int pk = 0;

		try {

			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("insert into st_employee values(?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getEmployeeName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getDepartment());
			pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			System.out.println("Data Inserted" + i);
			conn.commit();
			pstmt.close();
		} catch (Exception e) {

			try {

				conn.rollback();
			} catch (Exception e2) {

				throw new ApplicationException("Exception : Employee RollBack in add");
			}

			throw new ApplicationException("Exception in add Employee");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		return pk;
	}

	public void update(EmployeeBean bean) throws ApplicationException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("update st_employee set employee_name = ?,last_name = ?,department = ?,"
							+ "dob = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ?  where id = ?");
			pstmt.setString(1, bean.getEmployeeName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setString(3, bean.getDepartment());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());
			pstmt.setLong(9, bean.getId());
			int i = pstmt.executeUpdate();
			conn.commit();
			System.out.println("data updated => " + i);

		} catch (Exception e) {

			try {
				conn.rollback();
			} catch (Exception ex) {

				throw new ApplicationException("Exception : Update Employeeback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Employee " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

	public void Delete(EmployeeBean bean) throws ApplicationException {
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_employee where id = ?");
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
			throw new ApplicationException("Exception : Exception in delete Employee " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

	public EmployeeBean findByPk(long id) throws ApplicationException {
		Connection conn = null;
		EmployeeBean bean = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_employee where id = ?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				bean = new EmployeeBean();
				bean.setId(rs.getLong(1));
				bean.setEmployeeName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDepartment(rs.getString(4));
				bean.setDob(rs.getDate(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception in getting Employee by PK");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List list() throws Exception {
		return search(null, 0, 0);
	}

	public List search(EmployeeBean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_employee where 1=1");

		if (bean != null) {
			if (bean.getEmployeeName() != null && bean.getEmployeeName().length() > 0) {
				sql.append(" and  employee_name like '" + bean.getEmployeeName() + "%'");
			}
			if (bean.getDepartment() != null && bean.getDepartment().length() > 0) {
				sql.append(" and  department like '" + bean.getDepartment() + "%'");
			}
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
				bean = new EmployeeBean();
				bean.setId(rs.getLong(1));
				bean.setEmployeeName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setDepartment(rs.getString(4));
				bean.setDob(rs.getDate(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
				list.add(bean);
			}
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in search employee " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}
}
