package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.CollegeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class CollegeModel {

	public Integer nextPk() throws DatabaseException {

		int pk = 0;
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_college");

			ResultSet rs = pstmt.executeQuery();
			// System.out.println("NextPk =:" + pk);
			while (rs.next()) {

				pk = rs.getInt(1);

			}

		} catch (Exception e) {
			throw new DatabaseException("Exception:   Exception in getting pk:" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		return pk + 1;
	}

//------------------------------------------------------------------------------------------------
	public long add(CollegeBean bean) throws ApplicationException, DuplicateRecordException {

		int pk = 0;
		Connection conn = null;

		CollegeBean existbBean = findByName(bean.getName());
		if (existbBean != null) {

			throw new DuplicateRecordException("college name alrady exist....!!!");

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

			int i = pstmt.executeUpdate();
			System.out.println("Data Inserted:" + i);
			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add college Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

//------------------------------------------------------------------------------------------------
	public void update(CollegeBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;

		CollegeBean existbBean = findByName(bean.getName());
		if (existbBean != null && bean.getId() != existbBean.getId()) {

			throw new DuplicateRecordException("college name already exist....!!!");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("update st_college set name = ?,address = ?,state = ?, city = ?,phone_no = ?,"
							+ "created_by = ?,modified_by = ?,created_datetime = ?,modified_datetime = ? where id = ?");

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

			int i = pstmt.executeUpdate();

			System.out.println("Updated:" + i);
			conn.commit();
		} catch (Exception e) {

			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add RollBack Exception" + e2);
			}
			throw new ApplicationException("Exception : Update college Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

//------------------------------------------------------------------------------------------------
	public void delete(CollegeBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_college where id = ?");

			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			conn.commit();
			System.out.println("Data Deleted:" + i);

		} catch (Exception e) {
			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception: Add RollBack " + e2.getMessage());
			}

			throw new ApplicationException("Exception : college Delete Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);

		}
	}

//------------------------------------------------------------------------------------------------
	public CollegeBean findByPk(long id) throws ApplicationException {

		Connection conn = null;
		CollegeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where id = ?");
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
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception  getting college by pk");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

//------------------------------------------------------------------------------------------------
	public CollegeBean findByName(String name) throws ApplicationException {
		Connection conn = null;
		CollegeBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_college where name = ?");
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

		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception getting college by name");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;

	}

//-----------------------------------------------------------------------------------------------
	public List list() throws ApplicationException {
		return search(null, 0, 0);

	}

	public List search(CollegeBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;
		List list = new ArrayList();
		try {

			conn = JDBCDataSource.getConnection();

			StringBuffer sql = new StringBuffer("select * from st_college where 1=1");

			if (bean != null) {
				if (bean.getName() != null && bean.getName().length() > 0) {
					sql.append(" and name like '" + bean.getName() + "%'");
				}

				if (bean.getCity() != null && bean.getCity().length() > 0) {
					sql.append(" and city like '" + bean.getCity() + "%'");
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				sql.append(" limit " + pageNo + ", " + pageSize);
			}
			System.out.println("sql==>" + sql);
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception :  Exception in search College " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		return list;
	}
}
