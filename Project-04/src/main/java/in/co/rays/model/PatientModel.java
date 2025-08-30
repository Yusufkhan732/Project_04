package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.PatientBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class PatientModel {

	public Integer nextPk() throws DatabaseException {

		int pk = 0;

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID)from st_patient");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();

			throw new DatabaseException("Exception : Exception getting by pk" + e.getMessage());

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public long add(PatientBean bean) throws ApplicationException, DuplicateRecordException {

		int pk = 0;
		Connection conn = null;

		PatientBean exitbean = findByEmail(bean.getEmail());
		if (exitbean != null) {
			throw new DuplicateRecordException("Email already exists....!!!");

		}

		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("insert into st_patient values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getGender());
			pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(6, bean.getContactNumber());
			pstmt.setString(7, bean.getEmail());
			pstmt.setString(8, bean.getAddress());
			pstmt.setString(9, bean.getBloodGroup());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			System.out.println("Data Inserted :" + i);
		} catch (Exception e) {
			e.printStackTrace();
			try {

			} catch (Exception e2) {
				throw new ApplicationException("Exception: add  Rollback Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception: Exception Add Patient" + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void update(PatientBean bean) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		PatientBean exitbean = findByEmail(bean.getEmail());

		if (bean != null && bean.getId() != exitbean.getId()) {
			throw new DuplicateRecordException("Email already exists...!!");
		}
		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("update st_patient set first_name = ?,"
					+ "last_name = ?,gender = ?,dob = ?,contact_number = ?,email = ?,"
					+ "address = ? ,blood_group = ?,created_by = ?,modified_by = ?,"
					+ "created_datetime = ?,modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setString(3, bean.getGender());
			pstmt.setDate(4, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(5, bean.getContactNumber());
			pstmt.setString(6, bean.getEmail());
			pstmt.setString(7, bean.getAddress());
			pstmt.setString(8, bean.getBloodGroup());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
			pstmt.setLong(13, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("Data Update " + i);
			conn.commit();

		} catch (Exception e) {
			e.printStackTrace();
			try {

				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Add rollback Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Patient Update Exception" + e.getMessage());
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

	public void delete(PatientBean bean) throws ApplicationException {

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_patient where id = ?");

			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			System.out.println("Data Delete" + i);
			conn.commit();
		} catch (Exception e) {

			e.printStackTrace();
			try {

				conn.rollback();
			} catch (Exception e2) {
				throw new ApplicationException("Exception: Delete Rollback Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception: Delete Patient Exception" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public PatientBean findByPk(long id) throws ApplicationException {

		Connection conn = null;
		PatientBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_patient where id = ?");
			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDob(rs.getDate(5));
				bean.setContactNumber(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setAddress(rs.getString(8));
				bean.setBloodGroup(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception: Exception getting Patient Pk" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public PatientBean findByEmail(String email) throws ApplicationException {
		Connection conn = null;
		PatientBean bean = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_patient where email = ?");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDob(rs.getDate(5));
				bean.setContactNumber(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setAddress(rs.getString(8));
				bean.setBloodGroup(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception: Exception getting Patient email" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List list() throws ApplicationException {
		return search(null, 0, 0);

	}

	public List search(PatientBean bean, int pageNo, int pageSize) throws ApplicationException {
		StringBuffer sql = new StringBuffer("select * from st_patient where 1=1");

		if (bean != null) {
			if (bean.getId() > 0) {

				sql.append(" and id = " + bean.getId());
			}
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" and first_name like '" + bean.getFirstName() + "%'");

			}
			if (bean.getEmail() != null && bean.getEmail().length() > 0) {
				sql.append(" and eamil ilke '" + bean.getEmail() + "%'");

			}
		}
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);

		}
		System.out.println("sql>>" + sql.toString());

		Connection conn = null;
		ArrayList list = new ArrayList();

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new PatientBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setGender(rs.getString(4));
				bean.setDob(rs.getDate(5));
				bean.setContactNumber(rs.getString(6));
				bean.setEmail(rs.getString(7));
				bean.setAddress(rs.getString(8));
				bean.setBloodGroup(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception: Exception Search Patient" + e.getMessage());
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
}