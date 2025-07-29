package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.CourseBean;
import in.co.rays.bean.SubjectBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

public class SubjectModel {

	public Integer nextPk() throws Exception {

		int pk = 0;
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_subject");

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

	public long add(SubjectBean bean) throws Exception {

		int pk = 0;
		Connection conn = null;
		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectBean existBean = findByName(bean.getName());

		if (existBean != null) {
			throw new DuplicateRecordException("subject name already exist..!!");
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
			System.out.println("Data Inserted:" + i);
			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add Student Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void update(SubjectBean bean) throws Exception {

		Connection conn = null;
		CourseModel courseModel = new CourseModel();
		CourseBean courseBean = courseModel.findByPk(bean.getCourseId());
		bean.setCourseName(courseBean.getName());

		SubjectBean existBean = findByName(bean.getName());

		if (existBean != null && bean.getId() != existBean.getId()) {
			throw new DuplicateRecordException("subject name already exist..!!");
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
			System.out.println("Data Updated:" + i);
			conn.commit();

		} catch (Exception e) {
			try {
				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Add Student Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);

		}
	}

	public void delete(SubjectBean bean) throws ApplicationException {
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();

			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_subject where id = ?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			System.out.println("Data update");
			conn.commit();

		} catch (Exception e) {

			try {

				conn.rollback();

			} catch (Exception e2) {

				throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
			}
			throw new ApplicationException("Exception : Delete Subject Exception" + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
	}

	public SubjectBean findByPk(long id) throws ApplicationException {

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

			throw new ApplicationException("Exception : Exception  getting subject by pk");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	public SubjectBean findByName(String name) throws ApplicationException {
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
				bean.setName(rs.getString(3));
				bean.setCourseId(rs.getLong(3));
				bean.setCourseName(rs.getString(4));
				bean.setDescription(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}

		} catch (Exception e) {

			throw new ApplicationException("Exception : Exception getting subject name");

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return bean;

	}

	public List list() throws ApplicationException {
		return search(null, 0, 0);

	}

	public List search(SubjectBean bean, int pageNo, int pageSize) throws ApplicationException {

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

		System.out.println("sql ==>> " + sql.toString());

		Connection conn = null;
		List list = new ArrayList();

		try {

			conn = JDBCDataSource.getConnection();

			PreparedStatement pstmt = conn.prepareStatement(sql.toString());

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
				list.add(bean);
			}
		} catch (Exception e) {

			throw new ApplicationException("Exception :  Exception in search subject " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		return list;

	}
}
