package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CollegeBean;
import in.co.rays.bean.StudentBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CollegeModel;
import in.co.rays.model.StudentModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * Controller servlet for handling Student operations like add, update, and
 * load. It validates input, interacts with the model, and forwards to
 * appropriate views.
 * 
 * @author Yusuf Khan
 */
@WebServlet(name = "StudentCtl", urlPatterns = { "/ctl/StudentCtl" })
public class StudentCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(StudentCtl.class);

	/**
	 * Loads the list of colleges for populating the dropdown before displaying the
	 * student form.
	 * 
	 * @param request HttpServletRequest object
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("StudentCtl preload started");
		CollegeModel model = new CollegeModel();
		try {
			List<CollegeBean> list = model.list();
			request.setAttribute("list", list);
			if (list == null) {
				log.warn("College list is null in preload");
			}
		} catch (ApplicationException e) {
			log.error("ApplicationException in preload", e);
		}
		log.debug("StudentCtl preload ended");
	}

	/**
	 * Validates input parameters from the HTTP request. Checks for required fields,
	 * proper formats (e.g., email, phone), and sets appropriate error messages if
	 * validation fails.
	 * 
	 * @param request HttpServletRequest object
	 * @return boolean true if all inputs are valid, false otherwise
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("StudentCtl validate started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("firstName"))) {
			request.setAttribute("firstName", PropertyReader.getValue("error.require", "First Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("firstName"))) {
			request.setAttribute("firstName", "Invalid First Name");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("lastName"))) {
			request.setAttribute("lastName", PropertyReader.getValue("error.require", "Last Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("lastName"))) {
			request.setAttribute("lastName", "Invalid Last Name");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", PropertyReader.getValue("error.require", "Mobile No"));
			pass = false;

		} else if (!DataValidator.isPhoneLength(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Mobile No must have 10 digits");
			pass = false;

		} else if (!DataValidator.isPhoneNo(request.getParameter("mobileNo"))) {
			request.setAttribute("mobileNo", "Invalid Mobile No");
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("gender"))) {
			request.setAttribute("gender", PropertyReader.getValue("error.require", "Gender"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("email"))) {
			request.setAttribute("email", PropertyReader.getValue("error.require", "Email "));
			pass = false;

		} else if (!DataValidator.isEmail(request.getParameter("email"))) {
			request.setAttribute("email", PropertyReader.getValue("error.email", "Email "));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("collegeId"))) {
			request.setAttribute("collegeId", PropertyReader.getValue("error.require", "College Name"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
			pass = false;

		} else if (!DataValidator.isDate(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
			pass = false;
		}

		log.debug("StudentCtl validate ended with result: " + pass);
		return pass;
	}

	/**
	 * Populates and returns a StudentBean object using data from the HTTP request
	 * parameters.
	 * 
	 * @param request HttpServletRequest object
	 * @return BaseBean populated with student data
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("StudentCtl populateBean started");

		StudentBean bean = new StudentBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFirstName(DataUtility.getString(request.getParameter("firstName")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastName")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setGender(DataUtility.getString(request.getParameter("gender")));
		bean.setMobileNo(DataUtility.getString(request.getParameter("mobileNo")));
		bean.setEmail(DataUtility.getString(request.getParameter("email")));
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeId")));

		populateDTO(bean, request);

		log.debug("StudentCtl populateBean ended");
		return bean;
	}

	/**
	 * Handles HTTP GET requests for loading student details if an ID is provided,
	 * otherwise displays a blank form.
	 * 
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("StudentCtl doGet started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		StudentModel model = new StudentModel();

		if (id > 0 || op != null) {

			StudentBean bean;
			try {
				bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);

			} catch (ApplicationException e) {
				log.error("ApplicationException in doGet", e);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("StudentCtl doGet ended");
	}

	/**
	 * Handles HTTP POST requests for save, update, cancel, and reset operations on
	 * student data.
	 * 
	 * @param request  HttpServletRequest object
	 * @param response HttpServletResponse object
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("StudentCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));

		StudentModel model = new StudentModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			StudentBean bean = (StudentBean) populateBean(request);
			try {
				long pk = model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Student added successfully", request);
				log.info("Student added successfully, ID: " + pk);

			} catch (ApplicationException e) {
				log.error("ApplicationException in doPost - Save", e);
				return;

			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Email already exists", request);
				log.warn("Duplicate email found while saving student: " + bean.getEmail());
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			StudentBean bean = (StudentBean) populateBean(request);
			try {
				if (bean.getId() > 0) {
					model.update(bean);
					log.info("Student updated successfully, ID: " + bean.getId());
				}
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Student updated successfully", request);
			} catch (ApplicationException e) {
				log.error("ApplicationException in doPost - Update", e);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Email already exists", request);
				log.warn("Duplicate email found while updating student: " + bean.getEmail());
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			log.debug("Operation Cancel invoked");
			ServletUtility.redirect(ORSView.STUDENT_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			log.debug("Operation Reset invoked");
			ServletUtility.redirect(ORSView.STUDENT_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("StudentCtl doPost ended");
	}

	/**
	 * Returns the view (JSP) page path associated with the student form.
	 * 
	 * @return String representing the student view JSP path
	 */
	@Override
	protected String getView() {
		return ORSView.STUDENT_VIEW;
	}
}
