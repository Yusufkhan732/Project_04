package in.co.rays.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.CourseBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * CourseCtl is a controller servlet that handles add, update, and display
 * operations for Course entities. It validates input data, populates CourseBean
 * from request parameters, and interacts with CourseModel.
 * 
 * @author Yusuf khan
 */
@WebServlet(name = "CourseCtl", urlPatterns = { "/ctl/CourseCtl" })
public class CourseCtl extends BaseCtl {

	/** The log. */
	private static Logger log = Logger.getLogger(CourseCtl.class);

	/**
	 * Validates the request parameters for Course form. Checks for required fields
	 * and valid formats.
	 * 
	 * @param request HttpServletRequest containing parameters to validate.
	 * @return true if all validations pass, false otherwise.
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.debug("CourseCtl validate started");

		boolean isValid = true;
		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			isValid = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Invalid Course Name");
			isValid = false;
		}
		if (DataValidator.isNull(request.getParameter("duration"))) {
			request.setAttribute("duration", PropertyReader.getValue("error.require", "Duration"));
			isValid = false;
		}
		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			isValid = false;
		}

		log.debug("CourseCtl validate Ended with status: " + isValid);
		return isValid;
	}

	/**
	 * Populates and returns a CourseBean from HTTP request parameters.
	 * 
	 * @param request HttpServletRequest containing form parameters.
	 * @return populated CourseBean.
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.debug("CourseCtl populateBean started");

		CourseBean bean = new CourseBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDuration(DataUtility.getString(request.getParameter("duration")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);

		log.debug("CourseCtl populateBean ended with bean: " + bean);
		return bean;
	}

	/**
	 * Handles HTTP GET requests. Loads Course details by id if provided and
	 * forwards to the Course view.
	 * 
	 * @param request  HttpServletRequest object.
	 * @param response HttpServletResponse object.
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CourseCtl doGet started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		CourseModel model = new CourseModel();
		if (id > 0 || op != null) {
			try {
				
				CourseBean bean = model.findByPk(id);
				if (bean != null) {
					
					ServletUtility.setBean(bean, request);
				}
			} catch (Exception e) {
				log.error("CourseCtl doGet Exception", e);
			}
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("CourseCtl doGet Ended");
	}

	/**
	 * Handles HTTP POST requests. Performs save, update, cancel, or reset
	 * operations based on the operation parameter.
	 * 
	 * @param request  HttpServletRequest object.
	 * @param response HttpServletResponse object.
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.debug("CourseCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));
		CourseModel model = new CourseModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			CourseBean bean = (CourseBean) populateBean(request);
			try {
				
				long pk = model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Data Successfully saved", request);
				log.info("Course added successfully with ID: " + pk);
			} catch (ApplicationException e) {
				
				log.error("ApplicationException in CourseCtl doPost Save", e);
			} catch (DuplicateRecordException e) {
				
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Course already exists", request);
				log.error("DuplicateRecordException in CourseCtl doPost Save", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			CourseBean bean = (CourseBean) populateBean(request);
			try {
				if (bean.getId() > 0) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Data Successfully Updated", request);
					log.info("Course updated successfully with ID: " + bean.getId());
				}
			} catch (ApplicationException e) {
				
				log.error("ApplicationException in CourseCtl doPost Update", e);
			} catch (DuplicateRecordException e) {
				
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Course Already exists", request);
				log.error("DuplicateRecordException in CourseCtl doPost Update", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COURSE_LIS_CTL, request, response);
			log.debug("CourseCtl doPost Cancel Operation");
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			
			ServletUtility.redirect(ORSView.COURSE_CTL, request, response);
			log.debug("CourseCtl doPost Reset Operation");
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("CourseCtl doPost Ended");
	}

	/**
	 * Returns the view page for CourseCtl.
	 * 
	 * @return String view path.
	 */
	@Override
	protected String getView() {
		return ORSView.COURSE_VIEW;
	}
}