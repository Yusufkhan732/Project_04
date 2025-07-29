package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class CourseModel {

	public Integer nextPk() throws Exception {

		int pk = 0;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_course");

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				pk = rs.getInt(1);
			}
		} catch (Exception e) {

			throw new DatabaseException("Exception  : Exception getting pk " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

//------------------------------------------------------------------------------------------------
	public long add(CourseBean bean) throws ApplicationException, DuplicateRecordException, Exception {

		CourseBean exsitbBean = findByName(bean.getName());

		if (exsitbBean != null) {
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

			int i = pstmt.executeUpdate();

			System.out.println("data inserted:" + i);
			conn.commit();

		} catch (Exception e) {

			try {

				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception Add RollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add Course Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return pk;

	}

//------------------------------------------------------------------------------------------------
	public void update(CourseBean bean) throws DuplicateRecordException, ApplicationException, Exception {

		CourseBean exitbBean = findByName(bean.getName());

		if (exitbBean != null && bean.getId() != exitbBean.getId()) {

			throw new DuplicateRecordException("Course Name Already Exist...!!!!");
		}

		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("update st_course set name = ?,duration = ?,"
					+ "description = ?, created_by = ?,modified_by = ?,created_datetime = ?,modified_datetime = ? where id = ?");

			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getDuration());
			pstmt.setString(3, bean.getDescription());
			pstmt.setString(4, bean.getCreatedBy());
			pstmt.setString(5, bean.getModifiedBy());
			pstmt.setTimestamp(6, bean.getCreatedDatetime());
			pstmt.setTimestamp(7, bean.getModifiedDatetime());
			pstmt.setLong(8, bean.getId());
			int i = pstmt.executeUpdate();

			System.out.println("update inserted:" + i);

			conn.commit();
		} catch (Exception e) {

			try {

				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add Rollback Exception" + e2.getMessage());
			}

			throw new ApplicationException("Exception : Update Course Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

//------------------------------------------------------------------------------------------------
	public void delete(CourseBean bean) throws Exception {

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);

			PreparedStatement pstmt = conn.prepareStatement("delete from st_course where id = ?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();

			System.out.println("Delete Data:" + i);

			conn.commit();
		} catch (Exception e) {

			try {

				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
			}

			throw new ApplicationException("Exception Delete Course Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

//------------------------------------------------------------------------------------------------
	public CourseBean findByPk(long id) throws Exception {
		Connection conn = null;
		CourseBean bean = null;

		try {
			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_course where id = ?");

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

		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception getting course by pk" + e.getMessage());

		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;

	}

//------------------------------------------------------------------------------------------------
	public CourseBean findByName(String name) throws Exception {

		Connection conn = null;
		CourseBean bean = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select * from st_course where name = ?");

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
		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception getting course Name" + e.getMessage());
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

//------------------------------------------------------------------------------------------------
	public List list() throws Exception {
		return search(null, 0, 0);

	}

//------------------------------------------------------------------------------------------------
	public List search(CourseBean bean, int pageNo, int pageSize) throws Exception {

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

		System.out.println("sql ==>> " + sql.toString());

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

			throw new ApplicationException("Exception: Exception in serach course" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
}
