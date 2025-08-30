package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.MarksheetBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.util.JDBCDataSource;

/**
 * The MarksheetModel class provides JDBC-based operations for handling student
 * marksheets in the database.
 *
 * It supports CRUD operations, search, and generating a merit list.
 * 
 * Author: Yusuf Khan
 */
public class MarksheetModel {

	private static Logger log = Logger.getLogger(MarksheetModel.class);

	/**
	 * Gets the next primary key value for the st_marksheet table.
	 *
	 * @return the next available primary key
	 * @throws Exception if a database error occurs
	 */
	public Integer nextPk() throws Exception {
		log.debug("MarksheetModel.nextPk() START");
		int pk = 0;
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(ID) from st_marksheet");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (Exception e) {
			log.error("Exception in MarksheetModel.nextPk()", e);
			throw new DatabaseException("Exception: Exception in getting pk:" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.nextPk() END");
		return pk + 1;
	}

	/**
	 * Adds a new marksheet record to the database. Automatically fetches student
	 * name using studentId.
	 *
	 * @param bean the MarksheetBean containing marksheet data
	 * @return the generated primary key
	 * @throws DuplicateRecordException if roll number already exists
	 * @throws ApplicationException     if a database error occurs
	 */
	public long add(MarksheetBean bean) throws DuplicateRecordException, ApplicationException {
		log.debug("MarksheetModel.add() START");
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
			log.debug("Marksheet inserted, rows affected = " + i);
			conn.commit();
		} catch (Exception e) {
			try {

				conn.rollback();
			} catch (Exception e2) {
				log.error("Rollback Exception in MarksheetModel.add()", e2);
				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			log.error("Exception in MarksheetModel.add()", e);
			throw new ApplicationException("Exception : Add Marksheet Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.add() END");
		return pk;
	}

	/**
	 * Updates an existing marksheet in the database. Also updates the student name
	 * based on studentId.
	 *
	 * @param bean the updated MarksheetBean
	 * @throws DuplicateRecordException if roll number is duplicated
	 * @throws ApplicationException     if a database error occurs
	 */
	public void update(MarksheetBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("MarksheetModel.update() START");
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
			log.debug("Marksheet updated, rows affected = " + i);
			conn.commit();
		} catch (Exception e) {
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback Exception in MarksheetModel.update()", e2);
				throw new ApplicationException("Exception : Add rollBack Exception" + e2.getMessage());
			}
			log.error("Exception in MarksheetModel.update()", e);
			throw new ApplicationException("Exception : Add marksheet Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.update() END");
	}

	/**
	 * Deletes a marksheet record based on its ID.
	 *
	 * @param bean the MarksheetBean containing the ID to delete
	 * @throws ApplicationException if a database error occurs
	 */
	public void delete(MarksheetBean bean) throws ApplicationException {
		log.debug("MarksheetModel.delete() START");
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_marksheet where id = ?");
			pstmt.setLong(1, bean.getId());
			int i = pstmt.executeUpdate();
			log.debug("Marksheet deleted, rows affected = " + i);
			conn.commit();
		} catch (Exception e) {
			try {

				conn.rollback();
			} catch (Exception e2) {

				log.error("Rollback Exception in MarksheetModel.delete()", e2);
				throw new ApplicationException("Exception : Add RollBack Exception" + e2.getMessage());
			}
			log.error("Exception in MarksheetModel.delete()", e);
			throw new ApplicationException("Exception : Delete marksheet Exception" + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.delete() END");
	}

	/**
	 * Finds a marksheet by its primary key (ID).
	 *
	 * @param id the ID of the marksheet
	 * @return the MarksheetBean if found, null otherwise
	 * @throws ApplicationException if a database error occurs
	 */
	public MarksheetBean findByPk(long id) throws ApplicationException {
		log.debug("MarksheetModel.findByPk() START");
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
			log.error("Exception in MarksheetModel.findByPk()", e);
			throw new ApplicationException("Exception : Exception getting marksheet by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.findByPk() END");
		return bean;
	}

	/**
	 * Finds a marksheet by its roll number.
	 *
	 * @param rollNo the roll number of the student
	 * @return the MarksheetBean if found, null otherwise
	 * @throws ApplicationException if a database error occurs
	 */
	public MarksheetBean findByRoll(String RollNo) throws ApplicationException {
		log.debug("MarksheetModel.findByRoll() START");
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
			log.error("Exception in MarksheetModel.findByRoll()", e);
			throw new ApplicationException("Exception : Exception getting marksheet by roll");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.findByRoll() END");
		return bean;
	}

	/**
	 * Retrieves a list of all marksheets.
	 *
	 * @return list of MarksheetBean objects
	 * @throws ApplicationException if a database error occurs
	 */
	public List list() throws ApplicationException {
		log.debug("MarksheetModel.list() START");
		List result = search(null, 0, 0);
		log.debug("MarksheetModel.list() END");
		return result;
	}

	/**
	 * Searches for marksheet records with optional filtering and pagination.
	 *
	 * @param bean     the MarksheetBean containing filter criteria (e.g., name)
	 * @param pageNo   the current page number (1-based)
	 * @param pageSize number of records per page (0 for all)
	 * @return list of matching MarksheetBean records
	 * @throws ApplicationException if a database error occurs
	 */
	public List search(MarksheetBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("MarksheetModel.search() START");
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
			log.debug("SQL in MarksheetModel.search(): " + sql.toString());
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
			log.error("Exception in MarksheetModel.search()", e);

			throw new ApplicationException("Exception : Exception in search marksheet " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.search() END");
		return list;
	}

	/**
	 * Retrieves the merit list of marksheets.
	 *
	 * @param pageNo   the current page number (1-based)
	 * @param pageSize number of records per page (0 for all)
	 * @return list of top-performing MarksheetBean records
	 * @throws ApplicationException if a database error occurs
	 */
	public List<MarksheetBean> getMeritList(int pageNo, int pageSize) throws ApplicationException {
		log.debug("MarksheetModel.getMeritList() START");
		ArrayList<MarksheetBean> list = new ArrayList<MarksheetBean>();
		StringBuffer sql = new StringBuffer(

				"select id, roll_no, name, physics, chemistry, maths, (physics + chemistry + maths) as total from st_marksheet where physics > 33 and chemistry > 33 and maths > 33 order by total desc");
		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				MarksheetBean bean = new MarksheetBean();
				bean.setId(rs.getLong(1));
				bean.setRollNo(rs.getString(2));
				bean.setName(rs.getString(3));
				bean.setPhysics(rs.getInt(4));
				bean.setChemistry(rs.getInt(5));
				bean.setMaths(rs.getInt(6));
				list.add(bean);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			log.error("Exception in MarksheetModel.getMeritList()", e);
			throw new ApplicationException("Exception in getting merit list of Marksheet");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("MarksheetModel.getMeritList() END");
		return list;
	}
}
