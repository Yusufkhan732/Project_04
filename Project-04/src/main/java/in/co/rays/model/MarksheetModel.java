package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.MarksheetBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class MarksheetModel {

	public Integer nextPk() throws Exception {

		int pk = 0;
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_marksheet");

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

	public long add(MarksheetBean bean) throws DuplicateRecordException, ApplicationException {

		StudentModel stmodel = new StudentModel();
		StudentBean studentbean = stmodel.findByPk(bean.getStudentId());
		bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

		MarksheetBean existBean = findByRoll(bean.getRollNo());

		if (existBean != null) {
			throw new DuplicateRecordException("roll no already exist..!!");
		}
		int pk = 0;
		Connection conn = null;
		try {

			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_marksheet values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getRollNo());
			pstmt.setLong(3, bean.getStudentId());
			pstmt.setString(4, bean.getName());
			pstmt.setInt(5, bean.getPhysics());
			pstmt.setInt(6, bean.getChemistry());
			pstmt.setInt(7, bean.getMaths());
			pstmt.setString(8, bean.getCreatedBy());
			pstmt.setString(9, bean.getModifiedBy());
			pstmt.setTimestamp(10, bean.getCreatedDatetime());
			pstmt.setTimestamp(11, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();

			System.out.println("data inserted => " + i);
			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add Marksheet Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void update(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {

		Connection conn = null;
		StudentModel studentModel = new StudentModel();
		StudentBean studentbean = studentModel.findByPk(bean.getStudentId());
		bean.setName(studentbean.getFirstName() + " " + studentbean.getLastName());

		MarksheetBean existBean = findByRoll(bean.getRollNo());

		if (existBean != null && bean.getId() != existBean.getId()) {
			throw new DuplicateRecordException("roll no already exist..!!");
		}

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_marksheet set roll_no = ?, student_id = ?, name = ?, physics = ?, chemistry = ?, maths = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getRollNo());
			pstmt.setLong(2, bean.getStudentId());
			pstmt.setString(3, bean.getName());
			pstmt.setInt(4, bean.getPhysics());
			pstmt.setInt(5, bean.getChemistry());
			pstmt.setInt(6, bean.getMaths());
			pstmt.setString(7, bean.getCreatedBy());
			pstmt.setString(8, bean.getModifiedBy());
			pstmt.setTimestamp(9, bean.getCreatedDatetime());
			pstmt.setTimestamp(10, bean.getModifiedDatetime());
			pstmt.setLong(11, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("Data upadte = " + i);
			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add marksheet Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);

		}
	}

	public void delete(long id) throws ApplicationException {
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_marksheet where id = ?");

			pstmt.setLong(1, id);

			int i = pstmt.executeUpdate();
			System.out.println("Delete = >" + i);
			conn.commit();

		} catch (Exception e) {

			try {

				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Delete marksheet Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

	public MarksheetBean findByPk(long id) throws ApplicationException {

		Connection conn = null;
		MarksheetBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_marksheet where id = ?");

			pstmt.setLong(1, id);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception  getting marksheet by pk");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public MarksheetBean findByRoll(String RollNo) throws ApplicationException {

		Connection conn = null;
		MarksheetBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_marksheet where roll_no = ?");

			pstmt.setString(1, RollNo);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
			}
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception  getting marksheet by pk");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public List list() throws ApplicationException {
		return search(null, 0, 0);

	}

	public List search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {

		Connection conn = null;
		List list = new ArrayList();

		try {

			conn = JDBCDataSource.getConnection();

			StringBuffer sql = new StringBuffer("select * from st_marksheet where 1=1");

			if (bean != null) {
				if (bean.getName() != null && bean.getName().length() > 0) {
					sql.append(" and name like '" + bean.getName() + "%'");
				}
			}

			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				sql.append(" limit " + pageNo + ", " + pageSize);
			}

			System.out.println("sql ==>> " + sql.toString());

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setStudentId(rs.getLong(3));
				bean.setName(rs.getString(4));
				bean.setPhysics(rs.getInt(5));
				bean.setChemistry(rs.getInt(6));
				bean.setMaths(rs.getInt(7));
				bean.setCreatedBy(rs.getString(8));
				bean.setModifiedBy(rs.getString(9));
				bean.setCreatedDatetime(rs.getTimestamp(10));
				bean.setModifiedDatetime(rs.getTimestamp(11));
				list.add(bean);
			}
		} catch (Exception e) {

			throw new ApplicationException("Exception :  Exception in search marksheet " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
}
