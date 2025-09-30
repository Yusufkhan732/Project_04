
package in.co.rays.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DatabaseException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.exception.RecordNotFoundException;
import in.co.rays.util.EmailBuilder;
import in.co.rays.util.EmailMessage;
import in.co.rays.util.EmailUtility;
import in.co.rays.util.JDBCDataSource;

/**
 * UserModel class provides methods to perform CRUD operations, authentication,
 * password management, and user registration on UserBean. It interacts with the
 * database and manages users in the st_user table.
 * 
 * @Author Yusuf Khan
 * @version 1.0
 */
public class UserModel {

	public static Logger log = Logger.getLogger(UserModel.class);

	/**
	 * Returns the next primary key for the st_user table.
	 * 
	 * @return the next primary key as Integer
	 * @throws DatabaseException if a database access error occurs
	 */

	public int nextPk() throws DatabaseException {
		log.debug("UserModel nextPk Started");
		Connection conn = null;
		int pk = 0;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(ID) FROM ST_USER");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				pk = rs.getInt(1);
			}
			rs.close();
			pstmt.close();

			log.debug("UserModel nextPk successful, pk = " + pk);
		} catch (Exception e) {

			log.error("Database Exception in nextPk", e);

			throw new DatabaseException("Exception : Exception in getting pk");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("UserModel nextPk Ended");
		return pk + 1;
	}

	/**
	 * Adds a new user to the database.
	 * 
	 * @param bean UserBean object containing user details
	 * @return the primary key of the newly added user
	 * @throws DuplicateRecordException if a user with the same login already exists
	 * @throws ApplicationException     if an unexpected error occurs during the
	 *                                  operation
	 */
	public long add(UserBean bean) throws DuplicateRecordException, ApplicationException {

		log.debug("UserModel add Started");
		Connection conn = null;
		int pk = 0;
		UserBean existBean = findByLogin(bean.getLogin());
		if (existBean != null) {

			log.error("Duplicate login found: " + bean.getLogin());
			throw new DuplicateRecordException("login already exist..!!");
		}
		try {

			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_user values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setLong(1, pk);
			pstmt.setString(2, bean.getFirstName());
			pstmt.setString(3, bean.getLastName());
			pstmt.setString(4, bean.getLogin());
			pstmt.setString(5, bean.getPassword());
			pstmt.setDate(6, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(7, bean.getMobileNo());
			pstmt.setLong(8, bean.getRoleId());
			pstmt.setString(9, bean.getGender());
			pstmt.setString(10, bean.getCreatedBy());
			pstmt.setString(11, bean.getModifiedBy());
			pstmt.setTimestamp(12, bean.getCreatedDatetime());
			pstmt.setTimestamp(13, bean.getModifiedDatetime());
			int i = pstmt.executeUpdate();
			conn.commit();

			System.out.println("data inserted => " + i);
			log.debug("User added successfully, pk = " + pk);

		} catch (Exception e) {

			log.error("Exception in add User", e);
			try {

				conn.rollback();
			} catch (Exception ex) {

				log.error("Rollback Exception in add()", ex);
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add User " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("UserModel add Ended");
		return pk;
	}

	/**
	 * Updates an existing user's information in the database.
	 * 
	 * @param bean UserBean object containing updated user details
	 * @throws ApplicationException     if an unexpected error occurs during the
	 *                                  operation
	 * @throws DuplicateRecordException if a user with the same login already exists
	 */
	public void update(UserBean bean) throws ApplicationException, DuplicateRecordException {

		log.debug("UserModel update Started");

		Connection conn = null;
		UserBean existBean = findByLogin(bean.getLogin());

		if (existBean != null && bean.getId() != existBean.getId()) {

			log.error("Duplicate login found: " + bean.getLogin());

			throw new DuplicateRecordException("login already exist..!!");
		}
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_user set first_name = ?, last_name = ?, login = ?, password = ?, dob = ?, mobile_no = ?, role_id = ?, gender = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ?  where id = ?");
			pstmt.setString(1, bean.getFirstName());
			pstmt.setString(2, bean.getLastName());
			pstmt.setString(3, bean.getLogin());
			pstmt.setString(4, bean.getPassword());
			pstmt.setDate(5, new java.sql.Date(bean.getDob().getTime()));
			pstmt.setString(6, bean.getMobileNo());
			pstmt.setLong(7, bean.getRoleId());
			pstmt.setString(8, bean.getGender());
			pstmt.setString(9, bean.getCreatedBy());
			pstmt.setString(10, bean.getModifiedBy());
			pstmt.setTimestamp(11, bean.getCreatedDatetime());
			pstmt.setTimestamp(12, bean.getModifiedDatetime());
			pstmt.setLong(13, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			System.out.println("data updated => " + i);

			log.debug("User updated successfully, ID = " + bean.getId());
		} catch (Exception e) {

			log.error("Exception in update User", e);
			try {
				conn.rollback();
			} catch (Exception ex) {

				log.error("Rollback Exception in update()", ex);
				throw new ApplicationException("Exception : Update rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating User " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("UserModel update Ended");
	}

	/**
	 * Deletes a user from the database.
	 * 
	 * @param bean UserBean object containing the user ID to delete
	 * @throws ApplicationException if an unexpected error occurs during the
	 *                              operation
	 */
	public void delete(UserBean bean) throws ApplicationException {

		log.debug("UserModel delete Started");
		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_user where id = ?");
			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			System.out.println("data deleted => " + i);

			log.debug("User deleted successfully, ID = " + bean.getId());
		} catch (Exception e) {

			log.error("Exception in delete User", e);
			try {

				conn.rollback();
			} catch (Exception ex) {

				log.error("Rollback Exception in delete()", ex);

				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete User " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("UserModel delete Ended");
	}

	/**
	 * Finds a user by primary key.
	 * 
	 * @param id the primary key of the user to find
	 * @return UserBean object if found, otherwise null
	 * @throws ApplicationException if an unexpected error occurs during the
	 *                              operation
	 */
	public UserBean findByPk(long id) throws ApplicationException {

		log.debug("UserModel findByPk Started");
		Connection conn = null;
		UserBean bean = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_user where id = ?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			log.debug("User found by PK, ID = " + id);
		} catch (Exception e) {

			log.error("Exception in findByPk", e);

			throw new ApplicationException("Exception : Exception in getting User by PK");
		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("UserModel findByPk Ended");
		return bean;

	}

	/**
	 * Finds a user by login.
	 * 
	 * @param login the login ID of the user to find
	 * @return UserBean object if found, otherwise null
	 * @throws ApplicationException if an unexpected error occurs during the
	 *                              operation
	 */
	public UserBean findByLogin(String login) throws ApplicationException {

		log.debug("UserModel findByLogin Started");
		Connection conn = null;
		UserBean bean = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_user where login = ?");
			pstmt.setString(1, login);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			log.debug("User found by login = " + login);
		} catch (Exception e) {

			log.error("Exception in findByLogin", e);
			throw new ApplicationException("Exception : Exception in getting User by login " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}
		log.debug("UserModel findByLogin Ended");
		return bean;
	}

	/**
	 * Authenticates a user by login ID and password.
	 * 
	 * @param loginId  the login ID of the user
	 * @param password the password of the user
	 * @return UserBean object if authentication is successful, otherwise null
	 * @throws ApplicationException if an unexpected error occurs during the
	 *                              operation
	 */
	public UserBean authenticate(String loginId, String password) throws ApplicationException {

		log.debug("UserModel authenticate Started");
		Connection conn = null;
		UserBean bean = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_user where login = ? and password = ?");
			pstmt.setString(1, loginId);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
			}
			log.debug("Authentication checked for login = " + loginId);
		} catch (Exception e) {

			log.error("Exception in authenticate", e);
			throw new ApplicationException("Exception : Exception in get roles " + e);

		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("UserModel authenticate Ended");
		return bean;
	}

	/**
	 * Searches for users based on given criteria with pagination.
	 * 
	 * @param bean     UserBean object containing search criteria
	 * @param pageNo   the page number to retrieve (for pagination)
	 * @param pageSize the number of records per page (for pagination)
	 * @return List of UserBean objects matching the search criteria
	 * @throws ApplicationException if an unexpe cted error occurs during the
	 *                              operation
	 */

	public List search(UserBean bean, int pageNo, int pageSize) throws ApplicationException {
		log.debug("UserModel search Started");
		StringBuffer sql = new StringBuffer("select * from st_user where 1=1");

		if (bean != null) {
			if (bean.getFirstName() != null && bean.getFirstName().length() > 0) {
				sql.append(" and first_name like '" + bean.getFirstName() + "%'");
			}
			if (bean.getLogin() != null && bean.getLogin().length() > 0) {
				sql.append(" and login like '" + bean.getLogin() + "%'");
			}
			if (bean.getDob() != null && bean.getDob().getTime() > 0) {
				sql.append(" and dob like '" + new java.sql.Date(bean.getDob().getTime()) + "%'");
			}

			if (bean.getRoleId() > 0) {
				sql.append(" and role_id = " + bean.getRoleId());
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
				bean = new UserBean();
				bean.setId(rs.getLong(1));
				bean.setFirstName(rs.getString(2));
				bean.setLastName(rs.getString(3));
				bean.setLogin(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setDob(rs.getDate(6));
				bean.setMobileNo(rs.getString(7));
				bean.setRoleId(rs.getLong(8));
				bean.setGender(rs.getString(9));
				bean.setCreatedBy(rs.getString(10));
				bean.setModifiedBy(rs.getString(11));
				bean.setCreatedDatetime(rs.getTimestamp(12));
				bean.setModifiedDatetime(rs.getTimestamp(13));
				list.add(bean);
			}
			log.debug("Search executed, records found = " + list.size());
		} catch (Exception e) {
			log.error("Exception in search user", e);
			throw new ApplicationException("Exception : Exception in search user " + e);
		} finally {

			JDBCDataSource.closeConnection(conn);
		}

		log.debug("UserModel search Ended");
		return list;
	}

	/**
	 * Changes the password for a user.
	 * 
	 * @param id          the primary key of the user
	 * @param oldPassword the old password
	 * @param newPassword the new password to set
	 * @return true if password changed successfully, false otherwise
	 * @throws RecordNotFoundException if the user is not found or old password
	 *                                 doesn't match
	 * @throws ApplicationException    if an unexpected error occurs during the
	 *                                 operation
	 */
	public boolean changePassword(Long id, String oldPassword, String newPassword)
			throws RecordNotFoundException, ApplicationException {
		log.debug("UserModel changePassword Started");

		boolean flag = false;
		UserBean beanExist = null;
		beanExist = findByPk(id);

		if (beanExist != null && beanExist.getPassword().equals(oldPassword)) {
			beanExist.setPassword(newPassword);
			try {

				update(beanExist);
			} catch (DuplicateRecordException e) {

				log.error("DuplicateRecordException in changePassword", e);
				throw new ApplicationException("LoginId is already exist");
			}
			flag = true;
		} else {

			log.error("RecordNotFoundException in changePassword for ID = " + id);
			throw new RecordNotFoundException("Login not exist");
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", beanExist.getLogin());
		map.put("password", beanExist.getPassword());
		map.put("firstName", beanExist.getFirstName());
		map.put("lastName", beanExist.getLastName());
		String message = EmailBuilder.getChangePasswordMessage(map);
		EmailMessage msg = new EmailMessage();
		msg.setTo(beanExist.getLogin());
		msg.setSubject("Rays ORS Password has been changed Successfully.");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		EmailUtility.sendMail(msg);
		log.debug("Password changed and email sent to " + beanExist.getLogin());
		log.debug("UserModel changePassword Ended");
		return flag;
	}

	/**
	 * Sends the forgotten password to the user's email.
	 * 
	 * @param login the login ID (email) of the user
	 * @return true if the password is sent successfully, false otherwise
	 * @throws ApplicationException    if an unexpected error occurs during the
	 *                                 operation
	 * @throws RecordNotFoundException if the user with given login is not found
	 */
	public boolean forgetPassword(String login) throws ApplicationException, RecordNotFoundException {

		log.debug("UserModel forgetPassword Started");
		UserBean userData = findByLogin(login);
		boolean flag = false;

		if (userData == null) {

			log.error("RecordNotFoundException in forgetPassword for login = " + login);
			throw new RecordNotFoundException("Email ID does not exists !");
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", userData.getLogin());
		map.put("password", userData.getPassword());
		map.put("firstName", userData.getFirstName());
		map.put("lastName", userData.getLastName());
		String message = EmailBuilder.getForgetPasswordMessage(map);
		EmailMessage msg = new EmailMessage();
		msg.setTo(login);
		msg.setSubject("Rays ORS Password Reset");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		EmailUtility.sendMail(msg);
		flag = true;
		log.debug("Password sent successfully to " + login);
		log.debug("UserModel forgetPassword Ended");
		return flag;
	}

	public long registerUser(UserBean bean) throws ApplicationException, DuplicateRecordException {
		log.debug("UserModel registerUser Started");
		long pk = add(bean);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("login", bean.getLogin());
		map.put("password", bean.getPassword());
		String message = EmailBuilder.getUserRegistrationMessage(map);
		EmailMessage msg = new EmailMessage();
		msg.setTo(bean.getLogin());
		msg.setSubject("Registration is successful for ORS Project");
		msg.setMessage(message);
		msg.setMessageType(EmailMessage.HTML_MSG);
		EmailUtility.sendMail(msg);
		log.debug("User registered successfully, pk = " + pk);
		log.debug("UserModel registerUser Ended");
		return pk;
	}
}
