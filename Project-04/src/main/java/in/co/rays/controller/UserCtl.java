package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * UserCtl Controller class to perform add, update, and reset operations on User
 * data. It handles both GET and POST requests for user management.
 * 
 * @author Yusuf Khan
 */
@WebServlet(name = "UserCtl", urlPatterns = { "/ctl/UserCtl" })
public class UserCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(UserCtl.class);

	/**
	 * Loads the list of roles before the form is displayed.
	 * 
	 * @param request the HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("UserCtl preload started");
		RoleModel model = new RoleModel();

		try {
			List<RoleBean> roleList = model.list();
			System.out.println("rolelist");
			request.setAttribute("roleList", roleList);
			log.debug("Role list size=" + (roleList != null ? roleList.size() : 0));
		} catch (Exception e) {
			log.error("Error in preload()", e);
			e.printStackTrace();
		}
		log.debug("UserCtl preload ended");
	}

	/**
	 * Validates input data coming from the User form.
	 * 
	 * @param request the HttpServletRequest
	 * @return true if all inputs are valid, false otherwise
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("UserCtl validate started");

		boolean pass = true;

		String login = request.getParameter("login");
		String dob = request.getParameter("dob");
		String password = request.getParameter("password");

		if (DataValidator.isNull(request.getParameter("firstName"))) {

			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass = false;
			log.debug("First Name is null");

		} else if (!DataValidator.isName(request.getParameter("firstName"))) {
			request.setAttribute("firstName", "Invalid First Name");
			pass = false;
			log.debug("Invalid First Name");
		}

		if (DataValidator.isNull(request.getParameter("lastName"))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;
			log.debug("Last Name is null");

		} else if (!DataValidator.isName(request.getParameter("lastName"))) {
			request.setAttribute("lastName", "Invalid Last Name");
			pass = false;
			log.debug("Invalid Last Name");
		}

		if (DataValidator.isNull(login)) {
			request.setAttribute("login", PropertyReader.getValue("error.require", "Login Id"));
			pass = false;
			log.debug("Login Id is null");

		} else if (!DataValidator.isEmail(login)) {
			request.setAttribute("login", "Invalid login Id");
			pass = false;
			log.debug("Invalid login Id");
		}

		if (DataValidator.isNull(password)) {
			request.setAttribute("password", PropertyReader.getValue("error.require", "Password"));
			pass = false;
			log.debug("Password is null");

		} else if (!DataValidator.isPasswordLength(password)) {
			request.setAttribute("password", "Password should be 8 to 12 characters");
			pass = false;
			log.debug("Password length invalid");

		} else if (!DataValidator.isPassword(password)) {
			request.setAttribute("password", "Must contain uppercase, lowercase, digit & special character");
			pass = false;
			log.debug("Password format invalid");
		}

		if (DataValidator.isNull(request.getParameter("confirmPassword"))) {
			request.setAttribute("confirmPassword", PropertyReader.getValue("error.require", "Confirm Password"));
			pass = false;
			log.debug("Confirm Password is null");
		}

		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
			log.debug("Gender is null");
		}
		if (DataValidator.isNull(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
			pass = false;
			log.debug("DOB is null");

		} else if (!DataValidator.isDate(dob)) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
			pass = false;
			log.debug("DOB invalid format");
		}
		if (DataValidator.isNull(request.getParameter("roleId"))) {
			request.setAttribute("roleId", PropertyReader.getValue("error.require", "Role"));
			pass = false;
			log.debug("Role is null");
		}
		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "MobileNo"));
			pass = false;
			log.debug("Mobile No is null");

		} else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Mobile No must have 10 digits");
			pass = false;
			log.debug("Mobile No length invalid");

		} else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Invalid Mobile No");
			pass = false;
			log.debug("Mobile No format invalid");
		}
		if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))
				&& !"".equals(request.getParameter("confirmPassword"))) {
			request.setAttribute("confirmPassword", "Password and Confirm Password must be Same!");
			pass = false;
			log.debug("Password and Confirm Password mismatch");
		}

		log.debug("UserCtl validate ended with pass=" + pass);
		return pass;
	}

	/**
	 * Populates a UserBean from request parameters.
	 * 
	 * @param request the HttpServletRequest
	 * @return populated UserBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("UserCtl populateBean started");

		UserBean bean = new UserBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setRoleId(DataUtility.getLong(request.getParameter("roleId")));

		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));

		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));

		bean.setLogin(DataUtility.getString(request.getParameter("login")));

		bean.setPassword(DataUtility.getString(request.getParameter("password")));

		bean.setConfirmPassword(DataUtility.getString(request.getParameter("confirmPassword")));

		bean.setGender(DataUtility.getString(request.getParameter("gender")));

		bean.setDob(DataUtility.getDate(request.getParameter("dob")));

		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));

		populateDTO(bean, request);

		log.debug("UserCtl populateBean ended");

		return bean;
	}

	/**
	 * Handles HTTP GET request to display the user form for add/update.
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("UserCtl doGet started");

		// String op = DataUtility.getString(request.getParameter("operation"));

		UserModel model = new UserModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0) {

			UserBean bean;

			try {

				bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
				log.debug("User found with id=" + id);

			} catch (ApplicationException e) {
				log.error("Error in doGet()", e);
				e.printStackTrace();
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);

		log.debug("UserCtl doGet ended");
	}

	/**
	 * Handles HTTP POST request for Save, Update, Reset and Cancel operations.
	 * 
	 * @param request  the HttpServletRequest
	 * @param response the HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("UserCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));

		UserModel model = new UserModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			UserBean bean = (UserBean) populateBean(request);

			try {

				long pk = model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Data is Successfully Saved", request);
				log.debug("User saved successfully with id=" + pk);

			} catch (ApplicationException e) {
				log.error("ApplicationException in doPost Save", e);
				e.printStackTrace();
				return;

			} catch (DuplicateRecordException e) {
				log.error("DuplicateRecordException in doPost Save", e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login Id already exists", request);
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			UserBean bean = (UserBean) populateBean(request);

			try {
				if (bean.getId() > 0) {
					model.update(bean);
					log.debug("User updated successfully with id=" + bean.getId());
				}
				ServletUtility.setBean(bean, request);

				ServletUtility.setSuccessMessage("Data is successfully updated", request);

			} catch (ApplicationException e) {
				log.error("ApplicationException in doPost Update", e);
				e.printStackTrace();
				return;

			} catch (DuplicateRecordException e) {
				log.error("DuplicateRecordException in doPost Update", e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login Id already exists", request);
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			log.debug("Operation Cancel - redirecting to User List");
			ServletUtility.redirect(ORSView.USER_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			log.debug("Operation Reset - redirecting to User Form");
			ServletUtility.redirect(ORSView.USER_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("UserCtl doPost ended");
	}

	/**
	 * Returns the view for user form (JSP page).
	 * 
	 * @return the JSP path for the user view
	 */
	@Override
	protected String getView() {
		return ORSView.USER_VIEW;
	}
}